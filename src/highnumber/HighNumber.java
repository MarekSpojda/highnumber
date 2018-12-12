package highnumber;

import java.util.ArrayList;

import exceptions.HighNumberException;

/**
 * Class for mathematical operations. Number arguments in this class are mostly
 * String type, like "12", "-0.123" or "19283764.12963897163".<br>
 * <b>Important</b> - decimal separator for this class is . (dot). Highest
 * precision of calculations is returned by getPrecision(), can be changed by
 * setPrecision(). It is STRONGLY recommended to check correct format of input
 * numbers before computing by <b>isNumber(String)</b> method.
 *
 * @author Marek Spojda, marek.spojda@o2.pl
 */
public class HighNumber {
    @SuppressWarnings("WeakerAccess")
    public static final String AUTHOR = "highnumber.HighNumber author: Marek Spojda, mail: marek.spojda@o2.pl, 12th Dec, 2018";
    @SuppressWarnings("unused")
    public static final String HIGHPI = "3.141592653589793238462643383279502884197169399375105820974944592307"
            + "8164062862089986280348253421170679821480865132823066470938446095505"
            + "8223172535940812848111745028410270193852110555964462294895493038196"
            + "44288109756659334461284756482337867831652712019091";
    private int PRECISION = 0;
    private String DELTA = "0";

    /**
     * <b>highnumber.HighNumber</b><br>
     * <br>
     * <i>public highnumber.HighNumber(int precision)</i><br>
     * <br>
     * Creates highnumber.HighNumber object.
     *
     * @param precision initial precision, can be changed by setPrecision(int) method.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public HighNumber(int precision) throws HighNumberException {
        setPrecision(precision);
    }

    /**
     * <b>absolute</b><br>
     * <br>
     * <i>public String absolute(String input)</i><br>
     * <br>
     * Returns absolute version of input number. Correct format of input number
     * should be checked before by <b>isNumber(String)</b> method.
     *
     * @param input number to be processed.
     * @return absolute version of input number.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String absolute(String input) {
        ArrayList<Character> list = toCharArrayList(input);
        // Defining first character of input.
        char x = list.get(0);
        // Removing minus if necessary.
        if (x == '-')
            list.remove(0);
        // Building output number.
        return buildStringAll(list);
    }

    /**
     * <b>add</b><br>
     * <br>
     * <i>public String add(String input1, String input2)</i><br>
     * <br>
     * Adds two numbers. Result is affected by precision which can be returned by
     * getPrecision() method. Correct format of input numbers should be checked
     * before by <b>isNumber(String)</b> method.
     *
     * @param input1 first number to be added.
     * @param input2 second number to be added.
     * @return sum of both numbers - input1 + input2.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String add(String input1, String input2) throws HighNumberException {
        // Checking signs of input numbers.
        boolean sign1 = true;
        if (isNegative(input1))
            sign1 = false;
        boolean sign2 = true;
        if (isNegative(input2))
            sign2 = false;

        // Checking original precision of numbers.
        int originalPrec1 = numPrecision(input1);
        int originalPrec2 = numPrecision(input2);

        // Checking is numbers are decimal numbers, if no adding '.0'.
        boolean isDec1 = isDecimal(input1);
        boolean isDec2 = isDecimal(input2);
        if (!isDec1)
            input1 = input1 + ".0";
        if (!isDec2)
            input2 = input2 + ".0";

        // Checking numbers precision again.
        int prec1 = numPrecision(input1);
        int prec2 = numPrecision(input2);
        int biggerPrec = prec1;
        if (prec2 > prec1)
            biggerPrec = prec2;

        // Making numbers even and absolute.
        input1 = absolute(input1);
        input2 = absolute(input2);
        String[] evenTab = evenBeforeAfter(input1, input2);
        input1 = evenTab[0];
        input2 = evenTab[1];

        // Turning numbers into ArrayList<Character> lists and removing dots.
        ArrayList<Character> list1 = toCharArrayList(input1);
        ArrayList<Character> list2 = toCharArrayList(input2);
        int dotPos = list1.indexOf('.');
        list1.remove(dotPos);
        list2.remove(dotPos);

        // Making numbers int[] tables.
        int[] num1 = stringToInt(buildStringAll(list1));
        int[] num2 = stringToInt(buildStringAll(list2));
        int tabLength = num1.length;
        if (num2.length > num1.length)
            tabLength = num2.length;
        int[] resTab = new int[tabLength + 1];

        StringBuilder result = new StringBuilder();

        // Adding numbers depending on signs.
        if ((sign1 && sign2) || (!sign1 && !sign2)) {
            // Both signs are the same.
            for (int i = (num1.length - 1); i > (-1); i--) {
                resTab[i + 1] = resTab[i + 1] + num1[i] + num2[i];
                if (resTab[i + 1] >= 10) {
                    resTab[i + 1] = resTab[i + 1] - 10;
                    resTab[i]++;
                }
            }
        } else //noinspection ConstantConditions
            if (sign1 && !sign2) {
                // sign1 is positive and sign2 is negative.
                byte howBig = compare(input1, input2);
                if (howBig == 0) {
                    return "0";
                } else if (howBig == (-1)) {
                    //noinspection Duplicates
                    for (int i = (num1.length - 1); i > (-1); i--) {
                        resTab[i + 1] = resTab[i + 1] + num1[i] - num2[i];
                        if (resTab[i + 1] < 0) {
                            resTab[i + 1] = resTab[i + 1] + 10;
                            num1[i - 1]--;
                        }
                    }
                } else if (howBig == 1) {
                    //noinspection Duplicates
                    for (int i = (num1.length - 1); i > (-1); i--) {
                        resTab[i + 1] = resTab[i + 1] + num2[i] - num1[i];
                        if (resTab[i + 1] < 0) {
                            resTab[i + 1] = resTab[i + 1] + 10;
                            num2[i - 1]--;
                        }
                    }

                    result.append("-");
                }
            } else //noinspection ConstantConditions
                if (!sign1 && sign2) {
                    // sign1 is negative and sign2 is positive.
                    byte howBig = compare(input1, input2);
                    if (howBig == 0) {
                        return "0";
                    } else if (howBig == (-1)) {
                        //noinspection Duplicates
                        for (int i = (num1.length - 1); i > (-1); i--) {
                            resTab[i + 1] = resTab[i + 1] + num1[i] - num2[i];
                            if (resTab[i + 1] < 0) {
                                resTab[i + 1] = resTab[i + 1] + 10;
                                num1[i - 1]--;
                            }
                        }
                        result.append("-");
                    } else if (howBig == 1) {
                        //noinspection Duplicates
                        for (int i = (num1.length - 1); i > (-1); i--) {
                            resTab[i + 1] = resTab[i + 1] + num2[i] - num1[i];
                            if (resTab[i + 1] < 0) {
                                resTab[i + 1] = resTab[i + 1] + 10;
                                num2[i - 1]--;
                            }
                        }
                    }
                }

        // Building output number.
        for (int i = 0; i < (resTab.length - biggerPrec); i++) {
            result.append(resTab[i]);
        }
        result.append(".");
        for (int i = (resTab.length - biggerPrec); i < resTab.length; i++)
            result.append(resTab[i]);

        if (!sign1 && !sign2)
            result.insert(0, "-");

        // If both numbers are natural then result will be natural as well.
        if (!isDec1 && !isDec2)
            result = new StringBuilder(round(result.toString(), 0));
        if (compare(absolute(result.toString()), "0") == 0)
            return "0";

        // Making sure result look right.
        return round(removeZero(result.toString()), originalPrec1 + originalPrec2);
    }

    /**
     * <b>beforeAfter</b><br>
     * <br>
     * <i>public int[] beforeAfter(String input)</i><br>
     * <br>
     * Tells how many digits are before and after decimal separator. It does not
     * include minus. If number is not decimal number there would be 0 as result
     * after decimal separator. Correct format of input number should be checked
     * before by <b>isNumber(String)</b> method.
     *
     * @param input number to be processed.
     * @return [x, y] int table where <b>x</b> is number of digits before decimal
     * separator and <b>y</b> is number of digits after decimal separator.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public int[] beforeAfter(String input) {
        int[] result = new int[2];
        // Removing minus from input if necessary.
        input = absolute(input);
        // Defining result table.
        if (isDecimal(input)) {
            result[0] = input.indexOf('.');
            result[1] = input.length() - input.indexOf('.') - 1;
        } else {
            result[0] = input.length();
        }

        return result;
    }

    /**
     * <b>buildString</b><br>
     * <br>
     * <i>public String buildString(ArrayList&#60;Character&#62; list, int start,
     * int end) throws HighNumberException</i><br>
     * <br>
     * Builds String from ArrayList&#60;Character&#62; list starting and ending at
     * specified positions. Illegal parameters result in HighNumberException.
     *
     * @param list  ArrayList&#60;Character&#62; list from which characters are taken
     *              to build String.
     * @param start starting index of ArrayList&#60;Character&#62; list for String
     *              building.
     * @param end   end index of ArrayList&#60;Character&#62; list for String
     *              building.
     * @return String builded from specified range of ArrayList&#60;Character&#62;
     * list elements or HighNumberException in case of incorrect parameters.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("unused")
    public String buildString(ArrayList<Character> list, int start, int end) throws HighNumberException {
        if (start < 0 || end < 0)
            throw new HighNumberException(
                    "Incorrect parameters detected while building String from ArrayList<Character> list - start and/or end parameter is/are negative: "
                            + start + ", " + end);
        else if (end < start)
            throw new HighNumberException(
                    "Incorrect parameters detected while building String from ArrayList<Character> list - end parameter is smaller than start parameter: "
                            + start + ", " + end);
        if (start >= list.size() || end >= list.size())
            throw new HighNumberException(
                    "Incorrect parameters detected while building String from ArrayList<Character> list - start and/or end parameter is/are beyond maximal index of ArrayList<Character> list.");
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < (end + 1); i++) {
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * <b>buildStringAll</b><br>
     * <br>
     * <i>public String buildStringAll(ArrayList&#60;Character&#62; list)</i><br>
     * <br>
     * Builds String from ArrayList&#60;Character&#62; list.
     *
     * @param list ArrayList&#60;Character&#62; list from which characters are taken
     *             to build String.
     * @return String builded from ArrayList&#60;Character&#62; list elements.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String buildStringAll(ArrayList<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (Character aList : list) {
            sb.append(aList);
        }
        return sb.toString();
    }

    /**
     * <b>compare</b><br>
     * <br>
     * <i>public byte compare(String compareA, String compareB)</i><br>
     * <br>
     * Specifies if number compareB is greater, equal or smaller than number
     * compareA. Correct format of input numbers should be checked before by
     * <b>isNumber(String)</b> method.
     *
     * @param compareA first number to be checked.
     * @param compareB second number to be checked.
     * @return <b>1</b> if number compareB is bigger than number compareA, <b>0</b>
     * if numbers are equal or <b>-1</b> if number compareB is smaller than
     * number compareA.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public byte compare(String compareA, String compareB) throws HighNumberException {
        // Remembering signs of numbers and turning them into absolute values.
        boolean sign1 = true;
        if (isNegative(compareA))
            sign1 = false;
        boolean sign2 = true;
        if (isNegative(compareB))
            sign2 = false;

        // Comparing numbers with different signs since it can be done now.
        if (sign1 && !sign2) {
            return -1;
        } else if (!sign1 && sign2) {
            return 1;
        }

        // Making numbers absolute.
        compareA = absolute(compareA);
        compareB = absolute(compareB);

        // Making sure both numbers are decimal numbers.
        if (!isDecimal(compareA))
            compareA = compareA + ".0";
        if (!isDecimal(compareB))
            compareB = compareB + ".0";

        // Making equal amount of digits before and after decimal separator in both
        // numbers.

        String[] comparing = evenBeforeAfter(compareA, compareB);
        compareA = comparing[0];
        compareB = comparing[1];

        // Removing dot from numbers.
        ArrayList<Character> charList = toCharArrayList(compareA);
        //noinspection RedundantCollectionOperation
        charList.remove(charList.indexOf('.'));
        compareA = buildStringAll(charList);

        charList.clear();
        charList = toCharArrayList(compareB);
        //noinspection RedundantCollectionOperation
        charList.remove(charList.indexOf('.'));
        compareB = buildStringAll(charList);

        // Comparing numbers with different signs.
        int[] numA = stringToInt(compareA);
        int[] numB = stringToInt(compareB);
        //noinspection ConstantConditions
        if (sign1 && sign2) {
            for (int i = 0; i < compareA.length(); i++) {
                if (numA[i] < numB[i])
                    return 1;
                else if (numA[i] > numB[i])
                    return -1;
            }
            return 0;
        } else //noinspection ConstantConditions
            if (!sign1 && !sign2) {
                for (int i = 0; i < compareA.length(); i++) {
                    if (numA[i] < numB[i])
                        return -1;
                    else if (numA[i] > numB[i])
                        return 1;
                }
                return 0;
            }
        return 0;
    }

    /**
     * <b>divide</b><br>
     * <br>
     * <i>public String divide(String input1, String input2)</i><br>
     * <br>
     * Divides two numbers. Result is affected by precision which can be returned by
     * getPrecision() method. Correct format of input numbers should be checked
     * before by <b>isNumber(String)</b> method.
     *
     * @param input1 first number to be processed.
     * @param input2 second number to be processed.
     * @return first number divided by second number - input1 / input2.
     * @throws HighNumberException default exception for highnumber.HighNumber
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String divide(String input1, String input2) throws HighNumberException {
        // Checking input signs.
        boolean sign1 = true;
        boolean sign2 = true;
        if (isNegative(input1))
            sign1 = false;
        if (isNegative(input2))
            sign2 = false;

        // Making sure numbers are positive from now.
        input1 = absolute(input1);
        input2 = absolute(input2);

        // Making sure numbers are decimal.
        if (!isDecimal(input1))
            input1 = input1 + ".0";
        if (!isDecimal(input2))
            input2 = input2 + ".0";

        // Checking numbers precision.
        int precA = numPrecision(input1);
        int precB = numPrecision(input2);

        // Removing decimal separator from numbers.
        ArrayList<Character> listA = toCharArrayList(input1);
        ArrayList<Character> listB = toCharArrayList(input2);
        //noinspection RedundantCollectionOperation
        listA.remove(listA.indexOf('.'));
        //noinspection RedundantCollectionOperation
        listB.remove(listB.indexOf('.'));
        input1 = buildStringAll(listA);
        input2 = buildStringAll(listB);

        // Correcting ends of numbers.
        int diff = precA - precB;
        //noinspection Duplicates
        if (diff > 0) {
            StringBuilder input2Builder = new StringBuilder(input2);
            for (int i = 0; i < diff; i++)
                input2Builder.append("0");
            input2 = input2Builder.toString();
        } else if (diff < 0) {
            diff = precB - precA;
            StringBuilder input1Builder = new StringBuilder(input1);
            for (int i = 0; i < diff; i++)
                input1Builder.append("0");
            input1 = input1Builder.toString();
        }

        // Dividing based on number sizes.
        byte compare = compare(input1, input2);
        if (compare == 0) {
            if (sign1 != sign2)
                return "-1";
            return "1";
        } else if (compare == 1) {
            // Number 2 is bigger.
            // TODO divide - number 2 is bigger, remember about signs.
        } else {
            // Number 1 is bigger.
            // TODO divide - number 1 is bigger, remember about signs.
        }

        // TODO divide - finished here.
        return "Still dividing in 2018, input1: " + input1 + ", input2: " + input2;
    }

    /**
     * <b>evenBeforeAfter</b><br>
     * <br>
     * <i>public String[] evenBeforeAfter(String numA, String numB)</i><br>
     * <br>
     * Makes even amount of digits before and after decimal separator in both
     * numbers by adding zeros before first and last number. If number is not
     * decimal number a '.0' will be added to number before processing. Negative
     * numbers are turned into absolute version before processing as well. Correct
     * format of input numbers should be checked before by <b>isNumber(String)</b>
     * method.
     *
     * @param numA first number to be processed.
     * @param numB second number to be processed.
     * @return numbers with the same amount of digits before and after decimal
     * separator in String table form ([numA,numB]).
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String[] evenBeforeAfter(String numA, String numB) {
        // Making absolute version of numbers.
        numA = absolute(numA);
        numB = absolute(numB);

        // Making sure numbers are decimal.
        if (!isDecimal(numA)) {
            numA = numA + ".0";
        }
        if (!isDecimal(numB)) {
            numB = numB + ".0";
        }

        int descA[] = beforeAfter(numA);
        int descB[] = beforeAfter(numB);

        if (descB[0] > descA[0]) {
            int j = descB[0] - descA[0];
            StringBuilder before = new StringBuilder();
            for (int i = 0; i < j; i++) before.append("0");
            numA = before + numA;
        } else if (descB[0] < descA[0]) {
            int j = descA[0] - descB[0];
            StringBuilder before = new StringBuilder();
            for (int i = 0; i < j; i++) {
                before.append("0");
            }
            numB = before + numB;
        }

        if (descB[1] > descA[1]) {
            int j = descB[1] - descA[1];
            StringBuilder after = new StringBuilder();
            for (int i = 0; i < j; i++) {
                after.append("0");
            }
            numA = numA + after;
        } else if (descB[1] < descA[1]) {
            int j = descA[1] - descB[1];
            StringBuilder after = new StringBuilder();
            for (int i = 0; i < j; i++) {
                after.append("0");
            }
            numB = numB + after;
        }

        String[] result = new String[2];
        result[0] = numA;
        result[1] = numB;
        return result;
    }

    /**
     * <b>getDelta</b><br>
     * <br>
     * <i>public String getDelta()</i><br>
     * <br>
     * Returns delta that is set in highnumber.HighNumber object from which this method is
     * called. Delta is smallest difference between numbers that is used to process
     * numbers.
     *
     * @return delta that is set in highnumber.HighNumber object from which this method is
     * called.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("unused")
    public String getDelta() {
        return this.DELTA;
    }

    /**
     * <b>getPrecision</b><br>
     * <br>
     * <i>public int getPrecision()</i><br>
     * <br>
     * Returns precision that is set in highnumber.HighNumber object from which this method is
     * called.
     *
     * @return precision that is set in highnumber.HighNumber object from which this method is
     * called.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public int getPrecision() {
        return this.PRECISION;
    }

    /**
     * <b>howManyBA</b><br>
     * <br>
     * <i>public String howManyBA(String numA, String numB)</i><br>
     * <br>
     * Calculates how many times whole number B can be placed in number A. Numbers
     * are treated as positive numbers and result is natural number. Correct format
     * of input numbers should be checked before by <b>isNumber(String)</b> method.
     *
     * @param numA first number to be processed.
     * @param numB second number to be processed.
     * @return how many times whole number B can be placed in number A.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("unused")
    public String howManyBA(String numA, String numB) throws HighNumberException {
        // Making sure numbers are positive.
        numA = absolute(numA);
        numB = absolute(numB);

        // Making sure numbers are decimal.
        if (!isDecimal(numA))
            numA = numA + ".0";
        if (!isDecimal(numB))
            numB = numB + ".0";

        // Checking numbers precision.
        int precA = numPrecision(numA);
        int precB = numPrecision(numB);

        // Removing decimal separator from numbers.
        ArrayList<Character> listA = toCharArrayList(numA);
        ArrayList<Character> listB = toCharArrayList(numB);
        //noinspection RedundantCollectionOperation
        listA.remove(listA.indexOf('.'));
        //noinspection RedundantCollectionOperation
        listB.remove(listB.indexOf('.'));
        numA = buildStringAll(listA);
        numB = buildStringAll(listB);

        // Correcting ends of numbers.
        int diff = precA - precB;
        //noinspection Duplicates
        if (diff > 0) {
            StringBuilder numBBuilder = new StringBuilder(numB);
            for (int i = 0; i < diff; i++)
                numBBuilder.append("0");
            numB = numBBuilder.toString();
        } else if (diff < 0) {
            diff = precB - precA;
            StringBuilder numABuilder = new StringBuilder(numA);
            for (int i = 0; i < diff; i++)
                numABuilder.append("0");
            numA = numABuilder.toString();
        }

        // Checking which number is bigger.
        byte howBig = compare(numA, numB);

        // Calculating result based on howBig variable.
        StringBuilder result = new StringBuilder("1");
        StringBuilder tempResult;
        int multiplier = numA.length();

        if (howBig == 0)
            return "1";
        else if (howBig == 1) {
            // numB is bigger.
            return "0";
        } else {
            // numA is bigger
            boolean start = true;
            for (int i = 0; i < multiplier; i++)
                result.append("0");
            byte safeSwitch = 0;
            while (start) {
                if (safeSwitch != 0) {
                    if (compare(multiply(numB, result.toString()), numA) == (-1))
                        result = new StringBuilder(subtract(result.toString(), "1"));
                    return result.toString();
                }
                byte tempCompare = compare(multiply(numB, result.toString()), numA);

                if (tempCompare == (-1)) {
                    multiplier--;
                    tempResult = new StringBuilder("1");
                    for (int i = 0; i < multiplier; i++)
                        tempResult.append("0");

                    for (int i = 0; i < 9; i++) {
                        String veryTemp = subtract(result.toString(), tempResult.toString());

                        tempCompare = compare(multiply(numB, veryTemp), numA);
                        if (tempCompare == (-1)) {
                            result = new StringBuilder(veryTemp);
                        } else if (tempCompare == 0)
                            return veryTemp;
                        else {
                            if (tempResult.toString().equals("1"))
                                safeSwitch++;
                            break;
                        }
                    }
                } else {
                    start = false;
                }
            }
        }

        return result.toString();
    }

    /**
     * <b>isDecimal</b><br>
     * <br>
     * <i>public boolean isDecimal(String input)</i><br>
     * <br>
     * Specifies if number is decimal number. Correct format of input number should
     * be checked before by <b>isNumber(String)</b> method.
     *
     * @param input number to be checked.
     * @return <b>true</b> if number is decimal number or <b>false</b> if it is not.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isDecimal(String input) {
        byte dotCount = 0;
        for (char x : input.toCharArray()) {
            if (x == '.') {
                dotCount++;
                if (dotCount > 1)
                    return false;
            }
        }
        return dotCount != 0;
    }

    /**
     * <b>isNegative</b><br>
     * <br>
     * <i>public boolean isNegative(String input)</i><br>
     * <br>
     * Specifies if number is negative number. Correct format of input number should
     * be checked before by <b>isNumber(String)</b> method.
     *
     * @param input number to be checked.
     * @return <b>true</b> if number is negative number or <b>false</b> if it is
     * not.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isNegative(String input) {
        return toCharArrayList(input).get(0) == '-';
    }

    /**
     * <b>isNumber</b><br>
     * <br>
     * <i>public boolean isNumber(String input)</i><br>
     * <br>
     * Specifies if number has correct format.
     *
     * @param input number to be checked.
     * @return <b>true</b> if number has correct format or <b>false</b> if it
     * hasn't.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("unused")
    public boolean isNumber(String input) {
        if (input.equals(""))
            return false;
        boolean isNumber = false;
        ArrayList<Character> inArray = toCharArrayList(input);
        byte dotCount = 0;
        byte minusCount = 0;
        for (char x : input.toCharArray()) {
            if (x == '.' || x == '-' || x == '0' || x == '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6'
                    || x == '7' || x == '8' || x == '9')
                isNumber = true;
            else {
                isNumber = false;
                break;
            }
            if (x == '.') {
                dotCount++;
                if (dotCount > 1)
                    return false;
            }
            if (x == '-') {
                minusCount++;
                if (minusCount > 1)
                    return false;
            }
        }
        if (inArray.get(0) != '-' && minusCount == 1)
            return false;
        if (dotCount == 1) {
            if (inArray.get(0) == '.' || inArray.get(inArray.size() - 1) == '.')
                return false;
            if (inArray.indexOf('.') > 0 && inArray.get(inArray.indexOf('.') - 1) == '-')
                return false;
        }
        return isNumber;
    }

    /**
     * <b>multiply</b><br>
     * <br>
     * <i>public String multiply(String input1, String input2)</i><br>
     * <br>
     * Multiplies two numbers. Result is affected by precision which can be returned
     * by getPrecision() method. Correct format of input numbers should be checked
     * before by <b>isNumber(String)</b> method.
     *
     * @param input1 first number to be processed.
     * @param input2 second number to be processed.
     * @return first number multiplied by second number - input1 * input2.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String multiply(String input1, String input2) throws HighNumberException {
        // Checking numbers signs.
        boolean sign1 = true;
        boolean sign2 = true;
        if (isNegative(input1))
            sign1 = false;
        if (isNegative(input2))
            sign2 = false;

        // Making numbers absolute.
        input1 = absolute(input1);
        input2 = absolute(input2);

        // Checking if numbers are decimal.
        boolean isDec1 = isDecimal(input1);
        boolean isDec2 = isDecimal(input2);

        // Checking original precision of numbers.
        int originalPrec1 = numPrecision(input1);
        int originalPrec2 = numPrecision(input2);

        // Making sure numbers are decimal.
        if (!isDec1)
            input1 = input1 + ".0";
        if (!isDec2)
            input2 = input2 + ".0";

        // Checking numbers precision again.
        int prec1 = numPrecision(input1);
        int prec2 = numPrecision(input2);
        int biggerPrec = prec1;
        if (prec2 > prec1)
            biggerPrec = prec2;

        // Giving numbers the same size.
        String[] even = evenBeforeAfter(input1, input2);
        input1 = even[0];
        input2 = even[1];

        // Turning numbers into ArrayList<Character> lists and removing decimal
        // separator from them.
        ArrayList<Character> list1 = toCharArrayList(input1);
        ArrayList<Character> list2 = toCharArrayList(input2);
        //noinspection RedundantCollectionOperation
        list1.remove(list1.indexOf('.'));
        //noinspection RedundantCollectionOperation
        list2.remove(list2.indexOf('.'));
        input1 = buildStringAll(list1);
        input2 = buildStringAll(list2);

        // Creating int[] tables.
        int[] intTab1 = stringToInt(input1);
        int[] intTab2 = stringToInt(input2);
        int[] resTab = new int[input1.length() + input2.length() + 1];

        // Multiplying numbers.
        for (int i = (input2.length() - 1); i > (-1); i--) {
            for (int j = (input1.length() - 1); j > (-1); j--) {
                resTab[i + j + 2] = resTab[i + j + 2] + intTab2[i] * intTab1[j];
            }
        }

        // Correcting numbers greater than 9.
        for (int i = (resTab.length - 1); i > 0; i--) {
            if (resTab[i] > 9) {
                int backward = resTab[i] % 10;
                resTab[i - 1] = resTab[i - 1] + (resTab[i] - backward) / 10;
                resTab[i] = backward;
            }
        }

        // Initializing result;
        StringBuilder result = new StringBuilder();

        // Constructing result.
        for (int i = 0; i < (resTab.length - (2 * biggerPrec)); i++)
            result.append(resTab[i]);
        result.append(".");
        for (int i = (resTab.length - (2 * biggerPrec)); i < resTab.length; i++)
            result.append(resTab[i]);

        // If both numbers are natural then result will be natural as well.
        if (!isDec1 && !isDec2)
            result = new StringBuilder(round(result.toString(), 0));
        if (compare(absolute(result.toString()), "0") == 0) {
            return "0";
        }

        if (sign1 != sign2)
            result.insert(0, "-");

        // Making sure result look right.
        return round(removeZero(result.toString()), originalPrec1 + originalPrec2);
    }

    /**
     * <b>numPrecision</b><br>
     * <br>
     * <i>public int numPrecision(String input)</i><br>
     * <br>
     * Tells how many numbers are after decimal separator or returns 0 if there is
     * no decimal separator. Correct format of input number should be checked before
     * by <b>isNumber(String)</b> method.
     *
     * @param input number to be checked.
     * @return number precision.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public int numPrecision(String input) {
        if (isDecimal(input))
            return input.length() - input.indexOf('.') - 1;
        return 0;
    }

    /**
     * <b>removeZero</b><br>
     * <br>
     * <i>public String removeZero(String input)</i><br>
     * <br>
     * Removes unnecessary zeros from beginning of number. Correct format of input
     * number should be checked before by <b>isNumber(String)</b> method.
     *
     * @param input number to be checked.
     * @return number without unnecessary zeros at the beginning.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String removeZero(String input) {
        boolean sign = true;
        if (isNegative(input)) {
            sign = false;
            input = absolute(input);
        }

        // Checking if situation like -08 or 02.4 exist.
        ArrayList<Character> resultChar = toCharArrayList(input);
        while ((resultChar.get(0) == '0' && resultChar.get(1) == '0')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '1')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '2')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '3')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '4')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '5')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '6')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '7')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '8')
                || (resultChar.get(0) == '0' && resultChar.get(1) == '9'))
            resultChar.remove(0);

        // Building result from ArrayList<Character> list.
        String result = buildStringAll(resultChar);
        if (!sign)
            result = "-" + result;

        return result;
    }

    /**
     * <b>round</b><br>
     * <br>
     * <i>public String round(String input, int innerPrec) throws
     * HighNumberException</i><br>
     * <br>
     * Rounds number with given precision. If given precision is greater than
     * precision returned by getPrecision(), number is rounded to precision returned
     * by getPrecision(), also if number is not decimal then not-decimal form of
     * number is returned. Correct format of input number should be checked before
     * by <b>isNumber(String)</b> method.
     *
     * @param input     number to be rounded.
     * @param innerPrec precision to be used.
     * @return rounded number.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String round(String input, int innerPrec) throws HighNumberException {
        // Checking if precision is greater or equal 0.
        if (innerPrec < 0)
            throw new HighNumberException("Negative precision detected while rounding: " + innerPrec);

        // Checking if number is not decimal, if not then number is returned
        // immediately.
        if (!isDecimal(input))
            return input;

        // Creating String for result.
        StringBuilder result = new StringBuilder();

        // Making sure inner precision is not greater than precision returned by
        // getPrecision().
        if (innerPrec > getPrecision())
            innerPrec = getPrecision();

        if (numPrecision(input) <= innerPrec)
            return input;

        // Checking number's sign
        boolean sign = true;
        if (isNegative(input)) {
            sign = false;
            input = absolute(input);
        }

        ArrayList<Character> tab = toCharArrayList(input);
        int dotPos = tab.indexOf('.');
        tab.remove(dotPos);

        // Turning character table into int table.
        int[] tabInt = stringToInt(buildStringAll(tab));
        //noinspection StatementWithEmptyBody
        if (tabInt[dotPos + innerPrec] < 5) {
            // Nothing should be done here.
        } else if (tabInt[dotPos + innerPrec] > 5) {
            // Correcting values greater than 9 in tabInt[] tab list.
            tabInt[dotPos + innerPrec - 1]++;
            for (int i = (dotPos + innerPrec - 1); (i > 0); i--)
                if (tabInt[i] == 10) {
                    tabInt[i] = 0;
                    tabInt[i - 1] = tabInt[i - 1] + 1;
                }
        } else if (tabInt[dotPos + innerPrec] == 5) {
            boolean isZero = true;
            for (int i = (dotPos + innerPrec + 1); i < tabInt.length; i++) {
                if (tabInt[i] != 0) {
                    isZero = false;
                    break;
                }
            }

            if (isZero) {
                // When only zeros are after 5.
                if ((tabInt[dotPos + innerPrec - 1] == 1) || (tabInt[dotPos + innerPrec - 1] == 3)
                        || (tabInt[dotPos + innerPrec - 1] == 5) || (tabInt[dotPos + innerPrec - 1] == 7)
                        || (tabInt[dotPos + innerPrec - 1] == 9)) {
                    tabInt[dotPos + innerPrec - 1]++;

                    for (int i = (dotPos + innerPrec - 1); (i > 0); i--)
                        if (tabInt[i] == 10) {
                            tabInt[i] = 0;
                            tabInt[i - 1] = tabInt[i - 1] + 1;
                        }
                }
            } else {
                tabInt[dotPos + innerPrec - 1]++;
                for (int i = (dotPos + innerPrec - 1); (i > 0); i--) {
                    if (tabInt[i] == 10) {
                        tabInt[i] = 0;
                        tabInt[i - 1] = tabInt[i - 1] + 1;
                    }
                }
            }
        }

        // Building output number.
        for (int i = 0; i < dotPos; i++)
            result.append(tabInt[i]);

        if (innerPrec != 0)
            result.append(".");
        for (int i = dotPos; i < (dotPos + innerPrec); i++)
            result.append(tabInt[i]);
        if (!sign)
            result.insert(0, "-");
        if (compare(absolute(result.toString()), "0") == 0)
            return "0";

        return result.toString();
    }

    /**
     * <b>setPrecision</b><br>
     * <br>
     * <i>public void setPrecision(int input)</i><br>
     * <br>
     * Sets precision in highnumber.HighNumber object from which this method is called or
     * throws HighNumberException in case when precision is negative. Precision that
     * was set this way defines computing precision and maximum output precision.
     *
     * @param input precision to be set.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public void setPrecision(int input) throws HighNumberException {
        if (input >= 0)
            this.PRECISION = input;
        else
            throw new HighNumberException("Negative precision detected while setting precision: " + input);

        StringBuilder result = new StringBuilder("0.");
        if (input == 0)
            this.DELTA = "0";
        else {
            for (int i = 0; i < (input - 1); i++)
                result.append("0");
            result.append("1");
            this.DELTA = result.toString();
        }
    }

    /**
     * <b>stringToInt</b><br>
     * <br>
     * <i>public int[] stringToInt(String input)</i><br>
     * <br>
     * Creates int[] table from input number. Input number must be natural number.
     *
     * @param input number to be processed.
     * @return int[] table from input number which is natural number.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public int[] stringToInt(String input) throws HighNumberException {
        int[] result = new int[input.length()];
        ArrayList<Character> inputList = toCharArrayList(input);
        for (int i = 0; i < input.length(); i++) {
            if (inputList.get(i) == '0')
                result[i] = 0;
            else if (inputList.get(i) == '1')
                result[i] = 1;
            else if (inputList.get(i) == '2')
                result[i] = 2;
            else if (inputList.get(i) == '3')
                result[i] = 3;
            else if (inputList.get(i) == '4')
                result[i] = 4;
            else if (inputList.get(i) == '5')
                result[i] = 5;
            else if (inputList.get(i) == '6')
                result[i] = 6;
            else if (inputList.get(i) == '7')
                result[i] = 7;
            else if (inputList.get(i) == '8')
                result[i] = 8;
            else if (inputList.get(i) == '9')
                result[i] = 9;
            else
                throw new HighNumberException("Non-digit character detected while building int[] table: " + input);
        }
        return result;
    }

    /**
     * <b>subtract</b><br>
     * <br>
     * <i>public String subtract(String input1, String input2)</i><br>
     * <br>
     * Calculates the difference between two numbers. Result is affected by
     * precision which can be returned by getPrecision() method. Correct format of
     * input numbers should be checked before by <b>isNumber(String)</b> method.
     *
     * @param input1 first number to be processed.
     * @param input2 second number to be processed.
     * @return difference between numbers - input1 - input2.
     * @throws HighNumberException default exception for highnumber.HighNumber.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public String subtract(String input1, String input2) throws HighNumberException {
        // Checking sign of input2.
        if (isNegative(input2))
            input2 = absolute(input2);
        else
            input2 = "-" + input2;
        return add(input1, input2);
    }

    /**
     * <b>toCharArrayList</b><br>
     * <br>
     * <i>public ArrayList&#60;Character&#62; toCharArrayList(String input)</i><br>
     * <br>
     * Converts number to ArrayList&#60;Character&#62;.
     *
     * @param input number to be converted.
     * @return ArrayList&#60;Character&#62;.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    @SuppressWarnings("WeakerAccess")
    public ArrayList<Character> toCharArrayList(String input) {
        ArrayList<Character> charTable = new ArrayList<>();
        for (char x : input.toCharArray())
            charTable.add(x);
        return charTable;
    }
}