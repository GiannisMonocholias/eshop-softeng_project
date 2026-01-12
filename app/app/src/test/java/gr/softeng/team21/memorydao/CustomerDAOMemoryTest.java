package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

import static org.junit.Assert.*;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.TestHelper;

/**
 * Unit tests for the {@link CustomerDAOMemory} class.
 * This suite verifies the in-memory persistence logic for customer entities,
 * ensuring correct CRUD operations and handling of edge cases like duplicate or null entries.
 * @author PAVLOS GRATSANIS
 */
public class CustomerDAOMemoryTest {

    private CustomerDAOMemory customerDAOMemory;
    private Customer customer;
    private EmailAddress email;

    /**
     * Initializes the testing environment before each test.
     * Sets up the DAO singleton instance, clears existing data, and prepares
     * a sample customer entry.
     * @throws Exception if setup fails.
     */
    @Before
    public void setUp ( ) throws Exception {
        customerDAOMemory = CustomerDAOMemory.getInstance ( );
        customerDAOMemory.getCustomers ( ).clear ( );
        email = TestHelper.getEmail ( );
        customer = new Customer (
                "user1", "John", "pass1", "Doe",
                "1234567890", email, "CUST-001", new Date( ) );
        customerDAOMemory.addCustomer(customer);
    }

    /**
     * Cleans up the in-memory customer map after each test to ensure isolation.
     * @throws Exception if teardown fails.
     */
    @After
    public void tearDown ( ) throws Exception {
        customerDAOMemory.getCustomers ( ).clear ( );
    }

    /**
     * Verifies that the internal customer map is correctly retrieved and contains
     * the expected initial data.
     */
    @Test
    public void getCustomers ( ) {
        HashMap<String, Customer> customers = customerDAOMemory.getCustomers ( );
        assertEquals ( 1, customers.size ( ) );
        assertTrue ( customers.containsKey ( "CUST-001" ) );
    }

    /**
     * Tests the successful addition of a new customer to the repository.
     */
    @Test
    public void addCustomer ( ) {
        EmailAddress email2 = new EmailAddress ( "test2@mail.com" );
        Customer customer2 = new Customer (
                "user2", "Jane", "pass2", "Doe",
                "0987654321", email2, "CUST-002", new Date ( ) );
        customerDAOMemory.addCustomer(customer2);

        assertEquals ( 2, customerDAOMemory.getCustomers ( ).size ( ) );

    }

    /**
     * Verifies that attempting to add a null customer results in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addCustomerNull ( ) {
        customerDAOMemory.addCustomer ( null );
    }

    /**
     * Verifies that the repository prevents adding a customer with an ID that already exists.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addCustomerDuplicateId ( ) {
        customerDAOMemory.addCustomer ( customer );
    }

    /**
     * Tests the successful removal of an existing customer from the repository.
     */
    @Test
    public void removeCustomer ( ) {
        customerDAOMemory.removeCustomer ( customer );
        assertEquals ( 0, customerDAOMemory.getCustomers ( ).size ( ) );

    }

    /**
     * Verifies that attempting to remove a null customer reference throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeCustomerNull ( ) {
        customerDAOMemory.removeCustomer ( null );
    }

    /**
     * Verifies that attempting to remove a customer that does not exist in the repository
     * results in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeCustomerNotExists ( ) {
        customerDAOMemory.getCustomers ( ).clear ( );
        customerDAOMemory.removeCustomer ( customer );
    }

    /**
     * Final cleanup of the singleton instance's data after all tests in the class have finished.
     */
    @AfterClass
    public static void tearDownAfterClass () {
        CustomerDAOMemory.getInstance ( ).getCustomers ( ).clear ( );
    }
}