import java.security.SecureRandom;
import java.util.ArrayList;

/* The class manage deck of cards, such as, shuffle, deal card and add. */
public class DeckOfCards {
	private static final SecureRandom randomNumber = new SecureRandom();
	private ArrayList<Card> deck = new ArrayList<Card>();

	/* Constructors */
	// Empty constructor.
	public DeckOfCards() {

	}

	// The constructor build deck of card consist with numberOfCards cards.
	public DeckOfCards(int numberOfCards) {
		String[] faces = {"Ace", "Deuce", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
		String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

		// add cards to the Arraylist.
		for(int i = 0; i < numberOfCards; i++)
			deck.add(new Card(faces[i%13], suits[i/13]));
	}

	/*Shuffle the deck.*/
	public void shuffle() {

		for(int index = 0; index < deck.size(); index++) {
			int aux = randomNumber.nextInt(deck.size());

			Card temp = deck.get(index);
			deck.set(index, deck.get(aux));
			deck.set(aux, temp);
		}
	}

	/*
	 * Deal the first card from the deck, and remove it from the deck. then return it.
	 */
	public Card dealCard() {
		if(deck.size() > 0) {
			// Deal the first card.
			Card temp = deck.get(0);
			// Remove it from the deck.
			deck.remove(temp);
			return temp;
		}
		return null;
	}

	// Returns the amount of cards in deck.
	public int getSize() {
		return deck.size();
	}

	// Add card to the deck.
	public void addToDeck(Card card) {
		deck.add(card);
	}

	// Add the hole deck to this deck
	public void addToDeck(DeckOfCards deck) {
		Card temp;

		while((temp = deck.dealCard()) != null)
			this.deck.add(temp);
	}

}
