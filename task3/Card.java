package ru.vsu.cs.course1;

import java.util.Objects;

public class Card {
    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }
    public enum Rank {
        SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() { return suit; }
    public Rank getRank() { return rank; }

    /**
     * Проверяет, можно ли положить данную карту на другую карту.
     * Условие: совпадает масть ИЛИ совпадает достоинство.
     */
    public boolean canBePlacedOn(Card other) {
        return this.suit == other.suit || this.rank == other.rank;
    }

    @Override
    public String toString() {
        String rankStr = switch (rank) {
            case SIX -> "6"; case SEVEN -> "7"; case EIGHT -> "8"; case NINE -> "9";
            case TEN -> "10"; case JACK -> "В"; case QUEEN -> "Д"; case KING -> "К";
            case ACE -> "Т";
        };
        String suitStr = switch (suit) {
            case HEARTS -> "♥"; case DIAMONDS -> "♦"; case CLUBS -> "♣"; case SPADES -> "♠";
        };
        return rankStr + suitStr;
    }

    // Фабричный метод для создания карты из строки вида "6H", "7D", "JC", "AS" и т.п.
    public static Card fromString(String s) {
        if (s == null || s.length() < 2) throw new IllegalArgumentException("Неверный формат карты: " + s);
        String rankPart = s.substring(0, s.length() - 1);
        char suitChar = Character.toUpperCase(s.charAt(s.length() - 1));

        Rank rank;
        switch (rankPart) {
            case "6": rank = Rank.SIX; break;
            case "7": rank = Rank.SEVEN; break;
            case "8": rank = Rank.EIGHT; break;
            case "9": rank = Rank.NINE; break;
            case "10": rank = Rank.TEN; break;
            case "J": rank = Rank.JACK; break;
            case "Q": rank = Rank.QUEEN; break;
            case "K": rank = Rank.KING; break;
            case "A": rank = Rank.ACE; break;
            default: throw new IllegalArgumentException("Неизвестное достоинство: " + rankPart);
        }

        Suit suit;
        switch (suitChar) {
            case 'H': suit = Suit.HEARTS; break;
            case 'D': suit = Suit.DIAMONDS; break;
            case 'C': suit = Suit.CLUBS; break;
            case 'S': suit = Suit.SPADES; break;
            default: throw new IllegalArgumentException("Неизвестная масть: " + suitChar);
        }
        return new Card(suit, rank);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}