package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class CustomerServiceEmployeeTest {
    private CustomerServiceEmployee employee;
    private  Customer customer;
    private Order order;


    @Before
    public void setUp() {
        EmployeeDAOMemory.getInstance().clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();

        employee = new CustomerServiceEmployee("GP","Giorgos","abcd123",
                "Papadopoulos","3029761482",new EmailAddress("GP@gmail.com"),
                "CS_1",100,1000,8,EmployeeState.ACTIVE, new Date(3,5,2025));
        employee.setEmailProviderStub(new EmailDAOMemory());

        customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@gmail.com"), "CUST-001", new Date());
        customer.setEmailProviderStub(new EmailDAOMemory());

        order = new Order("order1246", new Date(), StatusType.NEW,
                false, PaymentType.CASH, new Date(), new ShoppingCart());
        employee.setEmailProviderStub(new EmailDAOMemory());

        EmailMessage e1 = new EmailMessage(
                new EmailAddress("customer1@example.com"),
                new EmailAddress("agent@example.com"),
                "Παραγγελία #1234",
                "Καλησπέρα, θα ήθελα ενημέρωση για την παραγγελία μου."
        );

        EmailMessage e2 = new EmailMessage(
                new EmailAddress("customer2@example.com"),
                new EmailAddress("agent@example.com"),
                "Πρόβλημα με το προϊόν",
                "Το προϊόν που παρέλαβα έχει ελάττωμα. Τι μπορώ να κάνω;"
        );

        EmailMessage e3 = new EmailMessage(
                new EmailAddress("customer3@example.com"),
                new EmailAddress("agent@example.com"),
                "Ευχαριστήριο μήνυμα",
                "Ευχαριστώ για την άμεση εξυπηρέτηση!"
        );
        employee.emailDAOMemory.saveInboxEmails(e1);
        employee.emailDAOMemory.saveInboxEmails(e2);
        employee.emailDAOMemory.saveInboxEmails(e3);

        EmailMessage sent1 = new EmailMessage(
                new EmailAddress("agent@example.com"),
                new EmailAddress("customer1@example.com"),
                "Απάντηση στην παραγγελία #1234",
                "Καλησπέρα σας, η παραγγελία σας είναι σε διαδικασία αποστολής."
        );
        EmailMessage sent2 = new EmailMessage(
                new EmailAddress("agent@example.com"),
                new EmailAddress("customer2@example.com"),
                "Οδηγίες για επιστροφή προϊόντος",
                "Μπορείτε να επιστρέψετε το προϊόν εντός 14 ημερών με δωρεάν μεταφορικά."
        );
        EmailMessage sent3 = new EmailMessage(
                new EmailAddress("agent@example.com"),
                new EmailAddress("customer3@example.com"),
                "Ευχαριστούμε για το μήνυμά σας",
                "Χαιρόμαστε που μείνατε ικανοποιημένος από την εξυπηρέτηση. Είμαστε πάντα στη διάθεσή σας."
        );
        employee.emailDAOMemory.saveSentEmails(sent1);
        employee.emailDAOMemory.saveSentEmails(sent2);
        employee.emailDAOMemory.saveSentEmails(sent3);
    }


    @Test(expected = NullPointerException.class)
    public void notifyCustomerDelay_NullOrderTest(){
        employee.notifyCustomerDelay(null, customer);
    }

    @Test(expected = NullPointerException.class)
    public void notifyCustomerDelay_NullCustomerTest(){
        Order tempOrder = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        employee.notifyCustomerDelay(tempOrder, null);
    }

    @Test
    public void notifyCustomerDelaySuccessTest(){
        employee.notifyCustomerDelay(order, customer);
    }


    @Test(expected = NullPointerException.class)
    public void notifyCustomerReady_NullOrderTest(){
        employee.notifyCustomerReady(null, customer);
    }

    @Test(expected = NullPointerException.class)
    public void notifyCustomerReady_NullCustomerTest(){
        Order tempOrder = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH, new Date(), new ShoppingCart());
        employee.notifyCustomerReady(tempOrder, null);
    }

    @Test
    public void notifyCustomerReadySuccessTest(){
        employee.notifyCustomerReady(order, customer);
    }



    @Test(expected = NullPointerException.class)
    public void replyToCustomerInquiry_NullBodyTest(){
        employee.replyToCustomerInquiry(customer, new EmailMessage(),null);
    }

    @Test(expected = NullPointerException.class)
    public void replyToCustomerInquiry_NullOriginalEmailTest(){
        employee.replyToCustomerInquiry(customer,null,"Response body");
    }

    @Test(expected = NullPointerException.class)
    public void replyToCustomerInquiry_NullCustomerTest(){
        employee.replyToCustomerInquiry(null,new EmailMessage(),"Response body");
    }

    @Test
    public void replyToCustomerInquirySuccessTest(){
        employee.replyToCustomerInquiry(customer, new EmailMessage(),"Response body");
    }

    @After
    public void tearDownTest(){
        EmployeeDAOMemory.getInstance().clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}