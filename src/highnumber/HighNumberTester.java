package highnumber;

import exceptions.HighNumberException;

import java.util.Arrays;

public class HighNumberTester {
    public static void main(String[] args) {
        try {
            HighNumber hn = new HighNumber(6);
            System.out.println(HighNumber.AUTHOR);
            System.out.println(hn.divide("-3.71200", "3.712"));
            String[] twoNums = hn.evenBeforeAfter("12.123214545", "21876371623.1");
            System.out.println(Arrays.toString(twoNums));
        } catch (HighNumberException e) {
            System.out.println(e.getMessage());
        }
    }
}