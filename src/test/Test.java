package test;

import java.util.Random;

public class Test {

	public static void main(String[] args) {
		Random random = new Random(System.currentTimeMillis());
		System.exit(random.nextInt(50)%2);
	}
}
