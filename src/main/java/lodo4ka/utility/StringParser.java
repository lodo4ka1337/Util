package lodo4ka.utility;

import java.math.BigDecimal;
import java.math.BigInteger;

public class StringParser {
    public static BigInteger strToInteger(String str) {
        try {
            return new BigInteger(str);
        }
        catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static BigDecimal strToFloat(String str) {
        try {
            return new BigDecimal(str);
        }
        catch (NumberFormatException ignored) {
            return null;
        }
    }
}
