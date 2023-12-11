import java.util.*;
/* Question 1:
 * The program sum up the array elements.
 */
public class Driver {
	
	public static void main(String[]args) {
		int n, m;
		Scanner scan = new Scanner(System.in);

		/* output/input process from the user. */
		System.out.println("Welcome to Multi-threaded Sum Up Array Program");
		System.out.print("Specify the Amount of Values: ");
		n = scan.nextInt();
		System.out.print("Specify the Amount of threads: ");
		m = scan.nextInt();
		
		// Initialzie the array, allocate and set values
		int[] arr = new int[n];
		initializeArray(arr);
		Monitor mentor = new Monitor(arr);
		
		// Run the threads.
		for (int i = 0; i < m; i++) {
			new Sum(mentor).start();
		}
		
		System.out.println("The sum is : " + mentor.getTotalSum());
		scan.close();
	}
	
	/* Initialze the arr with random values between 1 till UPPER_BOUND 
	 * Then print it.
	 */
	private static void initializeArray(int[] arr) {
		final int UPPER_BOUND = 100;
		String str = "{";
		Random rand = new Random();
		for(int i = 0; i < arr.length; i++) {
			arr[i] = rand.nextInt(UPPER_BOUND) + 1;
			str = str + arr[i] + " ";
		}
		str += "}";
		System.out.println(str);
	}
	
}
