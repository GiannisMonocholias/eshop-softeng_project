package gr.softeng.team21.domain;

public class EmailAddress {
    private String value;

    public EmailAddress(String value){
        this.value = value;
    }

    public String getAddress() {
        return value;
    }
    public void setAddress(String newvalue) {this.value = newvalue;}


    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (!(other instanceof EmailAddress)) {
            return false;
        }

        EmailAddress theEmail = (EmailAddress) other;
        return value == null ? theEmail.getAddress() == null
                : value.equals(theEmail.getAddress());
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }


    @Override
    public String toString() {
        return value;
    }
}
