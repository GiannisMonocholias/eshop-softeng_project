package gr.softeng.team21.domain;

public class Address {
    private String street;
    private String number;
    private String city;
    private String country;
    private String zipcode;


    public Address() {}


    public Address (String street, String number, String city,String country, String zipcode) {
        this.street = street;
        this.number = number;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(this == other){
            return true;
        }
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        if (!(street == null ? otherAddress.street
                == null : street.equals(otherAddress.street))) {
            return false;
        }
        if (!(number == null ? otherAddress.number
                == null : number.equals(otherAddress.number))) {
            return false;
        }
        if (!(city == null ? otherAddress.city
                == null : city.equals(otherAddress.city))) {
            return false;
        }
        if (!(zipcode == null ? otherAddress.zipcode
                == null : zipcode.equals(otherAddress.zipcode))) {
            return false;
        }
        if (!(country == null ? otherAddress.country
                == null : country.equals(otherAddress.country))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        if (street == null && number == null && city == null
                && zipcode == null && country == null) {
            return 0;
        }

        int result = 0;
        result = street == null ? result : 13 * result + street.hashCode();
        result = number == null ? result : 13 * result + number.hashCode();
        result = city == null ? result : 13 * result + city.hashCode();
        result = zipcode == null ? result : 13 * result + zipcode.hashCode();
        result = country == null ? result : 13 * result + country.hashCode();
        return result;
    }
    @Override
    public String toString() {
        return street + " " + number + ", " + city + " " + zipcode;
    }
}

