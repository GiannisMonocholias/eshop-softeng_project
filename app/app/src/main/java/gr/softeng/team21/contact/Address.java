package gr.softeng.team21.contact;

/**
 * Represents a physical address with street, number, city, country, and zipcode.
 * @author PAVLOS GRATSANIS,Γιάννης Μονοχολιάς
 */
public class Address {

    /** The street of the address */
    private String street;

    /** The number of the address */
    private String number;

    /** The city of the address */
    private String city;

    /** The country of the address */
    private String country;

    /** The zipcode of the address */
    private String zipcode;


    /**
     * Default constructor
     */
    public Address() {}

    /**
     * Creates a new Address with the specified details.
     * @param street  the street name
     * @param number  the street number
     * @param city    the city name
     * @param country the country name
     * @param zipcode the postal code
     */
    public Address (String street, String number, String city,String country, String zipcode) {
        this.street = street;
        this.number = number;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
    }

    /**
     * Returns the street of the address.
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street of the address.
     * @param street the new street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the zipcode of the address.
     * @return the zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * Sets the zipcode of the address.
     * @param zipcode the new zipcode
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * Returns the country of the address.
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the address.
     * @param country the new country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the city of the address.
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the address.
     * @param city the new city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the number of the address.
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the number of the address.
     * @param number the new number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Checks whether this Address object is equal to another object.
     * Equality is based on street, number, city, country, and zipcode.
     * @param other the object to compare with
     * @return true if all address fields are equal
     */
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

    /**
     * Returns a hash code value for the address.
     * The hash code is calculated based on all address fields.
     * @return the hash code
     */
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

    /**
     * Returns a string representation of the address.
     * Format: Street Number, City Country Zipcode
     * @return the formatted address string
     */
    @Override
    public String toString(){
        return this.getStreet() + " " + this.getNumber() + "," + this.getCity()+" "+this.country+" "+this.zipcode;
    }
}