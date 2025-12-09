import org.junit.After;
import org.junit.jupiter.api.*;
import static org.junit.Assert.*;

public class AddressTest {
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Street", "10", "Athens", "Greece", "12345");
    }

    @Test
    void getAndSetStreetTest() {
        Assertions.assertEquals("Street", address.getStreet());
        address.setStreet("Other Street");
        Assertions.assertEquals("Other Street", address.getStreet());
    }

    @Test
    void getAndSetZipcodeTest() {
        Assertions.assertEquals("12345", address.getZipcode());
        address.setZipcode("54321");
        Assertions.assertEquals("54321", address.getZipcode());
    }

    @Test
    void getAndSetCountryTest() {
        Assertions.assertEquals("Greece", address.getCountry());
        address.setCountry("Italy");
        Assertions.assertEquals("Italy", address.getCountry());
    }

    @Test
    void getAndSetCityTest() {
        Assertions.assertEquals("Athens", address.getCity());
        address.setCity("Thessaloniki");
        Assertions.assertEquals("Thessaloniki", address.getCity());
    }

    @Test
    void getAndSetNumberTest() {
        Assertions.assertEquals("10", address.getNumber());
        address.setNumber("20");
        Assertions.assertEquals("20", address.getNumber());
    }

    @Test
    public void equalsSameAddressTest() {
        Address address = new Address("A", "1", "C", "E", "D");
        Assertions.assertEquals(address, address);
    }

    @Test
    public void equalsNullAndOtherClassTest() {
        Address address = new Address("A", "1", "C", "E", "D");
        Assertions.assertNotEquals(null, address);


        Assertions.assertNotEquals(new Object(), address);
    }

    @Test
    public void equalsFullEqualityTest() {
        Address address1 = new Address("Ermou", "10", "Athens", "Greece", "10563");
        Address address2 = new Address("Ermou", "10", "Athens", "Greece", "10563");

        Assertions.assertEquals(address1, address2);
        Assertions.assertEquals(address1.hashCode(), address2.hashCode());
    }


    @Test
    public void equalsDifferingFieldsTest() {
        Address base = new Address("A", "1", "C", "E", "D");
        Address different;

        different = new Address("B", "1", "C", "E", "D");
        Assertions.assertNotEquals(base, different, "Should differ in Street");

        different = new Address("A", "2", "C", "E", "D");
        Assertions.assertNotEquals(base, different, "Should differ in Number");

        different = new Address("A", "1", "D", "E", "D");
        Assertions.assertNotEquals(base, different, "Should differ in City");

        different = new Address("A", "1", "C", "F", "D");
        Assertions.assertNotEquals(base, different, "Should differ in Country");

        different = new Address("A", "1", "C", "E", "E");
        Assertions.assertNotEquals(base, different, "Should differ in Zipcode");
    }


    @Test
    public void equalsNullVsNullFieldsTest() {

        Address address1 = new Address(null, null, null, null, null);
        Address address2 = new Address(null, null, null, null, null);

        Assertions.assertEquals(address1, address2, "Null fields should be equal");
        Assertions.assertEquals(0, address1.hashCode());
    }

    @Test
    public void equalsNonNullVsNullTest() {
        Address nonNullAddress = new Address("A", "1", "C", "E", "D");
        Address nullAddress = new Address(null, null, null, null, null);

        Assertions.assertNotEquals(nonNullAddress, nullAddress, "Non-null should not equal null");
    }

    @Test
    public void equalsNullVsNonNullTest() {
        Address nullAddress = new Address(null, null, null, null, null);
        Address nonNullAddress = new Address("A", "1", "C", "E", "D");

        Assertions.assertNotEquals(nullAddress, nonNullAddress, "Null should not equal non-null");
    }

    @Test
    public void equalsPartialNullTest() {
        Address address1 = new Address("Ermou", "10", "Athens", "Greece", null); // Zipcode is null
        Address address2 = new Address("Ermou", "10", "Athens", "Greece", "10563"); // Zipcode is non-null

        Assertions.assertNotEquals(address1, address2, "Should differ if only one zipcode is null");

        Assertions.assertNotEquals(address2, address1, "Should differ (Symmetry check)");
    }

    @Test
    public void equalsOneNullFieldAndOtherDifferenceTest() {
        Address address1 = new Address("A", "1", "C", null, "D");
        Address address2 = new Address("B", "1", "C", null, "D");

        Assertions.assertNotEquals(address1, address2, "Should differ in Street even with common null Country");

        Assertions.assertNotEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    public void hashCodeNullAllPropertiesIsZeroTest(){
        Address address1 = new Address();
        Assertions.assertEquals(0,address1.hashCode());
    }

    @Test
    public void equalAddressesSameHashCodeTest(){
        Address address1 = new Address("Street", "10", "Athens", "Greece", "12345");
        Address address2 = new Address("Street", "10", "Athens", "Greece", "12345");

        Assertions.assertEquals(address1.hashCode(),address1.hashCode());

        Assertions.assertEquals(address1.hashCode(),address2.hashCode());
    }

    @Test
    public void nonEqualAddressesDifferentHashCodeTest(){
        Address address1 = new Address("Street1", "11", "Athens", "Greece", "12345");
        Address address2 = new Address("Street2", "12", "Athens", "Greece", "34214");

        Assertions.assertNotEquals(address1.hashCode(),address2.hashCode());
    }

}
