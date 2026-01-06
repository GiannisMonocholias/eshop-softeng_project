package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class UpdateCatalogueEmployeeMenuPresenterTest {

    private UpdateCatalogueEmployeeMenuPresenter presenter;
    private UpdateCatalogueEmployeeMenuViewStub viewStub;
    private EmployeeDAO employeeDAO;

    private static final String EMPLOYEE_ID = "CAT-301";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new UpdateCatalogueEmployeeMenuViewStub();
        employeeDAO = EmployeeDAOMemory.getInstance();
        presenter = new UpdateCatalogueEmployeeMenuPresenter(viewStub, employeeDAO);
    }


    @Test
    public void onViewCreatedShowsCorrectName() {
        presenter.onViewCreated(EMPLOYEE_ID);
        Assert.assertEquals("Δήμητρα Γεωργίου", viewStub.getShownName());
    }

    @Test
    public void onClickAssignedRequestsNavigatesCorrectly() {
        presenter.onClickAssignedRequests(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedAssignedRequestsId());
    }

    @Test
    public void onProcessAccountSelectedNavigatesCorrectly() {
        presenter.onProcessAccountSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedProcessAccountId());
    }

    @Test
    public void onClickAvailableRequestsToAssignNavigatesCorrectly() {
        presenter.onClickAvailableRequestsToAssign(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedAvailableRequestsId());
    }

    @Test
    public void onDeleteAccountSelectedShowsConfirmation() {
        presenter.onDeleteAccountSelected();
        Assert.assertTrue(viewStub.isDeleteConfirmationShown());
    }

    @Test(expected = SecurityException.class)
    public void onDeleteAccountConfirmedRemovesEmployeeAndNavigates() {
        Assert.assertNotNull(employeeDAO.getEmployee(EMPLOYEE_ID));
        Assert.assertSame(employeeDAO.getEmployee(EMPLOYEE_ID), UserCredentialsDAOMemory.getInstance().validateAndGetUser("d_georgiou", "pass1243"));

        presenter.onDeleteAccountConfirmed(EMPLOYEE_ID);

        Assert.assertEquals("Ο λογαριασμός διαγράφηκε επιτυχώς.", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.isNavigateToLoginCalled());

        Assert.assertNull(employeeDAO.getEmployee(EMPLOYEE_ID));


        UserCredentialsDAOMemory.getInstance().validateAndGetUser("d_georgiou", "pass1243");

    }

    @Test
    public void onDeleteAccountConfirmedInvalidId_ShowsErrorMessage() {
        presenter.onDeleteAccountConfirmed("INVALID_ID");


        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.", viewStub.getMessageShown());

        Assert.assertFalse(viewStub.isNavigateToLoginCalled());
    }
}