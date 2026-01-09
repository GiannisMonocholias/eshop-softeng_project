package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link CustomerServiceEmployee} class.
 * This suite verifies the employee's ability to handle customer communications,
 * including order delay notifications, order readiness notifications, and
 * replying to customer inquiries.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeTest {
    private CustomerServiceEmployee employee;
    private  Customer customer;
    private Order order;

    /**
     * Sets up the testing environment before each test.
     * Initializes a Customer Service Employee, a Customer, and an Order.
     * Also populates the employee's inbox and sent folder with sample emails.
     */
    @Before
    public void setUp() {
        EmployeeDAOMemory.getInstance().clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();

        employee = new CustomerServiceEmployee("GP","Giorgos","abcd123",
                "Papadopoulos","3029761482",new EmailAddress("GP@gmail.com"),
                "CS_1",100,1000,8,EmployeeState.ACTIVE, new Date(3,5,2025));
        employee.setEmailProvider(new EmailDAOMemory());

        customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        customer.setEmailProvider(new EmailDAOMemory());

        order = new Order("order1246", new Date(), OrderStatusType.NEW,
                false, PaymentType.CASH, new Date(), new ShoppingCart());
        employee.setEmailProvider(new EmailDAOMemory());

        EmailMessage e1 = new EmailMessage(
                new EmailAddress("customer1@example.com"),
                new EmailAddress("agent@example.com"),
                "Παραγγελία #1234",
                "Καλησπέρα, θα ήθελα ενημέρωση για την παραγγελία μου.",
                new Date(10,12,2025)
        );

        EmailMessage e2 = new EmailMessage(
                new EmailAddress("customer2@example.com"),
                new EmailAddress("agent@example.com"),
                "Πρόβλημα με το προϊόν",
                "Το προϊόν που παρέλαβα έχει ελάττωμα. Τι μπορώ να κάνω;",
                new Date()
        );

        EmailMessage e3 = new EmailMessage(
                new EmailAddress("customer3@example.com"),
                new EmailAddress("agent@example.com"),
                "Ευχαριστήριο μήνυμα",
                "Ευχαριστώ για την άμεση εξυπηρέτηση!",
                new Date(8,12,2025)
        );
        employee.emailDAOMemory.saveInboxEmails(e1);
        employee.emailDAOMemory.saveInboxEmails(e2);
        employee.emailDAOMemory.saveInboxEmails(e3);

        EmailMessage sent1 = new EmailMessage(
                new EmailAddress("agent@example.com"),
                new EmailAddress("customer1@example.com"),
                "Απάντηση στην παραγγελία #1234",
                "Καλησπέρα σας, η παραγγελία σας είναι σε διαδικασία αποστολής.",
                new Date(20,12,2025)
        );
        EmailMessage sent2 = new EmailMessage(
                new EmailAddress("agent@example.com"),
                new EmailAddress("customer2@example.com"),
                "Οδηγίες για επιστροφή προϊόντος",
                "Μπορείτε να επιστρέψετε το προϊόν εντός 14 ημερών με δωρεάν μεταφορικά.",
                new Date(21,12,2025)
        );
        EmailMessage sent3 = new EmailMessage(
                new EmailAddress("agent@example.com"),
                new EmailAddress("customer3@example.com"),
                "Ευχαριστούμε για το μήνυμά σας",
                "Χαιρόμαστε που μείνατε ικανοποιημένος από την εξυπηρέτηση. Είμαστε πάντα στη διάθεσή σας.",
                new Date(18,12,2025)
        );
        employee.emailDAOMemory.saveSentEmails(sent1);
        employee.emailDAOMemory.saveSentEmails(sent2);
        employee.emailDAOMemory.saveSentEmails(sent3);
    }

    /**
     * Verifies that notifyCustomerDelay throws NullPointerException when the order is null.
     */
    @Test(expected = NullPointerException.class)
    public void notifyCustomerDelay_NullOrderTest(){
        employee.notifyCustomerDelay(null, customer);
    }

    /**
     * Verifies that notifyCustomerDelay throws NullPointerException when the customer is null.
     */
    @Test(expected = NullPointerException.class)
    public void notifyCustomerDelay_NullCustomerTest(){
        Order tempOrder = new Order("order1246", new Date(), OrderStatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        employee.notifyCustomerDelay(tempOrder, null);
    }

    /**
     * Verifies that notifyCustomerDelay executes successfully with valid parameters.
     */
    @Test
    public void notifyCustomerDelaySuccessTest(){
        employee.notifyCustomerDelay(order, customer);
    }

    /**
     * Verifies that notifyCustomerReady throws NullPointerException when the order is null.
     */
    @Test(expected = NullPointerException.class)
    public void notifyCustomerReady_NullOrderTest(){
        employee.notifyCustomerReady(null, customer);
    }

    /**
     * Verifies that notifyCustomerReady throws NullPointerException when the customer is null.
     */
    @Test(expected = NullPointerException.class)
    public void notifyCustomerReady_NullCustomerTest(){
        Order tempOrder = new Order("order1246", new Date(), OrderStatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        employee.notifyCustomerReady(tempOrder, null);
    }

    /**
     * Verifies that notifyCustomerReady executes successfully with valid parameters.
     */
    @Test
    public void notifyCustomerReadySuccessTest(){
        employee.notifyCustomerReady(order, customer);
    }

    /**
     * Verifies that replyToCustomerInquiry throws NullPointerException when the body is null.
     */
    @Test(expected = NullPointerException.class)
    public void replyToCustomerInquiry_NullBodyTest(){
        employee.replyToCustomerInquiry(customer, new EmailMessage(),null);
    }

    /**
     * Verifies that replyToCustomerInquiry throws NullPointerException when the original email is null.
     */
    @Test(expected = NullPointerException.class)
    public void replyToCustomerInquiry_NullOriginalEmailTest(){
        employee.replyToCustomerInquiry(customer,null,"Response body");
    }

    /**
     * Verifies that replyToCustomerInquiry throws NullPointerException when the customer is null.
     */
    @Test(expected = NullPointerException.class)
    public void replyToCustomerInquiry_NullCustomerTest(){
        employee.replyToCustomerInquiry(null,new EmailMessage(),"Response body");
    }

    /**
     * Verifies that replyToCustomerInquiry executes successfully with valid parameters.
     */
    @Test
    public void replyToCustomerInquirySuccessTest(){
        employee.replyToCustomerInquiry(customer, new EmailMessage(),"Response body");
    }

    /**
     * Cleans up the repositories after each test to ensure test isolation.
     */
    @After
    public void tearDownTest(){
        EmployeeDAOMemory.getInstance().clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}