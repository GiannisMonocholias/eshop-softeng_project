package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.util.Money;
import gr.softeng.team21.domain.ProductType;

public class ProductTypeDAOMemoryTest {
    private ProductTypeDAOMemory typesRepository;
    private ProductType product1;

    @Before
    public void setUp() {
        typesRepository = ProductTypeDAOMemory.getInstance();

        typesRepository.clear();

        product1 = new ProductType("p1", "Laptop", new Money(1000,"€"), "product1245");
    }

    @Test
    public void testGetProductsInitiallyEmpty() {
        assertTrue(typesRepository.getProducts().isEmpty());
    }


    @Test
    public void getInstanceReturnsSameReferences() {
        ProductTypeDAOMemory typesRepository2 = ProductTypeDAOMemory.getInstance();
        assertSame(typesRepository, typesRepository2);
    }

    @Test
    public void getProductTestSuccess() {
        typesRepository.addProductType(product1);
        ProductType returnedProduct = ProductTypeDAOMemory.getInstance().getProduct("product1245");
        assertTrue(ProductTypeDAOMemory.getInstance().getProducts().containsKey("product1245"));
        assertSame(returnedProduct, ProductTypeDAOMemory.getInstance().getProduct("product1245"));
    }


    @Test
    public void getProductNonExistingProductTest(){
        typesRepository.addProductType(product1);
        //Non existing product type
        ProductType returnedProduct1 = ProductTypeDAOMemory.getInstance().getProduct("product1244");
        assertNull(returnedProduct1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getProductNullArgumentTest(){
        typesRepository.addProductType(product1);
        //Null productCode argument
        ProductType returnedProduct2 = ProductTypeDAOMemory.getInstance().getProduct(null);
    }

    @Test
    public void addProductSuccessTest() {
        typesRepository.addProductType(product1);

        assertEquals(product1,typesRepository.getProduct("product1245"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void addProductNullArgumentTest() {
        // null argument passed
        typesRepository.addProductType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProductAlreadyRegisteredTest() {
        // Already registered productType
        typesRepository.addProductType(product1);
        typesRepository.addProductType(product1);
    }

    @Test
    public void SuccessDeleteProductTypeTest() {
        typesRepository.addProductType(product1);

        typesRepository.deleteProductType(product1);
        assertNull(typesRepository.getProduct("product1245"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void deleteProductNullArgumentTest() {
        // null argument passed
        typesRepository.deleteProductType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteProductNonRegisteredTest() {
        // Non registered productType
        typesRepository.deleteProductType(product1);
    }


    @Test
    public void processProductSuccessTest() {
        typesRepository.addProductType(product1);
        ProductType updatedProduct = new ProductType("p2", "Gaming Laptop", new Money(1500,"€"), "product1245");

        typesRepository.processProduct(updatedProduct);


        ProductType updatedProductTest = typesRepository.getProduct("product1245");
        assertEquals("p2", updatedProductTest.getProductname());
        assertEquals(1500, updatedProductTest.getPrice().getAmount().intValue());
        assertEquals("€", updatedProductTest.getPrice().getCurrency());
        assertEquals("Gaming Laptop", updatedProductTest.getDescription());
    }



    @Test(expected = IllegalArgumentException.class)
    public void processProductNullArgumentTest() {
        // null argument passed
        typesRepository.processProduct(null);
    }

    @Test(expected = IllegalStateException.class)
    public void processProductNonRegisteredTest() {
        // Non registered productType

        typesRepository.processProduct(product1);
    }


    @Test
    public void getProducts() {
        //product1 ProductType is constructed in the setUp method
        typesRepository.addProductType(product1);
        assertEquals(1, typesRepository.getProducts().size());


        ProductType product2 = new ProductType("p2", "Gaming Laptop", new Money(1500,"€"),"product1246");
        typesRepository.addProductType(product2);
        assertEquals(2, typesRepository.getProducts().size());


        typesRepository.deleteProductType(product1);
        assertEquals(1, typesRepository.getProducts().size());

        typesRepository.deleteProductType(product2);
        assertEquals(0, typesRepository.getProducts().size());
    }

    @Test
    public void clear() {
        typesRepository.clear();
        assertEquals(0, typesRepository.getProducts().size());
    }

    @After
    public void tearDownTest(){
        typesRepository.clear();
    }
}