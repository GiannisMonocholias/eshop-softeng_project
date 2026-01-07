package gr.softeng.team21.view.contact.editdata.Phone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;


public class PhonePresenterTest {

    private PhonePresenter presenter;
    private PhoneViewStub view;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        customer = CustomerDAOMemory.getInstance().getCustomer("CUST-501");
        view = new PhoneViewStub();
        presenter = new PhonePresenter(view, customer.getCustomer_id());
    }

    @Test
    public void savePhoneClickedSuccess() {
        presenter.savePhoneClicked("6966778899");
        Assert.assertEquals("Το τηλέφωνο ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals("6966778899", customer.getPhonenumber());
    }

    @Test
    public void savePhoneClickedInvalidLength() {
        presenter.savePhoneClicked("12345");
        Assert.assertEquals("Το τηλέφωνο πρέπει να έχει 10 ψηφία", view.getMessage());
    }


    @Test
    public void savePhoneClickedEmpty() {
        presenter.savePhoneClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε τηλέφωνο", view.getMessage());
    }
    @Test
    public void savePhoneClickedWithNullUser() {
        PhonePresenter nullPresenter = new PhonePresenter(view, null);
        nullPresenter.savePhoneClicked("6912345678");

    }
    @Test
    public void testfindUser(){
        presenter=new PhonePresenter(view, "INVALID-ID-999");
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.", view.getMessage());

        PhoneViewStub viewnullphone=new PhoneViewStub();
        Customer customer1=new Customer("passfirst","firstname","pass","lastname",null,
                new EmailAddress("@"),"CUSTNULLPHONE",new Date());
        CustomerDAOMemory.getInstance().addCustomer(customer1);
        PhonePresenter presenter1=new PhonePresenter(viewnullphone, customer1.getCustomer_id());
        Assert.assertNull(viewnullphone.getPhone());
    }

}