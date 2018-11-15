package receipt;

import java.math.BigDecimal;
import java.util.EnumSet;

/**
 * A representation of all the possible taxes to apply to a good.
 */
public enum Tax {
    BASIC_SALES("0.1"),
    IMPORT_DUTY("0.05");

    /** Rate of the tax */
    private BigDecimal rate;

    Tax(String rate) {
        this.rate = new BigDecimal(rate);
    }

    public BigDecimal getRate() {
        return rate;
    }

}
