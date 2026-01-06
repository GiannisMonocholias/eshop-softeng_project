package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.Address;

public class AddressTest {
    private Address address;

    @Before
    public void setUp() {
        address = new Address("Street", "10", "Athens", "Greece", "12345");
    }

    @Test
    public void getAndSetStreetTest() {
        assertEquals("Street", address.getStreet());
        address.setStreet("Other Street");
        assertEquals("Other Street", address.getStreet());
    }

    @Test
    public void getAndSetZipcodeTest() {
        assertEquals("12345", address.getZipcode());
        address.setZipcode("54321");
        assertEquals("54321", address.getZipcode());
    }

    @Test
    public void getAndSetCountryTest() {
        assertEquals("Greece", address.getCountry());
        address.setCountry("Italy");
        assertEquals("Italy", address.getCountry());
    }

    @Test
    public void getAndSetCityTest() {
        assertEquals("Athens", address.getCity());
        address.setCity("Thessaloniki");
        assertEquals("Thessaloniki", address.getCity());
    }

    @Test
    public void getAndSetNumberTest() {
        assertEquals("10", address.getNumber());
        address.setNumber("20");
        assertEquals("20", address.getNumber());
    }

    @Test
    public void equalsSameAddressTest() {
        Address address = new Address("A", "1", "C", "E", "D");
        assertEquals(address, address);
    }

    @Test
    public void equalsNullAndOtherClassTest() {
        Address address = new Address("A", "1", "C", "E", "D");
        assertNotEquals(null, address);


        assertNotEquals(new Object(), address);
    }

    @Test
    public void equalsFullEqualityTest() {
        Address address1 = new Address("Ermou", "10", "Athens", "Greece", "10563");
        Address address2 = new Address("Ermou", "10", "Athens", "Greece", "10563");

        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
    }


    @Test
    public void equalsDifferingFieldsTest() {
        Address base = new Address("A", "1", "C", "E", "D");
        Address different;

        different = new Address("B", "1", "C", "E", "D");
        assertNotEquals(base, different);

        different = new Address("A", "2", "C", "E", "D");
        assertNotEquals(base, different);

        different = new Address("A", "1", "D", "E", "D");
        assertNotEquals(base, different);

        different = new Address("A", "1", "C", "F", "D");
        assertNotEquals(base, different);

        different = new Address("A", "1", "C", "E", "E");
        assertNotEquals(base, different);
    }


    @Test
    public void equalsNullVsNullFieldsTest() {

        Address address1 = new Address(null, null, null, null, null);
        Address address2 = new Address(null, null, null, null, null);

        assertEquals(address1, address2);
        assertEquals(0, address1.hashCode());
    }

    @Test
    public void equalsNonNullVsNullTest() {
        Address nonNullAddress = new Address("A", "1", "C", "E", "D");
        Address nullAddress = new Address(null, null, null, null, null);

        assertNotEquals(nonNullAddress, nullAddress);
    }

    @Test
    public void equalsNullVsNonNullTest() {
        Address nullAddress = new Address(null, null, null, null, null);
        Address nonNullAddress = new Address("A", "1", "C", "E", "D");

        assertNotEquals(nullAddress, nonNullAddress);
    }

    @Test
    public void equalsPartialNullTest() {
        Address address1 = new Address("Ermou", "10", "Athens", "Greece", null);
        Address address2 = new Address("Ermou", "10", "Athens", "Greece", "10563");

        assertNotEquals(address1, address2);

        assertNotEquals(address2, address1);
    }

    @Test
    public void equalsOneNullFieldAndOtherDifferenceTest() {
        Address address1 = new Address("A", "1", "C", null, "D");
        Address address2 = new Address("B", "1", "C", null, "D");

        assertNotEquals(address1, address2);

        assertNotEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    public void hashCodeNullAllPropertiesIsZeroTest(){
        Address address1 = new Address();
        assertEquals(0,address1.hashCode());
    }

    @Test
    public void equalAddressesSameHashCodeTest(){
        Address address1 = new Address("Street", "10", "Athens", "Greece", "12345");
        Address address2 = new Address("Street", "10", "Athens", "Greece", "12345");

        assertEquals(address1.hashCode(),address1.hashCode());

        assertEquals(address1.hashCode(),address2.hashCode());
    }

    @Test
    public void nonEqualAddressesDifferentHashCodeTest(){
        Address address1 = new Address("Street1", "11", "Athens", "Greece", "12345");
        Address address2 = new Address("Street2", "12", "Athens", "Greece", "34214");

        assertNotEquals(address1.hashCode(),address2.hashCode());
    }

}