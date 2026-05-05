package ru.vsu.cs.course1;

import java.util.Deque;

public class TaskLogic {

    public static int solve(Deque<Card> deck, Deque<Card> table) {
        System.out.println("TaskLogic.solve started. Deck size: " + deck.size());
        if (deck.isEmpty()) return 0;

        Card first = deck.poll();
        if (first == null) return 0;
        table.push(first);
        int moves = 1;
        System.out.println("First card on table: " + first);

        boolean anyPlaced;
        do {
            anyPlaced = false;
            int sizeBefore = deck.size();
            System.out.println("New cycle, deck size: " + sizeBefore);
            for (int i = 0; i < sizeBefore; i++) {
                Card current = deck.poll();
                if (current == null) break;
                Card top = table.peek();
                if (top != null && current.canBePlacedOn(top)) {
                    table.push(current);
                    moves++;
                    anyPlaced = true;
                    System.out.println("  Placed: " + current + " on " + top);
                } else {
                    deck.addLast(current);
                    System.out.println("  Skipped: " + current + " (top=" + top + ")");
                }
            }
        } while (anyPlaced && !deck.isEmpty());

        System.out.println("Game finished. Moves: " + moves);
        return moves;
    }
}