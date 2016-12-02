package org.redstone.tools;
import java.util.Random;

public class Test
{
	public static void main(String[] args)
	{
		System.out.println("XRanom :");
		XRandom rand = new XRandom(50L);
		for (int i = 0; i < 5; i++)
			System.out.print(rand.nextInt(100) + "\t");

		for (int i = 0; i < 5; i++)
			System.out.print(rand.nextFloat()+ "\t");

		for (int i = 0; i < 5; i++)
			System.out.print(rand.nextDouble()+ "\t");
		

		System.out.println("\njava.util.Random :");
		Random rand_ = new Random(50L);
		for (int i = 0; i < 5; i++)
			System.out.print(rand_.nextInt(100)+ "\t");

		for (int i = 0; i < 5; i++)
			System.out.print(rand_.nextFloat()+ "\t");

		for (int i = 0; i < 5; i++)
			System.out.print(rand_.nextDouble()+ "\t");
	}
}
