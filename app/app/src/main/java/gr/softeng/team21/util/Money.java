package gr.softeng.team21.util;

import java.math.BigDecimal;

/**
 * Represents a monetary value consisting of an amount and a currency.
 *
 * @author PAVLOS GRATSANIS
 * @version 1.0
 * AM: 3230036
 */
public class Money {

    /** The monetary amount */
    private BigDecimal amount;

    /** The currency of the monetary amount (e.g $,€)*/
    private String currency;

    /**
     * Creates a Money object with a BigDecimal amount.
     *
     * @param amount the monetary amount
     * @param currency the currency
     */
    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Creates a Money object with an integer amount.
     *
     * @param amount the monetary amount as integer
     * @param currency the currency
     */
    public Money(int amount, String currency) {
        this.amount = BigDecimal.valueOf(amount);
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Multiplies the monetary amount by the given quantity.
     *
     * @param quantity the multiplication factor
     * @return a new Money object with the multiplied amount
     */
    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }

    /**
     * Adds another Money object to this one.
     * Both Money objects must have the same currency.
     *
     * @param other the Money object to add
     * @return a new Money object representing the sum
     * @throws IllegalArgumentException if currencies are different
     */
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Different currencies");
        }
        return new Money(this.amount.add(other.amount), currency);
    }

    /**
     * Checks whether this Money object is equal to another object.
     *
     * @param other the object to compare with
     * @return true if both amount and currency are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof Money))
            return false;
        if (other == this)
            return true;

        if (this.getAmount().equals(((Money) other).getAmount())
                && this.getCurrency().equals(((Money) other).getCurrency()))
            return true;
        return false;
    }


    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }
}
