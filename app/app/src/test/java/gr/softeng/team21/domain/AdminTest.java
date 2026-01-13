package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

public class AdminTest {

    private Admin admin;
    private EmployeeDAOMemory rep;
    private UpdateRequestDAOMemory updateRequest;


    @Before 
    public void setup() {
        EmployeeDAOMemory.getInstance().clear();
        UpdateRequestDAOMemory.getInstance().clear();

        rep = EmployeeDAOMemory.getInstance();
        updateRequest = UpdateRequestDAOMemory.getInstance();
        admin = Admin.getInstance();
        admin.setSalary(1000);
    }

    @Test
    public void getInstanceReturnsTheSameReference(){
        Admin admin1 = Admin.getInstance("Alex" , "Drak" , "123" , "alexdr" , "6969696969", new EmailAddress("alexd@gmail.vom"), 5000);
        Admin admin2 = Admin.getInstance();
        assertSame(admin, admin1);
        assertSame(admin, admin2);
    }

    @Test
    public void createEmployee() {
        int before = rep.getEmployees().size();

        admin.createEmployee("Alex" , "Drak" , "123" , "alexdr" , "6969696969", new EmailAddress("alexd@gmail.vom"), "2s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date());

        int after = rep.getEmployees().size();

        assertEquals(before + 1 , after);


        before = rep.getEmployees().size();

        admin.createEmployee(new Employee("Alex" , "Drak" , "123" , "alexdr" , "6969696969", new EmailAddress("alexd@gmail.vom"), "3s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date()));

        after = rep.getEmployees().size();

        assertEquals(before + 1, after);
    }

    @Test
    public void createCustomerServiceEmployee(){
        int before = rep.getEmployees().size();

        admin.createCustomerServiceEmployee("Alex" , "Drak" , "123" , "alexdr" , "69696969", new EmailAddress("alexd@gmail.vom"), "2s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date());

        int after = rep.getEmployees().size();

        assertEquals(before + 1 , after);


        before = rep.getEmployees().size();

        admin.createCustomerServiceEmployee(new CustomerServiceEmployee("Alex" , "Drak" , "123" , "alexdr" , "69696969", new EmailAddress("alexd@gmail.vom"), "3s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date()));

        after = rep.getEmployees().size();

        assertEquals(before + 1, after);
    }

    @Test
    public void createOrderPreparationEmployee(){
        int before = rep.getEmployees().size();

        admin.createOrderPreparationEmployee("Alex" , "Drak" , "123" , "alexdr" , "69696969", new EmailAddress("alexd@gmail.vom"), "2s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date());

        int after = rep.getEmployees().size();

        assertEquals(before + 1 , after);


        before = rep.getEmployees().size();

        admin.createOrderPreparationEmployee(new OrderPreparationEmployee("Alex" , "Drak" , "123" , "alexdr" , "69696969", new EmailAddress("alexd@gmail.vom"), "3s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date()));

        after = rep.getEmployees().size();

        assertEquals(before + 1, after);
    }

    @Test
    public void createCatalogueUpdateEmployee(){
        int before = rep.getEmployees().size();

        admin.createUpdateCatalogueEmployee("Alex" , "Drak" , "123" , "alexdr" , "69696969", new EmailAddress("alexd@gmail.vom"), "2s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date());

        int after = rep.getEmployees().size();

        assertEquals(before + 1 , after);


        before = rep.getEmployees().size();

        admin.createUpdateCatalogueEmployee(new UpdateCatalogueEmployee("Alex" , "Drak" , "123" , "alexdr" , "69696969", new EmailAddress("alexd@gmail.vom"), "3s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date()));

        after = rep.getEmployees().size();

        assertEquals(before + 1, after);
    }

    @Test
    public void createDeliverer(){
        int before = rep.getEmployees().size();

        admin.createDeliverer("Alex" , "Drak" , "123" , "alexdr" , "69696969", new EmailAddress("alexd@gmail.vom"), "2s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date(),50,true);

        int after = rep.getEmployees().size();

        assertEquals(before + 1 , after);


        before = rep.getEmployees().size();

        admin.createDeliverer(new Deliverer("Alex" , "Drak" , "123" , "alexdr" , "69696969", new EmailAddress("alexd@gmail.vom"), "3s" , 200 , 1000 , 40 , EmployeeState.ACTIVE, new Date(), 50, true));

        after = rep.getEmployees().size();

        assertEquals(before + 1, after);
    }

    @Test
    public void deleteEmployee() {
        Employee e = new Employee(
                "emp2", "Dimitris", "12345", "Kara", "6999999999",
                new EmailAddress("dimitris@example.com"), "E002", 0, 1000, 40,
                EmployeeState.ACTIVE, new Date()
        );
        admin.createEmployee(e);

        int before = rep.getEmployees().size();

        admin.deleteEmployee(e);

        int after = rep.getEmployees().size();

        assertEquals(before - 1, after);
    }

    @Test
    public void testSetAndGetSalary() {
        int expectedSalary = 5000;
        admin.setSalary(expectedSalary);
        assertEquals(expectedSalary, admin.getSalary());
    }

    @Test
    public void testInitialSalary() {
        assertEquals(1000, admin.getSalary());
    }


    @Test
    public void createUpdateRequest() {
        int before = updateRequest.getUpdateRequests().size();

        admin.createUpdateRequest(new Date() , "sss" , new ProductType("Fifa26", "PS5 game", new Money(70 , "euro"), "2526") , AllowedRequest.INSERT_PRODUCT , 300);

        int after = updateRequest.getUpdateRequests().size();

        assertEquals(before + 1 , after);
    }

    @After
    public void tearDownTest() {
        EmployeeDAOMemory.getInstance().clear();
        UpdateRequestDAOMemory.getInstance().clear();
    }
}