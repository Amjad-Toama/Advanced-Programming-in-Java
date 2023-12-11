import java.util.ArrayList;

public class Sum extends Thread {
	
	private Monitor m;
	
	public Sum(Monitor m) {
		this.m = m;
	}
	
	public void run() {
		super.run();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		/*While there is more element to sum, run. */
		while((temp = m.getValues()) != null) {
			int partialSum = 0;		// maintain the partial sum at each iteration.
			while(temp.size() != 0)	// while there is more elements at the temp arrayList
				partialSum += temp.remove(0);
			m.addNewElement(partialSum);	// add total partial sum as element to arraylist.
		}
	}
	
}
