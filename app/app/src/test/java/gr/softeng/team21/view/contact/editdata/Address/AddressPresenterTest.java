package gr.softeng.team21.view.contact.editdata.Address;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for the {@link AddressPresenter} class.
 * These tests verify the logic for saving address details, validating input and handling user retrieval.
 * @author PAVLOS GRATSANIS
 */
public class AddressPresenterTest {

    private AddressPresenter presenter;
    private AddressViewStub view;
    private Customer customer;

    /**
     * Sets up the test class before each test case.
     * Initializes in-memory data, retrieves a test customer, creates a view stub and initializes the presenter.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-501");
        view = new AddressViewStub();
        presenter=new AddressPresenter(view,customer.getCustomer_id());
    }

    /**
     * Verifies that the address is successfully updated when all input fields are valid.
     * Checks if the success message is displayed and if the customer object is updated correctly.
     */
    @Test
    public void saveAddressClickedSuccess() {
        String newStreet = "Nea Odos";
        String newNum = "10";
        String newZip = "12345";
        String newCity = "Athens";
        String newCountry = "Greece";
        presenter.saveAddressClicked(newStreet, newNum, newCity, newCountry, newZip);
        Assert.assertEquals("Η διεύθυνση ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals(newStreet, customer.getAddress().getStreet());
        Assert.assertEquals(newZip, customer.getAddress().getZipcode());
    }

    /**
     * Verifies that validation errors are shown when any of the required address fields are empty.
     */
    @Test
    public void saveAddressClickedEmptyFields() {
        presenter.saveAddressClicked("", "10", "Athens", "Greece", "12345");
        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία της διεύθυνσης", view.getMessage());
        presenter.saveAddressClicked("Ermou", "", "Athens", "Greece", "12345");
        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία της διεύθυνσης", view.getMessage());
        presenter.saveAddressClicked("Ermou", "10", "", "Greece", "12345");
        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία της διεύθυνσης", view.getMessage());
        presenter.saveAddressClicked("Ermou", "10", "Athens", "", "12345");
        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία της διεύθυνσης", view.getMessage());
        presenter.saveAddressClicked("Ermou", "10", "Athens", "Greece", "");
        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία της διεύθυνσης", view.getMessage());

    }

    /**
     * Verifies that a validation error is shown when the zip code  is not 5 digits long.
     */
    @Test
    public void saveAddressClicked_InvalidZip() {
        presenter.saveAddressClicked("Odos", "1", "City", "Country", "123");
        Assert.assertEquals("Ο ΤΚ πρέπει να είναι 5 ψηφία", view.getMessage());
    }

    /**
     * Verifies that the presenter handles a null user gracefully without crashing.
     *Nothing is tested simply to have 100% coverage.
     */
    @Test
    public void saveAddressClickedWithNull() {
        AddressPresenter nullpresenter=new AddressPresenter(view,null);
        nullpresenter.saveAddressClicked("","","","","");

    }

    /**
     * Tests the user retrieval logic in the presenter constructor.
     * Verifies cases where the user ID is null (user not found) and where a user exists but has no address.
     */
    @Test
    public void testfindUser(){
        AddressViewStub viewForNullId = new AddressViewStub();
        AddressPresenter nullpresenter=new AddressPresenter(viewForNullId,null);
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.",viewForNullId.getMessage());

        AddressViewStub viewForNoAddr = new AddressViewStub();
        Customer customer1 = new Customer("test", "first", "pass", "last", "699",
                new EmailAddress("a@b.com"), "CUST-NO-ADDR", null);
        customer1.setAddress(null);
        CustomerDAOMemory.getInstance().addCustomer(customer1);

        new AddressPresenter(viewForNoAddr, "CUST-NO-ADDR");
        Assert.assertNull(viewForNoAddr.getStreet());
    }
}