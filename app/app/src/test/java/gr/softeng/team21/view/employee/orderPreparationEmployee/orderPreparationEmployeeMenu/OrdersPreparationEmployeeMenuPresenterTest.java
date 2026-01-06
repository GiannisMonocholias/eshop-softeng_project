package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class OrdersPreparationEmployeeMenuPresenterTest {

    private OrdersPreparationEmployeeMenuPresenter presenter;
    private OrderPreparationEmployeeMenuViewStub viewStub;
    private EmployeeDAO employeeDAO;

    private static final String EMPLOYEE_ID = "PREP-201";

    @Before
    public void setUp() {
        MemoryInitializer.prepareData();

        viewStub = new OrderPreparationEmployeeMenuViewStub();
        employeeDAO = EmployeeDAOMemory.getInstance();
        presenter = new OrdersPreparationEmployeeMenuPresenter(viewStub, employeeDAO);
    }


    @Test
    public void onViewCreatedShowsCorrectName() {
        presenter.onViewCreated(EMPLOYEE_ID);
        Assert.assertEquals("Γιώργος Νικολάου", viewStub.getShownName());
    }

    @Test
    public void onClickAssignedOrdersNavigatesCorrectly() {
        presenter.onClickAssignedOrders(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedAssignedOrdersId());
    }

    @Test
    public void onClickAvailableOrdersToAssignNavigatesCorrectly() {
        presenter.onClickAvailableOrdersToAssign(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedAvailableOrdersId());
    }

    @Test
    public void onProcessAccountSelectedNavigatesCorrectly() {
        presenter.onProcessAccountSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedProcessAccountId());
    }

    @Test
    public void onDeleteAccountSelectedShowsConfirmationDialog() {
        presenter.onDeleteAccountSelected();
        Assert.assertTrue(viewStub.isDeleteConfirmationShown());
    }

    @Test(expected = SecurityException.class)
    public void onDeleteAccountConfirmedRemovesEmployeeAndNavigates() {
        Assert.assertNotNull(employeeDAO.getEmployee(EMPLOYEE_ID));
        Assert.assertSame(employeeDAO.getEmployee(EMPLOYEE_ID),UserCredentialsDAOMemory.getInstance().validateAndGetUser("g_nikolaou","pass1240"));

        presenter.onDeleteAccountConfirmed(EMPLOYEE_ID);

        Assert.assertEquals("Ο λογαριασμός διαγράφηκε επιτυχώς.", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.isNavigateToLoginCalled());


        Assert.assertNull(employeeDAO.getEmployee(EMPLOYEE_ID));

        UserCredentialsDAOMemory.getInstance().validateAndGetUser("g_nikolaou", "pass1240");
        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.", viewStub.getMessageShown());

    }

    @Test
    public void onDeleteAccountConfirmedShowsMessageForNonExistingId(){
        presenter.onDeleteAccountConfirmed("Non_existing_id");
        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.",viewStub.getMessageShown());
    }
}