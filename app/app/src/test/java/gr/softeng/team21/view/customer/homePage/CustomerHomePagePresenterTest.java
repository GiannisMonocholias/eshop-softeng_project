package gr.softeng.team21.view.customer.homePage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

public class CustomerHomePagePresenterTest {
    private CustomerHomePagePresenter presenter;
    private Customer customer;
    private CustomerHomePageViewStub view;
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        view=new CustomerHomePageViewStub();
        customer= CustomerDAOMemory.getInstance().getCustomer("CUST-500");
        presenter=new CustomerHomePagePresenter(view,customer);
    }

    @Test
    public void editDataClicked() {
        presenter.EditDataClicked();
        Assert.assertEquals(1, view.getEditDataCount());
    }

    @Test
    public void logoutClicked() {
        presenter.LogoutClicked();
        Assert.assertEquals(1,view.getLogoutCount());
    }

    @Test
    public void findProductClicked() {
        presenter.FindProductClicked();
        Assert.assertEquals(1,view.getFindProductCount());
    }

    @Test
    public void deleteClicked() {
        presenter.DeleteClicked();
        Assert.assertEquals(1,view.getDeleteCount());

    }

    @Test
    public void deleteConfirm() {
        Assert.assertNotNull(CustomerDAOMemory.getInstance().getCustomer("CUST-500"));
        presenter.DeleteConfirm();
        Assert.assertEquals("Ο λογαριασμός σας διαγράφηκε.", view.getMessage());
        Assert.assertEquals(1, view.getMainCount());
        Assert.assertNull(CustomerDAOMemory.getInstance().getCustomer("CUST-500"));
    }

    @Test
    public void inboxClicked() {
        presenter.InboxClicked();
        Assert.assertEquals(1,view.getInboxCount());
    }
    @Test
    public void deleteConfirmWithNullCustomer(){
        CustomerHomePagePresenter nullpresenter = new CustomerHomePagePresenter(view, null);
        nullpresenter.DeleteConfirm();
        Assert.assertEquals(0, view.getMainCount());

    }
}