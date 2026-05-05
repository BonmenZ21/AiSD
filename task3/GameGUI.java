package ru.vsu.cs.course1;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GameGUI extends JFrame {
    private JRadioButton stdRadio;
    private JRadioButton customRadio;
    private JTextArea resultArea;
    private JTextField fileField;
    private List<Card> originalDeck;

    public GameGUI() {
        setTitle("Симуляция игры с картами");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Файл с картами:"));
        fileField = new JTextField(20);
        fileField.setEditable(false);
        controlPanel.add(fileField);
        JButton browseBtn = new JButton("Обзор...");
        browseBtn.addActionListener(e -> chooseFile());
        controlPanel.add(browseBtn);

        stdRadio = new JRadioButton("Стандартная (ArrayDeque)", true);
        customRadio = new JRadioButton("Самописная (SimpleDeque)");
        ButtonGroup group = new ButtonGroup();
        group.add(stdRadio);
        group.add(customRadio);
        controlPanel.add(stdRadio);
        controlPanel.add(customRadio);

        JButton startBtn = new JButton("Запустить игру");
        startBtn.addActionListener(e -> runGame());
        controlPanel.add(startBtn);

        add(controlPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(resultArea);
        add(scroll, BorderLayout.CENTER);
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы", "txt"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            fileField.setText(file.getAbsolutePath());
            loadDeckFromFile(file);
        }
    }

    private void loadDeckFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            originalDeck = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        originalDeck.add(Card.fromString(part));
                    }
                }
            }
            JOptionPane.showMessageDialog(this,
                    "Загружено карт: " + originalDeck.size(),
                    "Успешно", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка чтения файла: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            originalDeck = null;
        }
    }

    private void runGame() {
        if (originalDeck == null || originalDeck.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Сначала загрузите колоду из файла!",
                    "Нет данных", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Перемешиваем
            List<Card> shuffled = new ArrayList<>(originalDeck);
            Collections.shuffle(shuffled);
            System.out.println("Перемешанная колода: " + shuffled);

            // 2. Создаём структуры
            Deque<Card> deck;
            Deque<Card> table;

            if (stdRadio.isSelected()) {
                deck = new ArrayDeque<>(shuffled);
                table = new ArrayDeque<>();
            } else {
                deck = new SimpleDeque<>();
                table = new SimpleDeque<>();
                for (Card c : shuffled) {
                    deck.addLast(c);
                }
            }

            // 3. Запускаем игру
            int moves = TaskLogic.solve(deck, table);

            // 4. Формируем отчёт
            StringBuilder sb = new StringBuilder();
            sb.append("=== РЕЗУЛЬТАТ ИГРЫ ===\n");
            sb.append("Использована реализация: ")
                    .append(stdRadio.isSelected() ? "стандартная (ArrayDeque)" : "самописная (SimpleDeque)")
                    .append("\n\n");

            sb.append("Исходная перемешанная колода:\n");
            for (int i = 0; i < shuffled.size(); i++) {
                sb.append(String.format("  %2d. %s\n", i + 1, shuffled.get(i)));
            }
            sb.append("\n");

            sb.append("а) Количество ходов (карт на столе): ").append(moves).append("\n\n");

            sb.append("б) Карты на столе (снизу вверх):\n");
            List<Card> tableList = new ArrayList<>();
            for (Card c : table) tableList.add(c);
            Collections.reverse(tableList);
            if (tableList.isEmpty()) {
                sb.append("  (стол пуст)\n");
            } else {
                for (int i = 0; i < tableList.size(); i++) {
                    sb.append(String.format("  %2d. %s\n", i + 1, tableList.get(i)));
                }
            }
            sb.append("\n");

            sb.append("в) Карты, оставшиеся в колоде (от первой к последней):\n");
            int idx = 1;
            if (deck.isEmpty()) {
                sb.append("  (колода пуста)\n");
            } else {
                for (Card c : deck) {
                    sb.append(String.format("  %2d. %s\n", idx++, c));
                }
            }

            resultArea.setText(sb.toString());
            System.out.println("GUI обновлён, игра завершена.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Ошибка во время игры:\n" + e.toString(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI().setVisible(true));
    }
}