package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class UpdateRequestsRepositoryTest {
    ProductType product1;
    CatalogueUpdateRequest insertRequest;
    UpdateRequestsRepository requestsRepository;

    @Before
    public void setUp() throws Exception {
        requestsRepository = UpdateRequestsRepository.getInstance();
        requestsRepository.clear();

        product1 = new ProductType ("Laptop Dell", "High End",  new Money ( 500, "€" ), "product1245");
        insertRequest = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product1, AllowedRequest.INSERT_PRODUCT, 1);
        requestsRepository.addUpdateRequest(insertRequest);
    }

    @Test
    public void getInstanceReturnsSameReferences() {
        UpdateRequestsRepository requestsRepositoyry2 = UpdateRequestsRepository.getInstance();
        assertSame(requestsRepository, requestsRepositoyry2);
    }


    @Test
    public void getUpdateRequestNonExistingRequestTest() {
        // Non existing request
        assertNull(requestsRepository.getUpdateRequest(5));
    }


    @Test(expected = IllegalArgumentException.class)
    public void addUpdateRequest_AlreadyExistingRequestTest(){
        // Already existing request (με ίδιο ID)
        requestsRepository.addUpdateRequest(insertRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUpdateRequest_NullArgumentTest(){
        // Null argument passed in addUpdateRequest() method
        requestsRepository.addUpdateRequest(null);
    }

    @Test
    public void addUpdateRequestSuccessTest(){
        ProductType product2 = new ProductType ("Laptop Lenovo", "High End",  new Money ( 600, "€" ), "product1246");
        CatalogueUpdateRequest insertRequest2 = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product2, AllowedRequest.INSERT_PRODUCT, 2);
        requestsRepository.addUpdateRequest(insertRequest2);
        assertSame(insertRequest2, requestsRepository.getUpdateRequest(2));
    }


    @Test(expected = IllegalArgumentException.class)
    public void deleteUpdateRequest_NonRegisteredRequestTest() {
        CatalogueUpdateRequest insertRequest2 = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product1, AllowedRequest.DELETE_PRODUCT, 2);
        // Trying to delete a request that is not registered in the repository
        requestsRepository.deleteUpdateRequest(insertRequest2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteUpdateRequest_NullArgumentTest() {
        // The request argument must not be null
        requestsRepository.deleteUpdateRequest(null);
    }


    @Test
    public void getUpdateRequests() {
        assertEquals(1,requestsRepository.getUpdateRequests().size());

        ProductType product2 = new ProductType ("Laptop Lenovo", "High End",  new Money ( 600, "€" ), "product1246");
        CatalogueUpdateRequest insertRequest2 = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product2, AllowedRequest.INSERT_PRODUCT, 2);
        requestsRepository.addUpdateRequest(insertRequest2);

        assertEquals(2,requestsRepository.getUpdateRequests().size());



        requestsRepository.deleteUpdateRequest(insertRequest2);
        assertEquals(1,requestsRepository.getUpdateRequests().size());

        requestsRepository.deleteUpdateRequest(insertRequest);
        assertEquals(0,requestsRepository.getUpdateRequests().size());

    }

    @Test
    public void clear() {
        requestsRepository.clear();
        assertEquals(0, requestsRepository.getUpdateRequests().size());
    }

    @After
    public void tearDownTest() {
        requestsRepository.clear();
    }
}