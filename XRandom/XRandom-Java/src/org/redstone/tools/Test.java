package org.redstone.tools;
import java.util.Random;

public class Test
{
	public static void main(String[] args)
	{
		Random rand_ = new Random(50L);
		XRandom rand = new XRandom(50L);
		for (int i = 0; i < 5; i++)
			System.out.println(rand.nextInt(100));

		for (int i = 0; i < 5; i++)
			System.out.println(rand.nextFloat());

		for (int i = 0; i < 5; i++)
			System.out.println(rand.nextDouble());
		
		for (int i = 0; i < 5; i++)
			System.out.println(rand_.nextInt(100));

		for (int i = 0; i < 5; i++)
			System.out.println(rand_.nextFloat());

		for (int i = 0; i < 5; i++)
			System.out.println(rand_.nextDouble());
	}
}
