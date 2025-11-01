package project_03_10;

import java.util.Scanner;

public class Project_03_10 {
  
  final static Scanner scan = new Scanner(System.in);
  
  public static void main(String [ ] args) {
    
    boolean notOdd = true, notDiv3 = true;
    int numberEven = 0;
    Long min = Long.MAX_VALUE;
    
    do {
      System.out.print("Дай ми поне 3 цели числа:");
      long next = scan.nextLong( );
      
      if (next % 2 != 0) notOdd = false;
      else {
        ++numberEven;
      }
      if (next % 3 == 0) notDiv3 = false;
      if (min > next) min = next;
    } while (notOdd || notDiv3);
    
    System.out.printf("%d times even number:\n", numberEven);
    if (min < 0L) System.out.println("no negative number");
    else System.out.printf("Minimal read negative is:\n, %d", min);
    
  }
  
}
