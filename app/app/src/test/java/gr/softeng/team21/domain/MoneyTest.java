package gr.softeng.team21.domain;

import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

import static org.junit.Assert.*;

import static org.junit.Assert.*;
public class MoneyTest {
    private Money m1, m2, m3,m4;

    @org.junit.Before
    public void setUp ( ) throws Exception {
        m1 = new Money ( 10, "$" );
        m2 = new Money ( 20, "$" );
        m3 = new Money ( 40, "€" );
        m4=new Money ( new BigDecimal ( "50.56" ),"€" );
    }


    @org.junit.Test
    public void addWithSameCurrency ( ) {
        Money result = m1.add ( m2 );
        assertEquals(30, result.getAmount().intValue ());
        assertEquals ( "$", result.getCurrency ( ) );
        assertEquals ( BigDecimal.valueOf (90.56),m3.add ( m4 ).getAmount () );
        assertEquals ( "€",m3.add ( m4 ).getCurrency () );
    }

    @org.junit.Test(expected = Exception.class)
    public void addWithDifferentCurrency ( ) {
        m1.add ( m3 );
    }
    @org.junit.Test
    public void testEqualsSameValues() {
        Money money = new Money(1000,"€");
        Money other = new Money(1000, "€");

        Assertions.assertEquals(money, other);
    }

    @org.junit.Test
    public void testEqualsDifferentValues() {
        Money money = new Money(499,"€");
        Money other = new Money(500, "€");
        Assertions.assertNotEquals(money, other);

        money.setAmount( BigDecimal.valueOf ( 500 ) );
        money.setCurrency("$");
        Assertions.assertNotEquals(money, other);

        money.setAmount( BigDecimal.valueOf ( 499 ) );
        Assertions.assertNotEquals(money, other);
    }

}