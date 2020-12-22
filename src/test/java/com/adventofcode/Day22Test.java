package com.adventofcode;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day22Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day22Test.class);

    private static long playCrabCombat(Scanner scanner) {
        Deque<Integer> player1deck = new LinkedList<>();
        Deque<Integer> player2deck = new LinkedList<>();
        readPlayerDecks(scanner, player1deck, player2deck);

        int round = 0;
        while (!player1deck.isEmpty() && !player2deck.isEmpty()) {
            int card1 = player1deck.removeFirst();
            int card2 = player2deck.removeFirst();
            if (card1 > card2) {
                LOGGER.debug("Round {} : ({} ; {}), player 1 wins", ++round, card1, card2);
                player1deck.addLast(card1);
                player1deck.addLast(card2);
            } else {
                LOGGER.debug("Round {} : ({} ; {}), player 2 wins", ++round, card1, card2);
                player2deck.addLast(card2);
                player2deck.addLast(card1);
            }
        }

        return countPoint(player1deck, player2deck);
    }

    private static long countPoint(Deque<Integer> player1deck, Deque<Integer> player2deck) {
        if (player1deck.isEmpty()) {
            LOGGER.info("Player 2 wins !");
            return countPoint(player2deck);
        } else {
            LOGGER.info("Player 1 wins !");
            return countPoint(player1deck);
        }
    }

    private static long countPoint(Deque<Integer> deck) {
        long point = 0;
        long cardPoint = deck.size();
        for (Integer card : deck) {
            point += cardPoint * card;
            --cardPoint;
        }
        return point;
    }

    private static long playRecursiveCombat(Scanner scanner) {
        Deque<Integer> player1deck = new LinkedList<>();
        Deque<Integer> player2deck = new LinkedList<>();
        readPlayerDecks(scanner, player1deck, player2deck);

        Pair<Integer, Long> result = playRecursiveCombat(player1deck, player2deck, 1);
        return result.getRight();
    }

    private static void readPlayerDecks(Scanner scanner, Deque<Integer> player1deck, Deque<Integer> player2deck) {
        boolean readPlayer2 = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                readPlayer2 = true;
                continue;
            }
            if (line.startsWith("Player ")) {
                continue;
            }
            int card = Integer.parseInt(line);
            if (readPlayer2) {
                player2deck.add(card);
            } else {
                player1deck.add(card);
            }
        }
    }

    private static Pair<Integer, Long> playRecursiveCombat(Deque<Integer> player1deck, Deque<Integer> player2deck, int depth) {
        Set<Pair<List<Integer>, List<Integer>>> cache = new HashSet<>();
        int round = 0;
        while (!player1deck.isEmpty() && !player2deck.isEmpty()) {
            if (!cache.add(Pair.of(List.copyOf(player1deck), List.copyOf(player2deck)))) {
                LOGGER.info("Found recursive decks: {}, {}", player1deck, player2deck);
                return Pair.of(1, countPoint(player1deck));
            }
            int card1 = player1deck.removeFirst();
            int card2 = player2deck.removeFirst();
            if (card1 <= player1deck.size() && card2 <= player2deck.size()) {
                LinkedList<Integer> newPlayer1Deck = player1deck.stream().limit(card1).collect(Collectors.toCollection(LinkedList::new));
                LinkedList<Integer> newPlayer2Deck = player2deck.stream().limit(card2).collect(Collectors.toCollection(LinkedList::new));

                LOGGER.debug("Launching sub-game with {} and {}", newPlayer1Deck, newPlayer2Deck);
                Pair<Integer, Long> subGameResult = playRecursiveCombat(newPlayer1Deck, newPlayer2Deck, depth + 1);
                if (subGameResult.getLeft() == 1) {
                    LOGGER.trace("Round {}.{} : ({} ; {}), player 1 wins", depth, ++round, card1, card2);
                    player1deck.addLast(card1);
                    player1deck.addLast(card2);
                } else {
                    LOGGER.trace("Round {}.{} : ({} ; {}), player 2 wins", depth, ++round, card1, card2);
                    player2deck.addLast(card2);
                    player2deck.addLast(card1);
                }
            } else if (card1 > card2) {
                LOGGER.trace("Round {}.{} : ({} ; {}), player 1 wins", depth, ++round, card1, card2);
                player1deck.addLast(card1);
                player1deck.addLast(card2);
            } else {
                LOGGER.trace("Round {}.{} : ({} ; {}), player 2 wins", depth, ++round, card1, card2);
                player2deck.addLast(card2);
                player2deck.addLast(card1);
            }
        }

        if (player1deck.isEmpty()) {
            LOGGER.debug("Game {} Player 2 wins !", depth);
            return Pair.of(2, countPoint(player2deck));
        } else {
            LOGGER.debug("Game {} Player 1 wins !", depth);
            return Pair.of(1, countPoint(player1deck));
        }
    }

    @Test
    void testCrabCombat() {
        String input = """
                Player 1:
                9
                2
                6
                3
                1
                                
                Player 2:
                5
                8
                4
                7
                10""";

        Scanner scanner = new Scanner(input);
        long point = playCrabCombat(scanner);
        assertThat(point).isEqualTo(306);

        long recursiveCombat = playRecursiveCombat(new Scanner(input));
        assertThat(recursiveCombat).isEqualTo(291);
    }

    /**
     * --- Day 22: Crab Combat ---
     * <p>
     * It only takes a few hours of sailing the ocean on a raft for boredom to
     * sink in. Fortunately, you brought a small deck of space cards! You'd like
     * to play a game of Combat, and there's even an opponent available: a small
     * crab that climbed aboard your raft before you left.
     * <p>
     * Fortunately, it doesn't take long to teach the crab the rules.
     * <p>
     * Before the game starts, split the cards so each player has their own deck
     * (your puzzle input). Then, the game consists of a series of rounds: both
     * players draw their top card, and the player with the higher-valued card
     * wins the round. The winner keeps both cards, placing them on the bottom of
     * their own deck so that the winner's card is above the other card. If this
     * causes a player to have all of the cards, they win, and the game ends.
     * <p>
     * For example, consider the following starting decks:
     * <p>
     * Player 1:
     * 9
     * 2
     * 6
     * 3
     * 1
     * <p>
     * Player 2:
     * 5
     * 8
     * 4
     * 7
     * 10
     * <p>
     * This arrangement means that player 1's deck contains 5 cards, with 9 on top
     * and 1 on the bottom; player 2's deck also contains 5 cards, with 5 on top
     * and 10 on the bottom.
     * <p>
     * The first round begins with both players drawing the top card of their
     * decks: 9 and 5. Player 1 has the higher card, so both cards move to the
     * bottom of player 1's deck such that 9 is above 5. In total, it takes 29
     * rounds before a player has all of the cards:
     * <p>
     * -- Round 1 --
     * Player 1's deck: 9, 2, 6, 3, 1
     * Player 2's deck: 5, 8, 4, 7, 10
     * Player 1 plays: 9
     * Player 2 plays: 5
     * Player 1 wins the round!
     * <p>
     * -- Round 2 --
     * Player 1's deck: 2, 6, 3, 1, 9, 5
     * Player 2's deck: 8, 4, 7, 10
     * Player 1 plays: 2
     * Player 2 plays: 8
     * Player 2 wins the round!
     * <p>
     * -- Round 3 --
     * Player 1's deck: 6, 3, 1, 9, 5
     * Player 2's deck: 4, 7, 10, 8, 2
     * Player 1 plays: 6
     * Player 2 plays: 4
     * Player 1 wins the round!
     * <p>
     * -- Round 4 --
     * Player 1's deck: 3, 1, 9, 5, 6, 4
     * Player 2's deck: 7, 10, 8, 2
     * Player 1 plays: 3
     * Player 2 plays: 7
     * Player 2 wins the round!
     * <p>
     * -- Round 5 --
     * Player 1's deck: 1, 9, 5, 6, 4
     * Player 2's deck: 10, 8, 2, 7, 3
     * Player 1 plays: 1
     * Player 2 plays: 10
     * Player 2 wins the round!
     * <p>
     * ...several more rounds pass...
     * <p>
     * -- Round 27 --
     * Player 1's deck: 5, 4, 1
     * Player 2's deck: 8, 9, 7, 3, 2, 10, 6
     * Player 1 plays: 5
     * Player 2 plays: 8
     * Player 2 wins the round!
     * <p>
     * -- Round 28 --
     * Player 1's deck: 4, 1
     * Player 2's deck: 9, 7, 3, 2, 10, 6, 8, 5
     * Player 1 plays: 4
     * Player 2 plays: 9
     * Player 2 wins the round!
     * <p>
     * -- Round 29 --
     * Player 1's deck: 1
     * Player 2's deck: 7, 3, 2, 10, 6, 8, 5, 9, 4
     * Player 1 plays: 1
     * Player 2 plays: 7
     * Player 2 wins the round!
     * <p>
     * <p>
     * == Post-game results ==
     * Player 1's deck:
     * Player 2's deck: 3, 2, 10, 6, 8, 5, 9, 4, 7, 1
     * <p>
     * Once the game ends, you can calculate the winning player's score. The
     * bottom card in their deck is worth the value of the card multiplied by 1,
     * the second-from-the-bottom card is worth the value of the card multiplied
     * by 2, and so on. With 10 cards, the top card is worth the value on the card
     * multiplied by 10. In this example, the winning player's score is:
     * <p>
     * 3 * 10
     * +  2 *  9
     * + 10 *  8
     * +  6 *  7
     * +  8 *  6
     * +  5 *  5
     * +  9 *  4
     * +  4 *  3
     * +  7 *  2
     * +  1 *  1
     * = 306
     * <p>
     * So, once the game ends, the winning player's score is 306.
     * <p>
     * Play the small crab in a game of Combat using the two decks you just dealt.
     * What is the winning player's score?
     * <p>
     * --- Part Two ---
     * <p>
     * You lost to the small crab! Fortunately, crabs aren't very good at
     * recursion. To defend your honor as a Raft Captain, you challenge the small
     * crab to a game of Recursive Combat.
     * <p>
     * Recursive Combat still starts by splitting the cards into two decks (you
     * offer to play with the same starting decks as before - it's only fair).
     * Then, the game consists of a series of rounds with a few changes:
     * <p>
     * - Before either player deals a card, if there was a previous round in
     * this game that had exactly the same cards in the same order in the
     * same players' decks, the game instantly ends in a win for player 1.
     * Previous rounds from other games are not considered. (This prevents
     * infinite games of Recursive Combat, which everyone agrees is a bad
     * idea.)
     * - Otherwise, this round's cards must be in a new configuration; the
     * players begin the round by each drawing the top card of their deck as
     * normal.
     * - If both players have at least as many cards remaining in their deck as
     * the value of the card they just drew, the winner of the round is
     * determined by playing a new game of Recursive Combat (see below).
     * - Otherwise, at least one player must not have enough cards left in
     * their deck to recurse; the winner of the round is the player with the
     * higher-value card.
     * <p>
     * As in regular Combat, the winner of the round (even if they won the round
     * by winning a sub-game) takes the two cards dealt at the beginning of the
     * round and places them on the bottom of their own deck (again so that the
     * winner's card is above the other card). Note that the winner's card might
     * be the lower-valued of the two cards if they won the round due to winning a
     * sub-game. If collecting cards by winning the round causes a player to have
     * all of the cards, they win, and the game ends.
     * <p>
     * Here is an example of a small game that would loop forever without the
     * infinite game prevention rule:
     * <p>
     * Player 1:
     * 43
     * 19
     * <p>
     * Player 2:
     * 2
     * 29
     * 14
     * <p>
     * During a round of Recursive Combat, if both players have at least as many
     * cards in their own decks as the number on the card they just dealt, the
     * winner of the round is determined by recursing into a sub-game of Recursive
     * Combat. (For example, if player 1 draws the 3 card, and player 2 draws the
     * 7 card, this would occur if player 1 has at least 3 cards left and player 2
     * has at least 7 cards left, not counting the 3 and 7 cards that were drawn.)
     * <p>
     * To play a sub-game of Recursive Combat, each player creates a new deck by
     * making a copy of the next cards in their deck (the quantity of cards copied
     * is equal to the number on the card they drew to trigger the sub-game).
     * During this sub-game, the game that triggered it is on hold and completely
     * unaffected; no cards are removed from players' decks to form the sub-game.
     * (For example, if player 1 drew the 3 card, their deck in the sub-game would
     * be copies of the next three cards in their deck.)
     * <p>
     * Here is a complete example of gameplay, where Game 1 is the primary game of
     * Recursive Combat:
     * <p>
     * === Game 1 ===
     * <p>
     * -- Round 1 (Game 1) --
     * Player 1's deck: 9, 2, 6, 3, 1
     * Player 2's deck: 5, 8, 4, 7, 10
     * Player 1 plays: 9
     * Player 2 plays: 5
     * Player 1 wins round 1 of game 1!
     * <p>
     * -- Round 2 (Game 1) --
     * Player 1's deck: 2, 6, 3, 1, 9, 5
     * Player 2's deck: 8, 4, 7, 10
     * Player 1 plays: 2
     * Player 2 plays: 8
     * Player 2 wins round 2 of game 1!
     * <p>
     * -- Round 3 (Game 1) --
     * Player 1's deck: 6, 3, 1, 9, 5
     * Player 2's deck: 4, 7, 10, 8, 2
     * Player 1 plays: 6
     * Player 2 plays: 4
     * Player 1 wins round 3 of game 1!
     * <p>
     * -- Round 4 (Game 1) --
     * Player 1's deck: 3, 1, 9, 5, 6, 4
     * Player 2's deck: 7, 10, 8, 2
     * Player 1 plays: 3
     * Player 2 plays: 7
     * Player 2 wins round 4 of game 1!
     * <p>
     * -- Round 5 (Game 1) --
     * Player 1's deck: 1, 9, 5, 6, 4
     * Player 2's deck: 10, 8, 2, 7, 3
     * Player 1 plays: 1
     * Player 2 plays: 10
     * Player 2 wins round 5 of game 1!
     * <p>
     * -- Round 6 (Game 1) --
     * Player 1's deck: 9, 5, 6, 4
     * Player 2's deck: 8, 2, 7, 3, 10, 1
     * Player 1 plays: 9
     * Player 2 plays: 8
     * Player 1 wins round 6 of game 1!
     * <p>
     * -- Round 7 (Game 1) --
     * Player 1's deck: 5, 6, 4, 9, 8
     * Player 2's deck: 2, 7, 3, 10, 1
     * Player 1 plays: 5
     * Player 2 plays: 2
     * Player 1 wins round 7 of game 1!
     * <p>
     * -- Round 8 (Game 1) --
     * Player 1's deck: 6, 4, 9, 8, 5, 2
     * Player 2's deck: 7, 3, 10, 1
     * Player 1 plays: 6
     * Player 2 plays: 7
     * Player 2 wins round 8 of game 1!
     * <p>
     * -- Round 9 (Game 1) --
     * Player 1's deck: 4, 9, 8, 5, 2
     * Player 2's deck: 3, 10, 1, 7, 6
     * Player 1 plays: 4
     * Player 2 plays: 3
     * Playing a sub-game to determine the winner...
     * <p>
     * === Game 2 ===
     * <p>
     * -- Round 1 (Game 2) --
     * Player 1's deck: 9, 8, 5, 2
     * Player 2's deck: 10, 1, 7
     * Player 1 plays: 9
     * Player 2 plays: 10
     * Player 2 wins round 1 of game 2!
     * <p>
     * -- Round 2 (Game 2) --
     * Player 1's deck: 8, 5, 2
     * Player 2's deck: 1, 7, 10, 9
     * Player 1 plays: 8
     * Player 2 plays: 1
     * Player 1 wins round 2 of game 2!
     * <p>
     * -- Round 3 (Game 2) --
     * Player 1's deck: 5, 2, 8, 1
     * Player 2's deck: 7, 10, 9
     * Player 1 plays: 5
     * Player 2 plays: 7
     * Player 2 wins round 3 of game 2!
     * <p>
     * -- Round 4 (Game 2) --
     * Player 1's deck: 2, 8, 1
     * Player 2's deck: 10, 9, 7, 5
     * Player 1 plays: 2
     * Player 2 plays: 10
     * Player 2 wins round 4 of game 2!
     * <p>
     * -- Round 5 (Game 2) --
     * Player 1's deck: 8, 1
     * Player 2's deck: 9, 7, 5, 10, 2
     * Player 1 plays: 8
     * Player 2 plays: 9
     * Player 2 wins round 5 of game 2!
     * <p>
     * -- Round 6 (Game 2) --
     * Player 1's deck: 1
     * Player 2's deck: 7, 5, 10, 2, 9, 8
     * Player 1 plays: 1
     * Player 2 plays: 7
     * Player 2 wins round 6 of game 2!
     * The winner of game 2 is player 2!
     * <p>
     * ...anyway, back to game 1.
     * Player 2 wins round 9 of game 1!
     * <p>
     * -- Round 10 (Game 1) --
     * Player 1's deck: 9, 8, 5, 2
     * Player 2's deck: 10, 1, 7, 6, 3, 4
     * Player 1 plays: 9
     * Player 2 plays: 10
     * Player 2 wins round 10 of game 1!
     * <p>
     * -- Round 11 (Game 1) --
     * Player 1's deck: 8, 5, 2
     * Player 2's deck: 1, 7, 6, 3, 4, 10, 9
     * Player 1 plays: 8
     * Player 2 plays: 1
     * Player 1 wins round 11 of game 1!
     * <p>
     * -- Round 12 (Game 1) --
     * Player 1's deck: 5, 2, 8, 1
     * Player 2's deck: 7, 6, 3, 4, 10, 9
     * Player 1 plays: 5
     * Player 2 plays: 7
     * Player 2 wins round 12 of game 1!
     * <p>
     * -- Round 13 (Game 1) --
     * Player 1's deck: 2, 8, 1
     * Player 2's deck: 6, 3, 4, 10, 9, 7, 5
     * Player 1 plays: 2
     * Player 2 plays: 6
     * Playing a sub-game to determine the winner...
     * <p>
     * === Game 3 ===
     * <p>
     * -- Round 1 (Game 3) --
     * Player 1's deck: 8, 1
     * Player 2's deck: 3, 4, 10, 9, 7, 5
     * Player 1 plays: 8
     * Player 2 plays: 3
     * Player 1 wins round 1 of game 3!
     * <p>
     * -- Round 2 (Game 3) --
     * Player 1's deck: 1, 8, 3
     * Player 2's deck: 4, 10, 9, 7, 5
     * Player 1 plays: 1
     * Player 2 plays: 4
     * Playing a sub-game to determine the winner...
     * <p>
     * === Game 4 ===
     * <p>
     * -- Round 1 (Game 4) --
     * Player 1's deck: 8
     * Player 2's deck: 10, 9, 7, 5
     * Player 1 plays: 8
     * Player 2 plays: 10
     * Player 2 wins round 1 of game 4!
     * The winner of game 4 is player 2!
     * <p>
     * ...anyway, back to game 3.
     * Player 2 wins round 2 of game 3!
     * <p>
     * -- Round 3 (Game 3) --
     * Player 1's deck: 8, 3
     * Player 2's deck: 10, 9, 7, 5, 4, 1
     * Player 1 plays: 8
     * Player 2 plays: 10
     * Player 2 wins round 3 of game 3!
     * <p>
     * -- Round 4 (Game 3) --
     * Player 1's deck: 3
     * Player 2's deck: 9, 7, 5, 4, 1, 10, 8
     * Player 1 plays: 3
     * Player 2 plays: 9
     * Player 2 wins round 4 of game 3!
     * The winner of game 3 is player 2!
     * <p>
     * ...anyway, back to game 1.
     * Player 2 wins round 13 of game 1!
     * <p>
     * -- Round 14 (Game 1) --
     * Player 1's deck: 8, 1
     * Player 2's deck: 3, 4, 10, 9, 7, 5, 6, 2
     * Player 1 plays: 8
     * Player 2 plays: 3
     * Player 1 wins round 14 of game 1!
     * <p>
     * -- Round 15 (Game 1) --
     * Player 1's deck: 1, 8, 3
     * Player 2's deck: 4, 10, 9, 7, 5, 6, 2
     * Player 1 plays: 1
     * Player 2 plays: 4
     * Playing a sub-game to determine the winner...
     * <p>
     * === Game 5 ===
     * <p>
     * -- Round 1 (Game 5) --
     * Player 1's deck: 8
     * Player 2's deck: 10, 9, 7, 5
     * Player 1 plays: 8
     * Player 2 plays: 10
     * Player 2 wins round 1 of game 5!
     * The winner of game 5 is player 2!
     * <p>
     * ...anyway, back to game 1.
     * Player 2 wins round 15 of game 1!
     * <p>
     * -- Round 16 (Game 1) --
     * Player 1's deck: 8, 3
     * Player 2's deck: 10, 9, 7, 5, 6, 2, 4, 1
     * Player 1 plays: 8
     * Player 2 plays: 10
     * Player 2 wins round 16 of game 1!
     * <p>
     * -- Round 17 (Game 1) --
     * Player 1's deck: 3
     * Player 2's deck: 9, 7, 5, 6, 2, 4, 1, 10, 8
     * Player 1 plays: 3
     * Player 2 plays: 9
     * Player 2 wins round 17 of game 1!
     * The winner of game 1 is player 2!
     * <p>
     * <p>
     * == Post-game results ==
     * Player 1's deck:
     * Player 2's deck: 7, 5, 6, 2, 4, 1, 10, 8, 9, 3
     * <p>
     * After the game, the winning player's score is calculated from the cards
     * they have in their original deck using the same rules as regular Combat. In
     * the above game, the winning player's score is 291.
     * <p>
     * Defend your honor as Raft Captain by playing the small crab in a game of
     * Recursive Combat using the same two decks as before. What is the winning
     * player's score?
     */
    @Test
    void inputCrabCombat() {
        Scanner scanner = new Scanner(Day21Test.class.getResourceAsStream("/day/22/input"));

        long point = playCrabCombat(scanner);
        assertThat(point).isEqualTo(33393);

        long recursiveCombat = playRecursiveCombat(new Scanner(Day21Test.class.getResourceAsStream("/day/22/input")));
        assertThat(recursiveCombat).isEqualTo(31963);
    }
}
