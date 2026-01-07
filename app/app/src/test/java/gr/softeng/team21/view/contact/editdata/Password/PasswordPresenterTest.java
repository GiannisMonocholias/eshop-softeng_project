package gr.softeng.team21.view.contact.editdata.Password;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;

public class PasswordPresenterTest {

    private PasswordViewStub view;
    private PasswordPresenter presenter;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view = new PasswordViewStub();
        customer=MemoryInitializer.getCustomerDAO().getCustomer("CUST-500");

        presenter = new PasswordPresenter(view, customer.getCustomer_id());
    }

    @Test
    public void savePasswordClickedSuccess() {
        presenter.savePasswordClicked("NewPass2024");
        Assert.assertEquals("Ο κωδικός ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals("NewPass2024", customer.getPassword());
    }

    @Test
    public void savePasswordClickedInvalidLength() {
        presenter.savePasswordClicked("12345");
        Assert.assertEquals("Ο κωδικός πρέπει να έχει τουλάχιστον 8 χαρακτήρες", view.getMessage());
    }

    @Test
    public void savePasswordClickedEmpty() {
        presenter.savePasswordClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε κωδικό", view.getMessage());
    }

    @Test
    public void savePasswordClickedSamePassword() {
        presenter.savePasswordClicked("pass1234");
        Assert.assertEquals("Ο νέος κωδικός δεν μπορεί να είναι ίδιος με τον παλιό", view.getMessage());
    }
    @Test
    public void savePasswordClickedWithNullUser() {
        PasswordPresenter safePresenter = new PasswordPresenter(view, null);
        safePresenter.savePasswordClicked("validPass123");

    }

    @Test
    public void testFindUser() {
        PasswordViewStub viewNotFound = new PasswordViewStub();
        PasswordPresenter presenter1=new PasswordPresenter(viewNotFound, "INVALID-ID-999");
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.", viewNotFound.getMessage());
        PasswordViewStub viewNullPass = new PasswordViewStub();

        Customer customerNullPass = new Customer("userNull", "Name", null, "Surname",
                "6900000000", new EmailAddress("null@test.gr"), "CUSTNULLPASS", new Date());
        CustomerDAOMemory.getInstance().addCustomer(customerNullPass);
       PasswordPresenter presenter2= new PasswordPresenter(viewNullPass, customerNullPass.getCustomer_id());
        Assert.assertNull(viewNullPass.getPassword());
    }
}