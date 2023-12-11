/* The class define cards attributes, face and suit. */
public class Card {
	private String face;
	private String suit;
	/* Constructor */
	public Card(String cardFace, String cardSuit) {
		this.face = cardFace;
		this.suit = cardSuit;
	}

	// Return the details of the card as string.
	public String toString() {
		return face + " of " + suit;
	}

	// Get the face value.
	public String getFace() {
		return this.face;
	}

	// Get the suit value.
	public String getSuit() {
		return this.suit;
	}

}
