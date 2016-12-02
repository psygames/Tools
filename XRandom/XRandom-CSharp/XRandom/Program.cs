using System;

namespace RedStone.Tools
{
    class Program
    {
        static void Main(string[] args)
        {
            XRandom rand = new XRandom(50L);
            for (int i = 0; i < 5; i++)
                Console.WriteLine("Int(100): " + rand.nextInt(100));
            for (int i = 0; i < 5; i++)
                Console.WriteLine("Float: " + rand.nextFloat());
            for (int i = 0; i < 5; i++)
                Console.WriteLine("Double: " + rand.nextDouble());
            Console.ReadKey();
        }
    }
}
