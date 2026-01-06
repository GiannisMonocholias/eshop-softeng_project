package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

public class CatalogueUpdateRequestTest {
    private CatalogueUpdateRequest request;
    private  ProductType product1;
    Date previousDate;

    @Before
    public void setUp(){
        product1 = new ProductType ( "Mouse Logitech", "Wireless", new Money( 50, "€" ), "product1245" );
        previousDate = new Date(1, 12, 2025);
        request = new CatalogueUpdateRequest(previousDate, "Add new laptop to catalogue", product1, AllowedRequest.INSERT_PRODUCT, 101);
    }

    @Test
    public void getIdTest() {
        assertEquals(101, request.getId());
    }

    @Test
    public void dateModifiedGetterSetterTest() {
        assertEquals(previousDate, request.getDateModified());

        Date newDate = new Date(5, 12, 2025);
        request.setDateModified(newDate);
        assertEquals(newDate, request.getDateModified());
    }


    @Test
    public void updateDescriptionGetterSetterTest() {
        // Previous description
        assertEquals("Add new laptop to catalogue", request.getUpdateDescription());

        // New description
        request.setUpdateDescription("Updated description");
        assertEquals("Updated description", request.getUpdateDescription());
    }

    @Test
    public void productGetterSetterTest() {
        // Previous product
        assertEquals(product1, request.getProduct());

        // New product
        ProductType newProduct = new ProductType ( "Laptop Dell", "High End", new Money ( 500, "€" ), "l101" );
        request.setProduct(newProduct);
        assertEquals(newProduct, request.getProduct());
    }

    @Test
    public void typeGetterSetterTest() {
        assertEquals(AllowedRequest.INSERT_PRODUCT, request.getType());

        request.setType(AllowedRequest.DELETE_PRODUCT);
        assertEquals(AllowedRequest.DELETE_PRODUCT, request.getType());
    }

    @Test
    public void executedGetterSetterTest(){
        //Initially the request is not executed
        assertFalse(request.getExecuted());

        request.setExecuted(true);
        // Now the request has been executed
        assertTrue(request.getExecuted());
    }


}