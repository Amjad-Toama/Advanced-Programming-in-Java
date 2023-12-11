import java.util.ArrayList;

public class Monitor {
	/* arr - Contain the array elements
	 * SIZE - The size of temp arraylist (The values to be sum at each thread).
	 * remain - The size of the array, that the array should have even if any other elements
	 * 			are in process.
	 */
	private ArrayList<Integer> arr;
	private final int SIZE = 2;
	private int remain;

	/* Constructor */
	public Monitor(int[] arr) {
		this.arr = new ArrayList<Integer>();
		// Insert the values of the arr to the arraylist.
		for(int i = 0; i < arr.length; i++) 
			this.arr.add(arr[i]);
		remain = this.arr.size();
	}

	/* The method returns temp arraylist. */
	public synchronized ArrayList<Integer> getValues(){
		
		// While there is anymore element to be sumed.
		while(arr.size() < SIZE && remain != 1){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Check if the amount of the element at the arr is sufficient, in order to 
		// create temp arraylist that will be sum.
		if(arr.size() >= SIZE && remain != 1) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			// inset the values to the arraylist.
			for(int i = 0; i < SIZE; i++)
				temp.add(this.arr.remove(0));
			return temp;
		}

		return null;
	}
	
	/* Add new element to the arraylist. */
	public synchronized void addNewElement(int e) {
		this.arr.add(e);		// add the element
		/* The formula that subtract all element except for one.*/
		remain -= (SIZE - 1);	// update the current actual size of the array.
		notifyAll();
	}
	
	/* Method returns the total sum of the array. */
	public synchronized int getTotalSum() {
		// While there is more element to sum wait.
		while(remain != 1) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.arr.get(0);
	}
}
