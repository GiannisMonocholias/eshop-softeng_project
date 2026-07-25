package gr.softeng.team21.view.admin.deleteEmp.empInfo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Unit testing class that verifies the behavior of the {@link EmpInfoPresenter}.
 * It tests finding specific employees by username and executing the final deletion process.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class EmpInfoPresenterTest {

    private EmpInfoPresenter presenter;
    private EmpInfoViewStub viewStub;
    private EmployeeDAOMemory employeeDAO;
    private UserCredentialsDAOMemory credentialsDAO;

    /**
     * Sets up the test environment, initializing memory DAOs and injecting them into the presenter.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();

        employeeDAO = EmployeeDAOMemory.getInstance();
        credentialsDAO = UserCredentialsDAOMemory.getInstance();
        viewStub = new EmpInfoViewStub();

        presenter = new EmpInfoPresenter(viewStub, employeeDAO, credentialsDAO);
    }

    /**
     * Verifies that the presenter fetches the correct employee details when a valid username is provided.
     */
    @Test
    public void testLoadEmployeeDetailsSuccessfully() {
        // Fetch an existing employee directly from the initialized memory to guarantee a valid test target
        Employee targetEmployee = employeeDAO.getEmployees().join().values().iterator().next();
        String validUsername = targetEmployee.getUsername();

        presenter.loadEmployeeDetails(validUsername);

        Employee displayedEmployee = viewStub.getDisplayedEmployee();
        assertNotNull("Displayed employee should not be null", displayedEmployee);
        assertEquals("The usernames should match", validUsername, displayedEmployee.getUsername());
        assertNull("No error message should be shown", viewStub.getErrorMessage());
    }

    /**
     * Verifies that the presenter handles non-existent users gracefully by triggering a view error.
     */
    @Test
    public void testLoadEmployeeDetailsFailsForInvalidUsername() {
        presenter.loadEmployeeDetails("invalid_username_12345");

        assertNull("Displayed employee should be null", viewStub.getDisplayedEmployee());
        assertNotNull("An error message should be displayed", viewStub.getErrorMessage());
        assertEquals("Ο υπάλληλος δεν βρέθηκε.", viewStub.getErrorMessage());
    }

    /**
     * Verifies the core deletion logic: ensuring the employee is removed from the DAO
     * and the view is instructed to close.
     */
    @Test
    public void testExecuteDeletionRemovesEmployeeAndClosesScreen() {
        // Fetch an existing employee to delete
        Employee employeeToDelete = employeeDAO.getEmployees().join().values().iterator().next();
        String usernameToDelete = employeeToDelete.getUsername();

        // Perform the deletion
        presenter.executeDeletion(employeeToDelete);

        // Verify view behavior
        assertTrue("The screen should be closed after successful deletion", viewStub.isScreenClosed());

        // Verify DAO state (Ensure the employee no longer exists)
        boolean exists = false;
        for (Employee emp : employeeDAO.getEmployees().join().values()) {
            if (emp.getUsername().equals(usernameToDelete)) {
                exists = true;
                break;
            }
        }
        assertFalse("Employee should be completely removed from the DAO", exists);
    }
}