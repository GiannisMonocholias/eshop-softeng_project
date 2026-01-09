package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.Address;

/**
 * Unit tests for the {@link Address} class.
 * This class verifies the correct behavior of getters, setters, and the critical
 * logic for {@code equals()} and {@code hashCode()} methods.
 * @author Γιάννης Μονοχολιάς
 */
public class AddressTest {
    private Address address;

    /**
     * Initializes a fresh Address instance before each test execution.
     */
    @Before
    public void setUp() {
        address = new Address("Street", "10", "Athens", "Greece", "12345");
    }

    /**
     * Tests the street property accessors.
     */
    @Test
    public void getAndSetStreetTest() {
        assertEquals("Street", address.getStreet());
        address.setStreet("Other Street");
        assertEquals("Other Street", address.getStreet());
    }

    /**
     * Tests the zipcode property accessors.
     */
    @Test
    public void getAndSetZipcodeTest() {
        assertEquals("12345", address.getZipcode());
        address.setZipcode("54321");
        assertEquals("54321", address.getZipcode());
    }

    /**
     * Tests the country property accessors.
     */
    @Test
    public void getAndSetCountryTest() {
        assertEquals("Greece", address.getCountry());
        address.setCountry("Italy");
        assertEquals("Italy", address.getCountry());
    }

    /**
     * Tests the city property accessors.
     */
    @Test
    public void getAndSetCityTest() {
        assertEquals("Athens", address.getCity());
        address.setCity("Thessaloniki");
        assertEquals("Thessaloniki", address.getCity());
    }

    /**
     * Tests the building number property accessors.
     */
    @Test
    public void getAndSetNumberTest() {
        assertEquals("10", address.getNumber());
        address.setNumber("20");
        assertEquals("20", address.getNumber());
    }

    /**
     * Verifies that the equals method returns true for the same object instance (Reflexivity).
     */
    @Test
    public void equalsSameAddressTest() {
        Address address = new Address("A", "1", "C", "E", "D");
        assertEquals(address, address);
    }

    /**
     * Verifies that equals returns false when comparing against null or a different class type.
     */
    @Test
    public void equalsNullAndOtherClassTest() {
        Address address = new Address("A", "1", "C", "E", "D");
        assertNotEquals(null, address);
        assertNotEquals(new Object(), address);
    }

    /**
     * Tests full equality between two distinct objects with identical fields.
     * Also verifies that identical objects produce identical hash codes.
     */
    @Test
    public void equalsFullEqualityTest() {
        Address address1 = new Address("Ermou", "10", "Athens", "Greece", "10563");
        Address address2 = new Address("Ermou", "10", "Athens", "Greece", "10563");

        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
    }

    /**
     * Verifies that equality fails when any single field differs between two Address objects.
     */
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

    /**
     * Verifies that two Address objects with all fields set to null are considered equal.
     */
    @Test
    public void equalsNullVsNullFieldsTest() {
        Address address1 = new Address(null, null, null, null, null);
        Address address2 = new Address(null, null, null, null, null);

        assertEquals(address1, address2);
        assertEquals(0, address1.hashCode());
    }

    /**
     * Verifies that a fully populated Address is not equal to a null-field Address.
     */
    @Test
    public void equalsNonNullVsNullTest() {
        Address nonNullAddress = new Address("A", "1", "C", "E", "D");
        Address nullAddress = new Address(null, null, null, null, null);

        assertNotEquals(nonNullAddress, nullAddress);
    }

    /**
     * Verifies inequality when the current object has null fields but the compared object does not.
     */
    @Test
    public void equalsNullVsNonNullTest() {
        Address nullAddress = new Address(null, null, null, null, null);
        Address nonNullAddress = new Address("A", "1", "C", "E", "D");

        assertNotEquals(nullAddress, nonNullAddress);
    }

    /**
     * Tests partial null scenarios to ensure the equals method is robust against null pointers.
     */
    @Test
    public void equalsPartialNullTest() {
        Address address1 = new Address("Ermou", "10", "Athens", "Greece", null);
        Address address2 = new Address("Ermou", "10", "Athens", "Greece", "10563");

        assertNotEquals(address1, address2);
        assertNotEquals(address2, address1);
    }

    /**
     * Verifies that objects with common null fields but differing non-null fields are not equal.
     */
    @Test
    public void equalsOneNullFieldAndOtherDifferenceTest() {
        Address address1 = new Address("A", "1", "C", null, "D");
        Address address2 = new Address("B", "1", "C", null, "D");

        assertNotEquals(address1, address2);
        assertNotEquals(address1.hashCode(), address2.hashCode());
    }

    /**
     * Verifies that an empty/uninitialized Address object returns a zero hash code.
     */
    @Test
    public void hashCodeNullAllPropertiesIsZeroTest(){
        Address address1 = new Address();
        assertEquals(0, address1.hashCode());
    }

    /**
     * Verifies the hash code contract: equal objects must have equal hash codes.
     */
    @Test
    public void equalAddressesSameHashCodeTest(){
        Address address1 = new Address("Street", "10", "Athens", "Greece", "12345");
        Address address2 = new Address("Street", "10", "Athens", "Greece", "12345");

        assertEquals(address1.hashCode(), address1.hashCode());
        assertEquals(address1.hashCode(), address2.hashCode());
    }

    /**
     * Verifies that distinct addresses result in different hash codes to minimize collisions.
     */
    @Test
    public void nonEqualAddressesDifferentHashCodeTest(){
        Address address1 = new Address("Street1", "11", "Athens", "Greece", "12345");
        Address address2 = new Address("Street2", "12", "Athens", "Greece", "34214");

        assertNotEquals(address1.hashCode(), address2.hashCode());
    }
}