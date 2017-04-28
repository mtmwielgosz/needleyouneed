package blankets;

import java.math.BigDecimal;
import java.math.RoundingMode;

class Money {

    public static final int MONEY_PRECISSION = 2;
    private BigDecimal value;

    public Money(BigDecimal value) {
        this.value = value.setScale(MONEY_PRECISSION, RoundingMode.HALF_UP);
    }

    public BigDecimal getValue() {
        return value;
    }
}
