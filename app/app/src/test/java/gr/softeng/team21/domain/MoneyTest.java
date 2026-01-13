package gr.softeng.team21.domain;

import org.junit.jupiter.api.Assertions;
import java.math.BigDecimal;
import static org.junit.Assert.*;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link Money} class.
 * Verifies arithmetic operations (addition) and equality logic for monetary values.
 *
 * @author PAVLOS GRATSANIS
 */
public class MoneyTest {
    private Money m1, m2, m3, m4;

    /**
     * Initializes Money instances with specific amounts and currencies
     */
    @org.junit.Before
    public void setUp() throws Exception {
        m1 = new Money(10, "$");
        m2 = new Money(20, "$");
        m3 = new Money(40, "€");
        m4 = new Money(new BigDecimal("50.56"), "€");
    }

    /**
     * Tests the add method when currencies match.
     * Verifies that amounts are summed correctly for both integer and BigDecimal inputs.
     */
    @org.junit.Test
    public void addWithSameCurrency() {
        Money result = m1.add(m2);
        assertEquals(30, result.getAmount().intValue());
        assertEquals("$", result.getCurrency());
        assertEquals(BigDecimal.valueOf(90.56), m3.add(m4).getAmount());
        assertEquals("€", m3.add(m4).getCurrency());
    }

    /**
     * Tests that the add method throws an exception when attempting
     * to add amounts with different currencies.
     */
    @org.junit.Test(expected = Exception.class)
    public void addWithDifferentCurrency() {
        m1.add(m3);
    }

    /**
     * Tests the equals method for objects with identical values.
     */
    @org.junit.Test
    public void testEqualsSameValues() {
        Money money = new Money(1000, "€");
        Money other = new Money(1000, "€");

        Assertions.assertEquals(money, other);
    }

    /**
     * Tests the equals method for objects with different amounts or currencies.
     */
    @org.junit.Test
    public void testEqualsDifferentValues() {
        Money money = new Money(499, "€");
        Money other = new Money(500, "€");
        Assertions.assertNotEquals(money, other);

        money.setAmount(BigDecimal.valueOf(500));
        money.setCurrency("$");
        Assertions.assertNotEquals(money, other);

        money.setAmount(BigDecimal.valueOf(499));
        Assertions.assertNotEquals(money, other);
    }
}