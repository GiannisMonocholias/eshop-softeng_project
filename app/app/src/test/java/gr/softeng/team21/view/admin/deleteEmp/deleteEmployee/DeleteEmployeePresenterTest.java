package gr.softeng.team21.view.admin.deleteEmp.deleteEmployee;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.admin.deleteEmp.DeleteEmployeeViewStub;

/**
 * Unit testing class that verifies the behavior of the {@link DeleteEmployeePresenter}.
 * It ensures that employees are correctly fetched and forwarded to the view.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class DeleteEmployeePresenterTest {

    private DeleteEmployeePresenter presenter;
    private DeleteEmployeeViewStub viewStub;
    private EmployeeDAOMemory employeeDAO;

    /**
     * Sets up the test environment before each test method execution.
     * Initializes in-memory mock data, creates a view stub, and instantiates
     * the presenter with Dependency Injection.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();

        employeeDAO = EmployeeDAOMemory.getInstance();
        viewStub = new DeleteEmployeeViewStub();

        presenter = new DeleteEmployeePresenter(viewStub, employeeDAO);
    }

    /**
     * Tests that the presenter correctly loads all available employees asynchronously.
     * Verifies that the list is passed to the view without errors.
     */
    @Test
    public void loadEmployeesSuccessfullyPopulatesView() {
        // Asynchronous call, executes instantly using MemoryDAO's CompletableFutures
        presenter.loadEmployees();

        List<Employee> employees = viewStub.getLoadedEmployees();

        assertNotNull("Employees list should not be null", employees);
        assertFalse("Employees list should not be empty", employees.isEmpty());
        assertNull("There should be no error message during successful fetch", viewStub.getErrorMessage());
    }
}