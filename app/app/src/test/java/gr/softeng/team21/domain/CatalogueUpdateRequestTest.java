package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link CatalogueUpdateRequest} class.
 * This suite verifies the integrity of request metadata, including unique identifiers,
 * submission dates, descriptions, product associations, execution status, and assigned employee Foreign Keys.
 * @author Γιάννης Μονοχολιάς
 */
public class CatalogueUpdateRequestTest {
    private CatalogueUpdateRequest request;
    private ProductType product1;
    private Date previousDate;

    /**
     * Sets up the testing environment before each test case.
     * Initializes a sample product and an update request with predefined values.
     */
    @Before
    public void setUp(){
        product1 = new ProductType("Mouse Logitech", "Wireless", new Money(50, "€"), "product1245");
        previousDate = new Date(1, 12, 2025);
        request = new CatalogueUpdateRequest(previousDate, "Add new laptop to catalogue", product1, AllowedRequest.INSERT_PRODUCT, 101);
    }

    /**
     * Verifies that the unique request ID is correctly retrieved.
     */
    @Test
    public void getIdTest() {
        assertEquals(101, request.getId());
    }

    /**
     * Tests the modification date property accessors.
     */
    @Test
    public void dateModifiedGetterSetterTest() {
        assertEquals(previousDate, request.getDateModified());

        Date newDate = new Date(5, 12, 2025);
        request.setDateModified(newDate);
        assertEquals(newDate, request.getDateModified());
    }

    /**
     * Tests the update description property accessors.
     */
    @Test
    public void updateDescriptionGetterSetterTest() {
        // Previous description
        assertEquals("Add new laptop to catalogue", request.getUpdateDescription());

        // New description
        request.setUpdateDescription("Updated description");
        assertEquals("Updated description", request.getUpdateDescription());
    }

    /**
     * Tests the product association property accessors.
     */
    @Test
    public void productGetterSetterTest() {
        // Previous product
        assertEquals(product1, request.getProduct());

        // New product
        ProductType newProduct = new ProductType("Laptop Dell", "High End", new Money(500, "€"), "l101");
        request.setProduct(newProduct);
        assertEquals(newProduct, request.getProduct());
    }

    /**
     * Tests the request type (AllowedRequest enum) property accessors.
     */
    @Test
    public void typeGetterSetterTest() {
        assertEquals(AllowedRequest.INSERT_PRODUCT, request.getType());

        request.setType(AllowedRequest.DELETE_PRODUCT);
        assertEquals(AllowedRequest.DELETE_PRODUCT, request.getType());
    }

    /**
     * Verifies the execution status logic.
     * Ensures requests start as unexecuted and correctly transition to executed.
     */
    @Test
    public void executedGetterSetterTest(){
        // Initially the request is not executed
        assertFalse(request.getExecuted());

        request.setExecuted(true);
        // Now the request has been executed
        assertTrue(request.getExecuted());
    }

    /**
     * Tests the assigned employee ID (Foreign Key) property accessors.
     * Ensures new requests start unassigned and can successfully store an employee ID.
     */
    @Test
    public void assignedEmployeeIdGetterSetterTest() {
        // Initially, the request is not assigned to anyone
        assertNull(request.getAssignedEmployeeId());

        // Assign the request to an employee using their ID
        String expectedEmployeeId = "CAT-301";
        request.setAssignedEmployeeId(expectedEmployeeId);

        assertEquals(expectedEmployeeId, request.getAssignedEmployeeId());
    }

    /**
     * Tests the request status (RequestStatusType enum) property accessors.
     */
    @Test
    public void statusGetterSetterTest() {
        // Initially NEW based on the default initialization
        assertEquals(RequestStatusType.NEW, request.getStatus());

        request.setStatus(RequestStatusType.ASSIGNED);
        assertEquals(RequestStatusType.ASSIGNED, request.getStatus());
    }
}