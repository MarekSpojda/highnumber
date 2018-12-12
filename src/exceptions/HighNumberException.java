package exceptions;

/**
 * Default exception class for highnumber.HighNumber.
 *
 * @author Marek Spojda, marek.spojda@o2.pl
 */

public class HighNumberException extends Exception {
    /**
     * <b>HighNumberException</b><br>
     * <br>
     * <i>public HighNumberException(String message)</i><br>
     * <br>
     * Creates HighNumberException message.
     *
     * @param message message describing error.
     * @author Marek Spojda, marek.spojda@o2.pl
     */
    public HighNumberException(String message) {
        super(message);
    }
}