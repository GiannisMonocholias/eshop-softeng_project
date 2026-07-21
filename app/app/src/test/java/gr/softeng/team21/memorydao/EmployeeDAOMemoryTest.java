package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.concurrent.CompletionException;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.EmployeeState;

/**
 * Unit tests for the {@link EmployeeDAOMemory} class.
 * This test suite ensures the integrity of the in-memory employee repository,
 * verifying singleton behavior, asynchronous CRUD operations via CompletableFuture,
 * and accurate exception handling for invalid data interactions.
 * @author Γιάννης Μονοχολιάς
 */
public class EmployeeDAOMemoryTest {

    private EmployeeDAOMemory repo;
    private Employee employee;

    /**
     * Sets up the testing environment before each individual test case.
     * Initializes the singleton repository, instantiates a sample employee,
     * and ensures the repository starts with controlled initial data.
     * The join() method is used to block and wait for the asynchronous insertion to finish.
     */
    @Before
    public void setUp() {
        repo = EmployeeDAOMemory.getInstance();

        employee = new Employee("Α001", "Alex","123" ,"Drakakis", "69696969", new EmailAddress("alexd@gmail.com") , "3232" , 1000 , 1500 , 40 , EmployeeState.ACTIVE , new Date() );
        repo.addEmployee(employee).join();
    }

    /**
     * Verifies that the {@link EmployeeDAOMemory} correctly implements the Singleton design pattern.
     * Confirms that consecutive calls to getInstance() return the exact same memory reference.
     */
    @Test
    public void testGetInstanceReturnsSameObject() {
        EmployeeDAOMemory inst1 = EmployeeDAOMemory.getInstance();
        EmployeeDAOMemory inst2 = EmployeeDAOMemory.getInstance();

        assertSame(inst1, inst2);
    }

    /**
     * Verifies that the sample employee added during the setup phase is successfully
     * stored and correctly indexed by their unique Employee ID.
     */
    @Test
    public void testEmployeeAddedOnConstructionSuccess() {
        HashMap<String, Employee> map = repo.getEmployees().join();

        assertTrue(map.containsKey(employee.getEmployeeId()));
        assertEquals(employee, map.get(employee.getEmployeeId()));
    }

    /**
     * Verifies that attempting to add a null employee reference is rejected.
     * Expects an IllegalArgumentException wrapped inside a {@link CompletionException}
     * due to the CompletableFuture architecture.
     */
    @Test(expected = CompletionException.class)
    public void testAddEmployeeNullThrowsException() {
        repo.addEmployee(null).join();
    }

    /**
     * Verifies that the repository prevents duplicate entries of the same employee object.
     * Expects an exception wrapped inside a {@link CompletionException} when adding an already existing employee.
     */
    @Test(expected = CompletionException.class)
    public void testAddExistingEmployeeThrowsException() {
        repo.addEmployee(employee).join();
    }

    /**
     * Tests the successful removal of an existing employee from the repository.
     * Ensures the employee is no longer retrievable after the deletion operation completes.
     */
    @Test
    public void testRemoveEmployeeSuccess() {
        repo.removeEmployee(employee).join();

        assertFalse(repo.getEmployees().join().containsKey(employee.getEmployeeId()));
    }

    /**
     * Verifies that attempting to remove a null employee reference is correctly rejected.
     * Expects an exception wrapped inside a {@link CompletionException}.
     */
    @Test(expected = CompletionException.class)
    public void testRemoveEmployeeNullThrowsException() {
        repo.removeEmployee(null).join();
    }

    /**
     * Verifies that attempting to remove an employee that is not present in the repository
     * results in a failure. Expects an exception wrapped inside a {@link CompletionException}.
     */
    @Test(expected = CompletionException.class)
    public void testRemoveEmployeeNotInRepositoryThrowsException() {
        repo.removeEmployee(employee).join();
        repo.removeEmployee(employee).join(); // The second one will fail
    }

    /**
     * Clears the repository synchronously after each test case execution.
     * Ensures state isolation and prevents data leakage between independent tests.
     */
    @After
    public void tearDown(){
        repo.clear().join();
    }
}