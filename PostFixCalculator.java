package postfixcalc;

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Represents a PostFixCalculator.
 * @author Paul Snieder
 */
public class PostFixCalculator {
  
  private static PostFixCalculator instance;
    
  /**
   * Default constructor (hidden so others can't create an instance).
   */
  private PostFixCalculator(){}
  
  /**
   * Returns the singleton instance.
   * @return the singleton instance
   */
  public static PostFixCalculator getInstance() {
    if (instance == null) {
      instance = new PostFixCalculator();
    }
    return instance;
  }
  
  /**
   * Calculates the answer to the postfix expression and return the result as a Number.
   * @param expression a String the postfix expression.
   * @return the result of the postfix expression as a Number.
   * @throws InvalidExpressionException if the expression isn't valid postfix.
   */
  public Number calculate(String expression) throws InvalidExpressionException {
    try {
      Stack<Number> numbers = new Stack<Number>();
      String[] array = expression.split(" ");
      
      Number x; //first number on the stack
      Number y; //second number on the stack
      
      for (int i = 0; i < array.length; i++) {
        
        switch (array[i]) {
          case "+":
            x = numbers.pop();
            y = numbers.pop();
            if (checkInt(x) && checkInt(y)) {
              numbers.push((int)x + (int)y);
            } else if (!checkInt(x) && checkInt(y)) {
              numbers.push((double)x + (int)y);
            } else if (checkInt(x) && !checkInt(y)) {
              numbers.push((int)x + (double)y);
            } else {
              numbers.push((double)x + (double)y);
            }
            break;
          case "-":
            x = numbers.pop();
            y = numbers.pop();
            if (checkInt(x) && checkInt(y)) {
              numbers.push((int)y - (int)x);
            } else if (!checkInt(x) && checkInt(y)) {
              numbers.push((int)y - (double)x);
            } else if (checkInt(x) && !checkInt(y)) {
              numbers.push((double)y - (int)x);
            } else {
              numbers.push((double)y - (double)x);
            }
            break;
          case "*":
            x = numbers.pop();
            y = numbers.pop();
            if (checkInt(x) && checkInt(y)) {
              numbers.push((int)x * (int)y);
            } else if (!checkInt(x) && checkInt(y)) {
              numbers.push((double)x * (int)y);
            } else if (checkInt(x) && !checkInt(y)) {
              numbers.push((int)x * (double)y);
            } else {
              numbers.push((double)x * (double)y);
            }
            break;
          case "/":
            x = numbers.pop();
            y = numbers.pop();
            if (checkInt(x) && checkInt(y)) {
              numbers.push((int)y / (int)x);
            } else if (!checkInt(x) && checkInt(y)) {
              numbers.push((int)y / (double)x);
            } else if (checkInt(x) && !checkInt(y)) {
              numbers.push((double)y / (int)x);
            } else {
              numbers.push((double)y / (double)x);
            }
            break;
          default:
            if (array[i].indexOf(".") == -1) { //if no "." is present, we know it's an int
              numbers.push(Integer.parseInt(array[i]));
            } else { //if we find a "." in the string (decimal point), we know it's a double
              numbers.push(Double.parseDouble(array[i]));
            }
            break;
        }
      }
      
      Number ret = numbers.pop();
      
      if (!numbers.isEmpty()) {
        throw new InvalidExpressionException();
      }
      
      return ret;
      
    } catch (EmptyStackException ese) {
      throw new InvalidExpressionException();
    }

  }
  
  /**
   * Checks if the number is an int.
   * @param x the number
   * @return true if the number is an int
   */
  private boolean checkInt(Number x) {
    try {
      x = (int)x; //this will throw an exception if x is a double
      return true;
    } catch (ClassCastException cce) { //if we catch this we know it's a double
      return false;
    }
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Main to run the program.
   * @param args command line arguments
   */
  public static void main(String[] args) {
    
    PostFixCalculator calc = PostFixCalculator.getInstance();
    Scanner scan = new Scanner(System.in);
    System.out.println("Post-fix Calculator: Please enter a post-fix math problem. "
        + "Only +, -, *, and / operations are supported: ");
    String expression = scan.nextLine();
    try {
      System.out.println("The answer is " + calc.calculate(expression));
    } catch (InvalidExpressionException e) {
      e.printStackTrace();
    }
    scan.close();
  }
  
}
