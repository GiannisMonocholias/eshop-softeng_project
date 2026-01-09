package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.EmployeeState;

/**
 * Unit tests for the {@link EmployeeDAOMemory} class.
 * This suite ensures the integrity of the in-memory employee repository,
 * verifying singleton behavior, CRUD operations, and exception handling
 * for invalid data interactions.
 * @author Γιάννης Μονοχολιάς
 */
public class EmployeeDAOMemoryTest {

    private EmployeeDAOMemory repo;
    private Employee employee;

    /**
     * Sets up the testing environment before each test.
     * Initializes the singleton repository, creates a sample employee,
     * and ensures the repository starts with controlled initial data.
     */
    @Before
    public void setUp() {
        repo = EmployeeDAOMemory.getInstance();



        employee = new Employee("Α001", "Alex","123" ,"Drakakis", "69696969", new EmailAddress("alexd@gmail.com") , "3232" , 1000 , 1500 , 40 , EmployeeState.ACTIVE , new Date() );
        EmployeeDAOMemory.getInstance().addEmployee(employee);
    }

    /**
     * Verifies that {@link EmployeeDAOMemory} correctly implements the Singleton pattern,
     * always returning the same memory reference.
     */
    @Test
    public void testGetInstanceReturnsSameObject() {
        EmployeeDAOMemory inst1 = EmployeeDAOMemory.getInstance();
        EmployeeDAOMemory inst2 = EmployeeDAOMemory.getInstance();

        assertSame(inst1, inst2);
    }

    /**
     * Verifies that an employee added during the setup phase is correctly
     * present and indexed by their unique Employee ID.
     */
    @Test
    public void testEmployeeAddedOnConstructionSuccess() {
        HashMap<String, Employee> map = repo.getEmployees();

        assertTrue(map.containsKey(employee.getEmployeeId()));
        assertEquals(employee, map.get(employee.getEmployeeId()));
    }

    /**
     * Verifies that attempting to add a null employee reference results
     * in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEmployeeNullThrowsException() {
        repo.addEmployee(null);
    }

    /**
     * Verifies that the repository prevents duplicate entries of the same
     * employee object, throwing an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddExistingEmployeeThrowsException() {
        repo.addEmployee(employee);
    }

    /**
     * Tests the successful removal of an existing employee from the repository.
     */
    @Test
    public void testRemoveEmployeeSuccess() {
        repo.removeEmployee(employee);

        assertFalse(repo.getEmployees().containsKey(employee.getEmployeeId()));
    }

    /**
     * Verifies that attempting to remove a null employee results
     * in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEmployeeNullThrowsException() {
        repo.removeEmployee(null);
    }

    /**
     * Verifies that attempting to remove an employee that has already been removed
     * (or never existed) results in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEmployeeNotInRepositoryThrowsException() {
        repo.removeEmployee(employee);

        repo.removeEmployee(employee);
    }

    /**
     * Clears the repository after each test to ensure state isolation
     * and prevent data leakage between test cases.
     */
    @After
    public void tearDown(){
        repo.clear();
    }
}