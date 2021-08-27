import java.util.*;

/**
 * This class is meant to serve ITP 265 students as a help for getting input and error checking on input, but
 * may also be used as a general purpose class to store methods which may be needed across lots of projects.
 *
 * @author Kendra Walther and Mitchell Maruyama
 * @version Spring 2021 

 */
public class ITPHelper
{
    private Scanner sc;

    /**
     * Constructor sets up a Scanner to be used by the class in order to read input from the standard console window (System.in)
     */
    public ITPHelper() {
        sc = new Scanner(System.in);
    }

    /**
     * Prompt the user and read one word of text as a String
     * @param prompt: the question to ask the user
     * @return: a one word String - if the user enters multiple words, all other input until the return will be discarded.
     */
    public String inputWord(String prompt) {
        System.out.print(prompt + "\n>");
        String word = sc.next();
        sc.nextLine(); // remove any "garbage" (like extra whitespace or the return key) after the one word that is read in
        return word;
    }

    /**
     * Prompt the user and read one line of text as a String
     * @param prompt: the question to ask the user
     * @return: a line of user input (including spaces, until they hit enter)
     */
    public String inputLine(String prompt) {
        System.out.print(prompt + "\n>");
        return sc.nextLine();
    }

    /**
     * Prompt the user and read an int, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user 
     * @return: an int 
     */
    public int inputInt(String prompt) {
        System.out.print(prompt + "\n>");
        while(!(sc.hasNextInt())){
            String garbage = sc.nextLine();
            System.err.println(garbage + " was not an int.");
            System.out.print(prompt + "\n>");
        }
        int num = sc.nextInt();
        sc.nextLine();
        return num;
    }

    /**
     * Prompt the user and read an int between (inclusive) of minValue and maxValue, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user 
     * @return: an int between minValue and maxValue
     */
    public int inputInt(String prompt, int minValue, int maxValue) {
        int num = inputInt(prompt);
        while(num < minValue || num > maxValue){
            System.out.println(num + " was not in the allowed range "+minValue+ "-" +maxValue);
            num = inputInt(prompt);
        }
        return num;
    }
    
    /**
     * Prompt the user and read a floating point number, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user 
     * @return: a double value 
     */
    public double inputDouble(String prompt) {
        System.out.print(prompt + "\n>");
        while(!(sc.hasNextDouble())){
            String garbage = sc.nextLine();
            System.err.println(garbage + " was not a floating point number.");
            System.out.print(prompt + "\n>");
        }
        double num = sc.nextDouble();
        sc.nextLine();
        return num;
    }

    /**
     * Prompt the user and read a floating point number between (inclusive) of min and max, 
     * clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user 
     * @return: a double value 
     */
    public double inputDouble(String prompt, double min, double max) {
        double num = inputDouble(prompt);
        while(num < min || num > max){
            System.out.println(num + " was not in the allowed range "+min+ "-" +max);
            num = inputDouble(prompt);
        }
        return num;
    }
    /**
     * Prompt the user and read a boolean value, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user 
     * @return: a boolean value 
     */
    public boolean inputBoolean(String prompt) {
        System.out.print(prompt + "\n>");
        while(!(sc.hasNextBoolean())){
            String garbage = sc.nextLine();
            System.err.println(garbage + " was not a boolean.");
            System.out.print(prompt + "\n>");
        }
        boolean b = sc.nextBoolean();
        sc.nextLine();
        return b;
    }

    /**
     * Prompt the user enter yes or no (will match y/yes and n/no any case) and return true for yes and false for no.
     * @param prompt: the question to ask the user 
     * @return: a boolean value 
     */
    public boolean inputYesNoAsBoolean(String prompt) {
        System.out.print(prompt + "\n>");
        String answer = sc.nextLine();
        while((answer.equalsIgnoreCase("yes") == false) &&(answer.equalsIgnoreCase("no") == false) && (answer.equalsIgnoreCase("y")==false)&& (answer.equalsIgnoreCase("n")==false)){
            System.out.println(answer + " was not a boolean (yes/y/no/n)");
            System.out.print(prompt + "\n>");
            answer = sc.nextLine();
        }
        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) return true;
        else return false;
    }

    public void print(String msg){
        System.out.println(msg);
    }
    
    public void printFancy(String msg){
        System.out.println("********************************");
        System.out.println(msg);   
        System.out.println("********************************");
    }
    
}
