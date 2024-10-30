package violet.dainty.util;

public class MathHelper {

	public static int smallestPrimeGreaterThan(int n) {
		int current = n + 1 + (n % 2);
		while (true) {
			if (isPrime(current)) return current;
			current += 2;
		}
	}

	public static boolean isPrime(int n) {
		if (n < 2) return false;
		for (int possibleFactor = 2; possibleFactor <= Math.sqrt(n); possibleFactor++) {
			if (n % possibleFactor == 0) return false;
		}
		return true;
	}	
}
