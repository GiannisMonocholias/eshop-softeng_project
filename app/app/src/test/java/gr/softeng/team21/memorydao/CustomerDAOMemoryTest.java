package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.CompletionException;

import static org.junit.Assert.*;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.TestHelper;

/**
 * Unit tests for the {@link CustomerDAOMemory} class.
 * This suite verifies the in-memory persistence logic for customer entities,
 * ensuring correct CRUD operations and handling of edge cases.
 * Utilizes assertThrows to accurately validate CompletableFuture exceptions.
 * @author PAVLOS GRATSANIS
 */
public class CustomerDAOMemoryTest {

    private CustomerDAOMemory customerDAOMemory;
    private Customer customer;
    private EmailAddress email;

    /**
     * Initializes the testing environment before each test.
     * @throws Exception if setup fails.
     */
    @Before
    public void setUp() throws Exception {
        customerDAOMemory = CustomerDAOMemory.getInstance();

        // Clear memory, while waiting for asynchronous completion
        customerDAOMemory.clear().join();

        email = TestHelper.getEmail();
        customer = new Customer(
                "user1", "John", "pass1", "Doe",
                "1234567890", email, "CUST-001", new Date()
        );


        customerDAOMemory.addCustomer(customer).join();
    }

    /**
     * Cleans up the in-memory customer map after each test to ensure isolation.
     * @throws Exception if teardown fails.
     */
    @After
    public void tearDown() throws Exception {
        customerDAOMemory.clear().join();
    }

    /**
     * Verifies that the internal customer map is correctly retrieved and contains
     * the expected initial data.
     */
    @Test
    public void getCustomers() {
        HashMap<String, Customer> customers = customerDAOMemory.getCustomers().join();
        assertEquals(1, customers.size());
        assertTrue(customers.containsKey("CUST-001"));
    }

    /**
     * Tests the successful addition of a new customer to the repository.
     */
    @Test
    public void addCustomer() {
        EmailAddress email2 = new EmailAddress("test2@mail.com");
        Customer customer2 = new Customer(
                "user2", "Jane", "pass2", "Doe",
                "0987654321", email2, "CUST-002", new Date()
        );
        customerDAOMemory.addCustomer(customer2).join();

        assertEquals(2, customerDAOMemory.getCustomers().join().size());
    }

    /**
     * Verifies that attempting to add a null customer results in an Exception.
     * Checks both the wrapper (CompletionException) and the actual cause.
     */
    @Test
    public void addCustomerNull() {
        // Πιάνουμε τον "φάκελο" (CompletionException)
        CompletionException exception = assertThrows(CompletionException.class, () -> {
            customerDAOMemory.addCustomer(null).join();
        });


        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Cannot add null customer.", exception.getCause().getMessage());
    }

    /**
     * Verifies that the repository prevents adding a customer with an ID that already exists.
     */
    @Test
    public void addCustomerDuplicateId() {
        CompletionException exception = assertThrows(CompletionException.class, () -> {
            customerDAOMemory.addCustomer(customer).join();
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Customer with this id already exists.", exception.getCause().getMessage());
    }

    /**
     * Tests the successful removal of an existing customer from the repository.
     */
    @Test
    public void removeCustomer() {
        customerDAOMemory.removeCustomer(customer).join();
        assertEquals(0, customerDAOMemory.getCustomers().join().size());
    }

    /**
     * Verifies that attempting to remove a null customer reference throws an exception.
     */
    @Test
    public void removeCustomerNull() {
        CompletionException exception = assertThrows(CompletionException.class, () -> {
            customerDAOMemory.removeCustomer(null).join();
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Cannot remove null customer.", exception.getCause().getMessage());
    }

    /**
     * Verifies that attempting to remove a customer that does not exist in the repository
     * results in an Exception.
     */
    @Test
    public void removeCustomerNotExists() {
        customerDAOMemory.clear().join();

        CompletionException exception = assertThrows(CompletionException.class, () -> {
            customerDAOMemory.removeCustomer(customer).join();
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Customer with this id does not exist.", exception.getCause().getMessage());
    }

    /**
     * Final cleanup of the singleton instance's data after all tests in the class have finished.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        CustomerDAOMemory.getInstance().clear().join();
    }
}