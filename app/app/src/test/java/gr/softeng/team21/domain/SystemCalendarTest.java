package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;

public class SystemCalendarTest {
    private static SystemCalendar calendar;

    @Before
    public void setUp() {

        calendar = SystemCalendar.getInstance();
        calendar.setEmailProviderStub(new EmailDAOMemory()); // καθαρό stub για κάθε test

        OrderDAOMemory.getInstance().clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }

    @Test
    public void getInstanceReturnsSameReferencesTest() {
        SystemCalendar cal2 = SystemCalendar.getInstance();
        assertSame(calendar, cal2);
    }

    @Test
    public void getSetCurrentDateTest() {
        Date now = new Date();
        calendar.setCurrentdate(now);
        assertEquals(now, calendar.getCurrnetdate());
    }

    @Test
    public void getSetEmailAddressTest() {
        EmailAddress addr = new EmailAddress("test@system.com");
        calendar.setEmailAddress(addr);
        assertEquals(addr, calendar.getEmailAddress());
    }

    @Test
    public void sendEmailStoresInInboxAndSentTest() {
        User recipient = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@mail.com"), "CUST-001", new Date());
        recipient.setEmailProvider(new EmailDAOMemory());

        calendar.sendEmail(calendar, recipient, "Subject", "Body");

        assertEquals(1, recipient.getEmailProvider().getInboxEmails().size());
        assertEquals(1, calendar.getEmailProviderStub().getSentEmails().size());

        EmailMessage inboxMsg = recipient.getEmailProvider().getInboxEmails().get(0);
        assertEquals("Subject", inboxMsg.getSubject());
        assertEquals("Body", inboxMsg.getBody());
    }

    @Test
    public void DeliverEmailMarksOriginalRepliedTest() {
        User recipient = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@mail.com"), "Customer1", new Date());
        recipient.setEmailProvider(new EmailDAOMemory());

        EmailMessage original = new EmailMessage();
        original.setReplied(false);

        calendar.deliverEmail(calendar, recipient, original, "Reply", "Body", true);

        assertTrue(original.isReplied());
    }

    @Test
    public void monitorOrdersTriggersNotifyCustomerDelayTest() {
        OrderDAOMemory repo = OrderDAOMemory.getInstance();

        Customer customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@mail.com"), "CUST-001", new Date());
        customer.setEmailProvider(new EmailDAOMemory());

        ShoppingCart shoppingCart = new ShoppingCart(customer);
        Order delayedOrder = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH,
                new Date(), shoppingCart);
        delayedOrder.setOrderstatus(StatusType.DELAYED);

        repo.addOrder(delayedOrder);

        calendar.monitorOrders();

        // The customer must receive the emails
        assertEquals(1, customer.getEmailProvider().getInboxEmails().size());
        EmailMessage msg = customer.getEmailProvider().getInboxEmails().get(0);
        assertTrue(msg.getSubject().contains("ORDER DELAY"));
        assertTrue(msg.getBody().contains("Delay!"));
    }

    @Test
    public void monitorOrdersTriggersNotifyCustomerReadyTest(){
        OrderDAOMemory repo = OrderDAOMemory.getInstance();

        Customer customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@mail.com"), "CUST-001", new Date());
        customer.setEmailProvider(new EmailDAOMemory());

        ShoppingCart shoppingCart = new ShoppingCart(customer);
        Order delayedOrder = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH,
                new Date(), shoppingCart);


        delayedOrder.setOrderstatus(StatusType.SHIPPED);

        repo.addOrder(delayedOrder);

        calendar.monitorOrders();

        // The customer must receive the emails
        assertEquals(1, customer.getEmailProvider().getInboxEmails().size());
        EmailMessage msg = customer.getEmailProvider().getInboxEmails().get(0);
        assertTrue(msg.getSubject().contains("ORDER READY"));
        assertTrue(msg.getBody().contains("is now ready"));
    }

    @After
    public void tearDown() {
        calendar.getEmailProviderStub().getInboxEmails().clear();
        calendar.getEmailProviderStub().getSentEmails().clear();
        OrderDAOMemory.getInstance().clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }

}