/**
|-------------------------------------------------------------------------------
| HelloGoodbye
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Oct 20, 2021
| Compilation:  javac HelloGoodbye.java
| Execution:    java HelloGoodbye Kevin Bob
|
| This program displays hello and goodbye messages for provided names.
|
*/

public class HelloGoodbye
{
    public static void main(String[] args)
    {
        System.out.println("Hello " + args[0] + " and " + args[1] + ".");
        System.out.println("Goodbye " + args[1] + " and " + args[0] + ".");
    }
}

