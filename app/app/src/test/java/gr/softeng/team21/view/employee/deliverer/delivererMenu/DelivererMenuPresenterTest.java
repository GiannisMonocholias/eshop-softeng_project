package gr.softeng.team21.view.employee.deliverer.delivererMenu;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class DelivererMenuPresenterTest {

    private DelivererMenuPresenter presenter;
    private DelivererMenuViewStub viewStub;
    private EmployeeDAO employeeDAO;

    private static final String EMPLOYEE_ID = "DEL-401";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new DelivererMenuViewStub();
        employeeDAO = EmployeeDAOMemory.getInstance();
        presenter = new DelivererMenuPresenter(viewStub, employeeDAO);
    }

    @Test
    public void onViewCreatedShowsCorrectName() {
        presenter.onViewCreated(EMPLOYEE_ID);
        Assert.assertEquals("Νίκος Στάμος", viewStub.getShownName());
    }

    @Test
    public void onViewCreated_InvalidIdDoesNothing() {
        presenter.onViewCreated("INVALID_ID");
        Assert.assertEquals("", viewStub.getShownName());
    }

    @Test
    public void onOrdersListSelectedNavigatesToOrdersList() {
        presenter.onOrdersListSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedOrdersListId());
    }

    @Test
    public void onProcessAccountSelectedNavigatesToEditData() {
        presenter.onProcessAccountSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedProcessAccountId());
    }

    @Test
    public void onDeleteAccountSelectedShowsConfirmationDialog() {
        presenter.onDeleteAccountSelected();
        Assert.assertTrue(viewStub.isDeleteConfirmationShown());
    }

    @Test(expected = SecurityException.class)
    public void onDeleteAccountConfirmedSuccessRemovesUserAndNavigates() {
        Assert.assertNotNull(employeeDAO.getEmployee(EMPLOYEE_ID));
        Assert.assertSame(employeeDAO.getEmployee(EMPLOYEE_ID),UserCredentialsDAOMemory.getInstance().validateAndGetUser("n_stamos","pass1246"));

        presenter.onDeleteAccountConfirmed(EMPLOYEE_ID);

        Assert.assertEquals("Ο λογαριασμός διαγράφηκε επιτυχώς.", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.isNavigateToLoginCalled());


        Assert.assertNull(employeeDAO.getEmployee(EMPLOYEE_ID));

        UserCredentialsDAOMemory.getInstance().validateAndGetUser("n_stamos", "pass1246");
    }

    @Test
    public void onDeleteAccountConfirmedInvalidIdShowsError() {
        presenter.onDeleteAccountConfirmed("INVALID_ID");

        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.", viewStub.getMessageShown());
        Assert.assertFalse(viewStub.isNavigateToLoginCalled());
    }
}