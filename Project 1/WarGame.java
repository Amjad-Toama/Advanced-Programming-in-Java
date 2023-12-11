import javax.swing.JOptionPane;

/* The class simulate war cards game, each round the announce who beats the other. */
public class WarGame {

	public static void main(String[] args) {
		/* deck is the hole deck of cards.
		 * deck1 is player 1 deck.
		 * deck2 is player 2 deck.
		 */
		DeckOfCards deck = new DeckOfCards(52);
		DeckOfCards deck1 = new DeckOfCards(); // Empty Deck.
		DeckOfCards deck2 = new DeckOfCards(); // Empty Deck.

		// Shuffle the hole deck.
		deck.shuffle();

		// Split the hole deck between both players.
		splitDeck(deck1, deck2, deck);

		// Start The game.
		warGame(deck1, deck2);

	}

	/* warGame method simulate war playing cards game, 
	 * The instructions are both players deal one card each round,
	 * the owner of the higher value card add both card to the bottom of his deck.
	 * If both cards are have equal value each player deal three card,
	 * and the owner of higher value third card add all the cards on the table to his deck.
	 * The player who drain his deck lose.
	 */
	/** NOTE: I limit the number of rounds to MAX_ROUNDS in order to avoid countless rounds number.
	 * The limitation implemented in line 44 at while loop, In case to cancel the limitation,
	 * change the condition (countRounds++ < MAX_ROUNDS) with (true) and delete line 121.
	 */
	private static void warGame(DeckOfCards deck1, DeckOfCards deck2) {
		// Empty deck, will contain dealt cards. concatenate it to the winner deck at each deal.
		DeckOfCards tableDeck = new DeckOfCards();

		final int MAX_ROUNDS = 35;

		// Limits the rounds in order to avoid long term game (infinite loop).
		int countRounds = 0;

		// The game run till at least one player his deck empty.
		// to cancel the limitation change the condition with (true) and delete line 121.
		while (++countRounds < MAX_ROUNDS) {

			// Deal cards from top of the deck of both players.
			Card card1 = deck1.dealCard();	
			Card card2 = deck2.dealCard();	

			// Calculate card value.
			int card1Value = cardValue(card1);
			int card2Value = cardValue(card2);

			// Insert the cards to the tempDeck.
			tableDeck.addToDeck(card1);
			tableDeck.addToDeck(card2);

			/* If both cards values are equal, each player deal three cards,
			 * till the values of the last dealt cards are different.
			 */
			if(card1Value == card2Value) {
				do {
					/* Before deal three cards check if both players have enough cards,
					 * otherwise announce Winner if exist and terminate the game.
					 */
					if (deck1.getSize() < 3 && deck2.getSize() < 3) { // Both players have empty deck.
						JOptionPane.showMessageDialog(null, "Game Over\nTie.");
						return;
					} else if (deck1.getSize() < 3) {	// Player 1 drained his deck.
						JOptionPane.showMessageDialog(null, "Game Over\nPlayer 2 is The Winner");
						return;
					} else if (deck2.getSize() < 3) {	// Player 2 drained his deck.
						JOptionPane.showMessageDialog(null, "Game Over\nPlayer 1 is The Winner");
						return;
					}

					JOptionPane.showMessageDialog(null, "Player 1:" + card1 + " -WAR- " + card2 + ":Player 2");

					// Deal three cards from Player 1 and put them on the table.
					for (int index = 0; index < 3; index++) {
						card1 = deck1.dealCard();
						// Add the card that dealt to the table deck.
						tableDeck.addToDeck(card1);
					}

					// Deal three cards from Player 2 and put them on the table.
					for (int index = 0; index < 3; index++) {
						card2 = deck2.dealCard();
						// Add the card that dealt to the table deck.
						tableDeck.addToDeck(card2);
					}

				} while((card1Value = cardValue(card1)) == (card2Value = cardValue(card2)));
			}	

			/* The player who own the last card with the highest value,
			 * add the table deck to the bottom of his own deck.
			 */
			if (card2Value < card1Value) {
				deck1.addToDeck(tableDeck);	// Add the table deck to deck1 
				JOptionPane.showMessageDialog(null, "Player 1: " + card1 + "\nPlayer 2: " + card2 + "\nPlayer 1 Beats Player 2");
				if (deck2.getSize() == 0) {	// Check if There is winner
					JOptionPane.showMessageDialog(null, "Game Over\nPlayer 1 is The Winner");
					return;
				}
			} else if (card1Value < card2Value) {
				deck2.addToDeck(tableDeck);	// Add the table deck to deck2
				JOptionPane.showMessageDialog(null, "Player 1: " + card1 + "\nPlayer 2: " + card2 + "\nPlayer 2 Beats Player 1");
				if (deck1.getSize() == 0) {	// Check if There is winner
					JOptionPane.showMessageDialog(null, "Game Over\nPlayer 2 is The Winner");
					return;
				}
			}

		}

		JOptionPane.showMessageDialog(null, "Game Over\nNo winner announced.");
	}

	/* The method split the deck between both players.*/
	public static void splitDeck(DeckOfCards deck1, DeckOfCards deck2, DeckOfCards deck) {
		// Each round deal card from the deck to players deck till it drained.
		while(deck.getSize() > 0) {
			deck1.addToDeck(deck.dealCard());	// add the card to deck1.
			deck2.addToDeck(deck.dealCard());	// add the card to deck2.
		}
	}

	/* Assess the value of card and return it. */
	private static int cardValue(Card card) {
		// The value of the card assess according to its face.
		String[] faces = {"Ace", "Deuce", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
		int index;

		// Search for the appropriate.
		for(index = 0; index < 13; index++) 
			if(card.getFace().equals(faces[index])) 
				break;

		return index + 1;
	}

}
