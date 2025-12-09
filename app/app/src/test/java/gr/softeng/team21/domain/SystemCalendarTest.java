package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SystemCalendarTest {
    private static SystemCalendar calendar;

    @Before
    public void setUp() {

        calendar = SystemCalendar.getInstance();
        calendar.setEmailProviderStub(new EmailProviderStub()); // καθαρό stub για κάθε test

        OrdersRepository.getInstance().clear();
        CustomerRepository.getInstance().getCustomers().clear();
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
        recipient.setEmailProviderStub(new EmailProviderStub());

        calendar.sendEmail(calendar, recipient, "Subject", "Body");

        assertEquals(1, recipient.getEmailProviderStub().getInboxEmails().size());
        assertEquals(1, calendar.getEmailProviderStub().getSentEmails().size());

        EmailMessage inboxMsg = recipient.getEmailProviderStub().getInboxEmails().get(0);
        assertEquals("Subject", inboxMsg.getSubject());
        assertEquals("Body", inboxMsg.getBody());
    }

    @Test
    public void DeliverEmailMarksOriginalRepliedTest() {
        User recipient = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@mail.com"), "Customer1", new Date());
        recipient.setEmailProviderStub(new EmailProviderStub());

        EmailMessage original = new EmailMessage();
        original.setReplied(false);

        calendar.deliverEmail(calendar, recipient, original, "Reply", "Body", true);

        assertTrue(original.isReplied());
    }

    @Test
    public void monitorOrdersTriggersNotifyCustomerDelayTest() {
        OrdersRepository repo = OrdersRepository.getInstance();

        Customer customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@mail.com"), "CUST-001", new Date());
        customer.setEmailProviderStub(new EmailProviderStub());

        ShoppingCart shoppingCart = new ShoppingCart(customer);
        Order delayedOrder = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH,
                new Date(), shoppingCart);
        delayedOrder.setOrderstatus(StatusType.DELAYED);

        repo.addOrder(delayedOrder);

        calendar.monitorOrders();

        // The customer must receive the emails
        assertEquals(1, customer.getEmailProviderStub().getInboxEmails().size());
        EmailMessage msg = customer.getEmailProviderStub().getInboxEmails().get(0);
        assertTrue(msg.getSubject().contains("ORDER DELAY"));
        assertTrue(msg.getBody().contains("Delay!"));
    }

    @Test
    public void monitorOrdersTriggersNotifyCustomerReadyTest(){
        OrdersRepository repo = OrdersRepository.getInstance();

        Customer customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", new EmailAddress("giannis@mail.com"), "CUST-001", new Date());
        customer.setEmailProviderStub(new EmailProviderStub());

        ShoppingCart shoppingCart = new ShoppingCart(customer);
        Order delayedOrder = new Order("order1246", new Date(), StatusType.NEW, false, PaymentType.CASH,
                new Date(), shoppingCart);


        delayedOrder.setOrderstatus(StatusType.SHIPPED);

        repo.addOrder(delayedOrder);

        calendar.monitorOrders();

        // The customer must receive the emails
        assertEquals(1, customer.getEmailProviderStub().getInboxEmails().size());
        EmailMessage msg = customer.getEmailProviderStub().getInboxEmails().get(0);
        assertTrue(msg.getSubject().contains("ORDER READY"));
        assertTrue(msg.getBody().contains("is now ready"));
    }

    @After
    public void tearDown() {
        calendar.getEmailProviderStub().getInboxEmails().clear();
        calendar.getEmailProviderStub().getSentEmails().clear();
        OrdersRepository.getInstance().clear();
        CustomerRepository.getInstance().getCustomers().clear();
    }

}