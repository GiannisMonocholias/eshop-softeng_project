package gr.softeng.team21.domain;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;

public class CustomerTest {
  private Customer customer;
  private EmailAddress email;
  private Address address;
  private ShoppingCart shoppingCart;
  private Order order;

  @Before
  public void setUp ( ) throws Exception {
    email = TestHelper.getEmail ( );
    address = TestHelper.getAddress ( );
    CustomerDAOMemory.getInstance ( ).getCustomers ( ).clear ( );
    customer = new Customer (
            "giannispap", "Giannis", "pass1234", "Papadopoulos",
            "697123456", email, "CUST-001", new Date ( ) );
    CustomerDAOMemory.getInstance().addCustomer(customer);
    customer.setAddress ( address );

    order = new Order ( "order001", new Date ( ), StatusType.NEW, false,
            PaymentType.CASH, new Date ( ), new ShoppingCart ( ) );
  }

  @Test
  public void findProduct ( ) {
    TestHelper.addProductsManually ( );
    ProductType p = customer.findProduct ( TestHelper.getProducts ( ), "l101" );
    assertEquals ( p, TestHelper.getLaptop ( ) );
  }

  @Test
  public void findProductwithNullArguments ( ) {
    ProductType p, k;
    p = customer.findProduct ( TestHelper.getProducts ( ), "l101" );
    assertNull ( p );//empty products
    k = customer.findProduct ( TestHelper.getProducts ( ), "k101" );
    assertNull ( k );//error productcode
  }

  @Test
  public void addItemToCart ( ) {
    ProductType p = TestHelper.getMonitor ( );
    ProductType l = TestHelper.getLaptop ( );

    customer.addItemToCart ( p, 2 );
    customer.addItemToCart ( l, 3 );
    customer.addItemToCart ( l, 1 );
    assertEquals ( 2, customer.getShoppingCart ( ).getItems ( ).size ( ) );
    assertEquals ( 4, customer.getShoppingCart ( ).getItems ( ).get ( 1 ).getQuantity ( ) );//test gia na doume an anenoithike h posothta tou laptop

  }

  @Test(expected = IllegalArgumentException.class)
  public void addItemToCartWithNegativeQuantity ( ) {
    ProductType p = TestHelper.getMonitor ( );
    customer.addItemToCart ( p, -1 );
  }

  @Test(expected = IllegalArgumentException.class)
  public void addItemToCartWithNullProduct ( ) {
    customer.addItemToCart ( null, 2 );
  }

  @Test
  public void removeItemFromCart ( ) {
    ProductType p1 = TestHelper.getMonitor ( );
    ProductType p2 = TestHelper.getKeyboard ( );
    customer.addItemToCart ( p1, 6 );
    customer.addItemToCart ( p2, 5 );
    customer.removeItemFromCart ( p1, 3 );
    assertEquals ( 3, customer.getShoppingCart ( ).getItems ( ).get ( 0 ).getQuantity ( ) );

    customer.removeItemFromCart ( p2, 5 );
    assertFalse ( customer.getShoppingCart ( ).getItems ( ).contains ( p2.getProductCode ( ) ) );
  }
    @Test(expected = IllegalArgumentException.class)
    public void removeItemFromCartwithEmptyShoppingCart () {
      customer.removeItemFromCart ( TestHelper.getKeyboard ( ), 5 );
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithNegativeQuantity () {
      customer.addItemToCart ( TestHelper.getKeyboard ( ), 5 );
      customer.removeItemFromCart ( TestHelper.getKeyboard ( ), -2 );
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithNullProduct () {
      customer.addItemToCart ( TestHelper.getKeyboard ( ), 5 );
      customer.removeItemFromCart ( null, 5 );
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemWithQuantityTooHigh () {
      customer.addItemToCart ( TestHelper.getKeyboard ( ), 5 );
      customer.removeItemFromCart ( TestHelper.getKeyboard ( ), 10 );
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeItemWhereNotInCart () {
      customer.addItemToCart ( TestHelper.getKeyboard ( ), 5 );
      customer.removeItemFromCart ( TestHelper.getLaptop ( ), 2 );
    }

    @Test
    public void checkout () {
      customer.addItemToCart ( TestHelper.getLaptop ( ), 1 );
      Order order1 = customer.Checkout ( );
      assertEquals ( StatusType.NEW, order1.getOrderstatus ( ) );
      assertEquals ( PaymentType.CASH, order1.getPaymentmethod ( ) );
      assertEquals ( false, order1.getPaid ( ) );
    }

    @Test

    public void checkoutwithNullArguments () {
    assertNull ( customer.Checkout ( ) );
    }

    @Test
    public void CheckoutCopyShoppingCart () {
      ProductType laptop = TestHelper.getLaptop ( );
      customer.addItemToCart ( laptop, 1 );
      Order order2 = customer.Checkout ( );

      customer.setShoppingCart ( new ShoppingCart ( customer ) );
      customer.addItemToCart ( laptop, 5 );
      //Test an to order kratei to palio shopppingcart
      assertEquals ( 1, order2.getShoppingCart ( ).getItems ( ).size ( ) );
      assertEquals ( 1, order2.getShoppingCart ( ).getItems ( ).get ( 0 ).getQuantity ( ) );
      //Test o customer exei neo shopppingcart
      assertEquals ( 1, customer.getShoppingCart ( ).getItems ( ).size ( ) );
      assertEquals ( 5, customer.getShoppingCart ( ).getItems ( ).get ( 0 ).getQuantity ( ) );
    }

    @Test
    public void selectPaymentType () {
      assertFalse ( order.getPaid ( ) );
      assertEquals ( PaymentType.CASH, order.getPaymentmethod ( ) );
      customer.selectPaymentType ( PaymentType.CARD, "1234-5678-9123-4567", order );
      assertTrue ( order.getPaid ( ) );
      assertEquals ( PaymentType.CARD, order.getPaymentmethod ( ) );

    }

    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithNullOrder () {
      customer.selectPaymentType ( PaymentType.CASH, null, null );
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithNullPaymentType () {
      customer.selectPaymentType ( null, null, order );
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectPaymentTypeWithInvalidCardFormat () {
      customer.selectPaymentType ( PaymentType.CARD, "1234-5678", order );
    }

    @Test
    public void confirm () {//allagi
      customer.addItemToCart ( TestHelper.getMouse ( ), 5 );
      Order order1 = customer.Checkout ( );
      customer.Confirm ( "CONFIRM", order1 );
      assertTrue ( OrderDAOMemory.getInstance ( ).getOrders ( ).containsKey ( order1.getOrdercode ( ) ) );
      assertNull ( customer.getShoppingCart ( ) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirmWithNullOrder () {
      customer.Confirm ( "CONFIRM", null );

    }

    @Test(expected = IllegalArgumentException.class)
    public void confirmWithNullConfirmChoice () {
      customer.Confirm ( null, order );
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirmWithEmptyConfirmChoice () {
      customer.Confirm ( "", order );
    }

    @Test
    public void remove () {

      CustomerDAOMemory.getInstance().removeCustomer(customer);
      assertFalse ( CustomerDAOMemory.getInstance ( ).getCustomers ( ).containsKey ( customer.getCustomer_id ( ) ) );
    }
    @Test(expected = IllegalStateException.class)
    public void removeCustomerThatDoesNotExist () {
      customer.remove ( );
      customer.remove ( );
    }
    @After
    public void tearDown () throws Exception {
      TestHelper.clear ( );
      CustomerDAOMemory.getInstance ( ).getCustomers ( ).clear ( );
      OrderDAOMemory.getInstance ( ).getOrders ( ).clear ( );
    }
    @AfterClass
    public static void tearDownAfterClass () {

      TestHelper.clear ( );
      CustomerDAOMemory.getInstance ( ).getCustomers ( ).clear ( );

      OrderDAOMemory.getInstance ( ).getOrders ( ).clear ( );
    }
  }
