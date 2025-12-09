package gr.softeng.team21.domain;

import java.math.BigDecimal;
public class Money {
    private BigDecimal amount;
    private String currency;

    public Money (BigDecimal amount,String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    public Money(int amount, String currency) {
        this.amount = BigDecimal.valueOf(amount);
        this.currency = currency;
    }

    public BigDecimal getAmount ( ) {
        return amount;
    }

    public void setAmount (BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency ( ) {
        return currency;
    }

    public void setCurrency (String currency) {
        this.currency = currency;
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Different currencies");
        }
        return new Money(this.amount.add(other.amount), currency);
    }
    @Override
    public boolean equals(Object other){
        if(other == null)
            return false;
        if(!(other instanceof Money))
            return false;
        if(other == this)
            return true;

        if(this.getAmount().equals(((Money) other).getAmount()) && this.getCurrency().equals(((Money) other).getCurrency()))
            return true;
        return false;
    }


}
