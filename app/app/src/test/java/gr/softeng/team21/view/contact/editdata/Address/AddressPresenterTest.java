package gr.softeng.team21.view.contact.editdata.Address;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

public class AddressPresenterTest {

    private AddressPresenter presenter;
    private AddressViewStub view;
    private Customer customer;

    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-501");
        view = new AddressViewStub();
        presenter=new AddressPresenter(view,customer.getCustomer_id());
    }

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
        Assert.assertEquals(newCity, customer.getAddress().getCity());
    }
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
    @Test
    public void saveAddressClicked_InvalidZip() {
        presenter.saveAddressClicked("Odos", "1", "City", "Country", "123");
        Assert.assertEquals("Ο ΤΚ πρέπει να είναι 5 ψηφία", view.getMessage());
    }

    @Test
    public void saveAddressClickedWithNull() {
        AddressPresenter nullpresenter=new AddressPresenter(view,null);
        nullpresenter.saveAddressClicked("","","","","");

    }
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