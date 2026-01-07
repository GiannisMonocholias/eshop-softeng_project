package gr.softeng.team21.view.contact.editdata.Username;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.util.Date;



public class UsernamePresenterTest {
private Customer customer;
private UsernamePresenter presenter;
private UsernameViewStub view;
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view=new UsernameViewStub();
        customer=MemoryInitializer.getCustomerDAO().getCustomer("CUST-500");
        presenter=new UsernamePresenter(view, customer.getCustomer_id());
    }

    @Test
    public void saveUsernameClickedSuccess() {
        presenter.saveUsernameClicked("newusername");
        Assert.assertEquals("Το username ενημερώθηκε επιτυχώς!", view.getMessage());
        Assert.assertEquals("newusername", customer.getUsername());


    }


    @Test
    public void saveUsernameClickedEmptyInput() {
        presenter.saveUsernameClicked("");
        Assert.assertEquals("Παρακαλώ εισάγετε Username", view.getMessage());

    }


    @Test
    public void saveUsernameClickedSameName() {
        String currentName = customer.getUsername();
        presenter.saveUsernameClicked(currentName);
        Assert.assertEquals("Δεν έγιναν αλλαγές.", view.getMessage());

    }


    @Test
    public void saveUsernameClickedTakenName() {
        presenter.saveUsernameClicked("georgepap");
        Assert.assertEquals("Το username χρησιμοποιείται ήδη.", view.getMessage());

    }
    @Test
    public void saveUsernameClickedWithNull() {
        UsernamePresenter nullpresenter=new UsernamePresenter(view,null);
        nullpresenter.saveUsernameClicked("newname");

    }

    @Test
    public void testfindUser(){
        UsernameViewStub viewForNullId = new UsernameViewStub();
        UsernamePresenter nullpresenter=new UsernamePresenter(viewForNullId,null);
        Assert.assertEquals("Ο χρήστης δεν βρέθηκε.",viewForNullId.getMessage());

        UsernameViewStub viewForNullName = new UsernameViewStub();
        Customer customer1=new Customer(null,"firstname","pass","lastname","69999",
                new EmailAddress("@"),"CUST-NULL",new Date());
        CustomerDAOMemory.getInstance().addCustomer(customer1);
        UsernamePresenter presenter1=new UsernamePresenter(viewForNullName, customer1.getCustomer_id());
        Assert.assertNull(viewForNullName.getCurrentUsername());

        UsernameViewStub viewForEmptyName = new UsernameViewStub();
        Customer customer2=new Customer("","firstname","pass","lastname","69999",
                new EmailAddress("@"),"CUST-EMPTY",new Date());
        CustomerDAOMemory.getInstance().addCustomer(customer2);
        UsernamePresenter presenter2=new UsernamePresenter(viewForEmptyName, customer2.getCustomer_id());
        Assert.assertNull(viewForEmptyName.getCurrentUsername());


    }
}