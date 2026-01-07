package gr.softeng.team21.view.contact.editdata.Email;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

public class EmailPresenterTest {

    private EmailViewStub view;
    private EmailPresenter presenter;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new EmailViewStub();
        customer = MemoryInitializer.getCustomerDAO().getCustomer("CUST-500");

        presenter = new EmailPresenter(view, customer.getCustomer_id());
    }

    @Test
    public void saveEmailClickedSuccess() {
        presenter.saveEmailClicked("new.nick@example.com");
        Assert.assertEquals("Το email ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals("new.nick@example.com", customer.getEmailAddress().toString());
    }

    @Test
    public void saveEmailClickedInvalidFormat() {
        presenter.saveEmailClicked("invalid_email_format");
        Assert.assertEquals("Μη έγκυρη μορφή email", view.getMessage());
    }

    @Test
    public void saveEmailClickedEmpty() {
        presenter.saveEmailClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε Email", view.getMessage());
    }

    @Test
    public void saveEmailClickedWithNullUser() {
        EmailPresenter presenternull = new EmailPresenter(view, null);
        presenternull.saveEmailClicked("validPass@gmail.com");

    }

    @Test
    public void testFindUser() {
        EmailViewStub viewNotFound = new EmailViewStub();
        EmailPresenter presenter1 = new EmailPresenter(viewNotFound, "INVALID-ID-999");
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.", viewNotFound.getMessage());

        EmailViewStub viewNullEmail = new EmailViewStub();
        Customer customerNullEmail = new Customer(
                "userNullEmail",
                "Name",
                "password123",
                "Surname",
                "6900000000",
                null,
                "CUSTNULLEMAIL",
                new Date()
        );
        CustomerDAOMemory.getInstance().addCustomer(customerNullEmail);
        EmailPresenter presenter2 = new EmailPresenter(viewNullEmail, customerNullEmail.getCustomer_id());
        Assert.assertNull(viewNullEmail.getEmail());

        EmailViewStub viewEmptyString = new EmailViewStub();

        Customer customerEmptyString = new Customer(
                "userEmpty",
                "Name",
                "pass",
                "Surname",
                "6900000000",
                new EmailAddress(""), 
                "CUST_EMPTY_STR",
                new Date()
        );

        CustomerDAOMemory.getInstance().addCustomer(customerEmptyString);
        EmailPresenter presenter3 = new EmailPresenter(viewEmptyString, customerEmptyString.getCustomer_id());
        Assert.assertNull(viewEmptyString.getEmail());
    }

}