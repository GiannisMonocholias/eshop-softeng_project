package gr.softeng.team21.view.user.EditData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for the unified {@link UserEditDataPresenter}.
 * Verifies asynchronous loading, complex validation logic, state saving, and unsaved changes detection.
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataPresenterTest {

    private UserEditDataPresenter presenter;
    private UserEditDataViewStub viewStub;
    private Customer testCustomer;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        CustomerDAO customerDAO = CustomerDAOMemory.getInstance();
        EmployeeDAO employeeDAO = EmployeeDAOMemory.getInstance();

        testCustomer = customerDAO.getCustomer("CUST-500").join();

        viewStub = new UserEditDataViewStub();
        presenter = new UserEditDataPresenter(viewStub, customerDAO, employeeDAO);
    }

    @Test
    public void loadUserDataPopulatesViewCorrectly() {
        presenter.loadUserData("CUST-500");

        Assert.assertEquals(testCustomer.getUsername(), viewStub.getUsername());
        Assert.assertEquals(testCustomer.getPassword(), viewStub.getPassword());
    }

    @Test
    public void onSaveClickedWithEmptyRequiredFieldsShowsError() {
        presenter.loadUserData("CUST-500");

        presenter.onSaveClicked("", "", "", "Name", "Surname", "6999999999",
                "Street", "1", "City", "12345", "Greece");

        Assert.assertEquals("Συμπληρώστε τα υποχρεωτικά πεδία (Όνομα Χρήστη, Κωδικός, Email).", viewStub.getMessage());
    }

    @Test
    public void onSaveClickedWithValidDataSavesAndFinishes() {
        presenter.loadUserData("CUST-500");

        presenter.onSaveClicked("NewUser", "NewPass123", "new@mail.com", "Nick", "Georgiou", "6911111111",
                "Ermou", "10", "Athens", "10000", "Greece");

        Assert.assertEquals("Τα στοιχεία σας ενημερώθηκαν επιτυχώς!", viewStub.getMessage());
        Assert.assertTrue(viewStub.isFinishCalled());

        // Verify domain object was updated
        Assert.assertEquals("NewUser", testCustomer.getUsername());
        Assert.assertEquals("NewPass123", testCustomer.getPassword());
        Assert.assertEquals("6911111111", testCustomer.getPhonenumber());
    }

    @Test
    public void onBackPressedWithUnsavedChangesTriggersDialog() {
        presenter.loadUserData("CUST-500"); // Original state recorded

        presenter.onBackPressed("ChangedUser", testCustomer.getPassword(), "new@email.com",
                testCustomer.getFirstname(), testCustomer.getLastname(), testCustomer.getPhonenumber(),
                "Street", "1", "City", "12345", "Greece");

        Assert.assertTrue(viewStub.isUnsavedDialogCalled());
    }

    @Test
    public void onBackPressedWithoutChangesFinishesSafely() {
        presenter.loadUserData("CUST-500");

        presenter.onBackPressed(testCustomer.getUsername(), testCustomer.getPassword(), testCustomer.getEmailAddress().toString(),
                testCustomer.getFirstname(), testCustomer.getLastname(), testCustomer.getPhonenumber(),
                testCustomer.getAddress().getStreet(), testCustomer.getAddress().getNumber(), testCustomer.getAddress().getCity(),
                testCustomer.getAddress().getZipcode(), testCustomer.getAddress().getCountry());

        Assert.assertTrue(viewStub.isFinishCalled());
        Assert.assertFalse(viewStub.isUnsavedDialogCalled());
    }
}