package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeMenu;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu.CustomerServiceMenuPresenter;

public class CustomerServiceMenuPresenterTest {

    private CustomerServiceMenuPresenter presenter;
    private CustomerServiceEmployeeMenuViewStub viewStub;
    private EmployeeDAO employeeDAO;
    private static final String EMPLOYEE_ID = "CSR-101";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new CustomerServiceEmployeeMenuViewStub();
        employeeDAO = EmployeeDAOMemory.getInstance();
        presenter = new CustomerServiceMenuPresenter(viewStub, employeeDAO);
    }


    @Test
    public void onViewCreatedShowsCorrectName() {
        presenter.onViewCreated(EMPLOYEE_ID);

        Assert.assertEquals("Μαρία Αλεξάνδρου", viewStub.getShownEmployeeName());
    }

    @Test
    public void onViewCreatedInvalidIdDoesNothing() {
        presenter.onViewCreated("NON_EXISTENT_ID");

        Assert.assertEquals("", viewStub.getShownEmployeeName());
    }

    @Test
    public void onInboxSelectedNavigatesToInbox() {
        presenter.onInboxSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigateToInboxId());
    }

    @Test
    public void onOrderStatusSelectedNavigatesToOrderStatus() {
        presenter.onOrderStatusSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigateToOrderStatusId());
    }

    @Test
    public void onProcessAccountSelectedNavigatesToProcessAccount() {
        presenter.onProcessAccountSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigateToProcessAccountId());
    }

    @Test
    public void onDeleteAccountSelectedShowsConfirmation() {
        presenter.onDeleteAccountSelected();
        Assert.assertTrue(viewStub.isDeleteConfirmationShown());
    }

    @Test(expected = SecurityException.class)
    public void onDeleteAccountConfirmedDeletesEmployeeAndCredentialsSuccess() {
        Employee empBefore = employeeDAO.getEmployee(EMPLOYEE_ID);
        Assert.assertNotNull(empBefore);
        Assert.assertSame(empBefore,UserCredentialsDAOMemory.getInstance().validateAndGetUser(empBefore.getUsername(),empBefore.getPassword()));

        // existing employeeId
        presenter.onDeleteAccountConfirmed(EMPLOYEE_ID);


        Assert.assertEquals("Ο λογαριασμός διαγράφηκε επιτυχώς.", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.isNavigateToLoginCalled());


        Assert.assertNull(employeeDAO.getEmployee(EMPLOYEE_ID));


        UserCredentialsDAOMemory.getInstance().validateAndGetUser(empBefore.getUsername(),empBefore.getPassword());
    }

    @Test
    public void onDeleteAccountConfirmedDeletesEmployeeAndCredentialsFailure() {
        // Non existing employeeId
        presenter.onDeleteAccountConfirmed("Non_existing_id");
        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.",viewStub.getMessageShown());

    }
}


