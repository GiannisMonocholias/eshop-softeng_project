package gr.softeng.team21.domain;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;
import gr.softeng.team21.util.Date;

public class Admin extends User {

    private static Admin instance;
    int salary;
    EmployeeDAOMemory rep = EmployeeDAOMemory.getInstance();



    private Admin(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, int salary){
        super(username, firstname, password, lastname, phoneNumber, emailaddress);
        this.salary = salary;
        this.emailDAOMemory = new EmailDAOMemory();
    }


    public static Admin getInstance(){
        if(instance == null){
            EmailAddress defaultEmail = new EmailAddress("default_admin");
            instance = new Admin("default_admin", "Default", "default_pass", "Admin", "N/A", defaultEmail, 0);
        }
        return instance;
    }


    public static Admin getInstance(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, int salary){

        // Δημιουργία ΜΟΝΟ αν δεν υπάρχει ήδη.
        if(instance == null){
            instance = new Admin(username, firstname, password, lastname, phoneNumber, emailaddress, salary);
        }

        return instance;
    }


    public int getSalary(){
        return salary;
    }

    public void setSalary(int salary){
        this.salary = salary;
    }


    public void createEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate){
        Employee employee = new Employee(username , firstname , password , lastname , phoneNumber , emailaddress , employeeId , bonus , salary , workingHours , employeeState , hireDate);
        EmployeeDAOMemory.getInstance().addEmployee(employee);
    }
    public void createEmployee(Employee employee){
        EmployeeDAOMemory.getInstance().addEmployee(employee);
    }

    public void createCustomerServiceEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate){
        CustomerServiceEmployee cse = new CustomerServiceEmployee(username, firstname, password, lastname ,phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        EmployeeDAOMemory.getInstance().addEmployee(cse);
    }

    public void createCustomerServiceEmployee(CustomerServiceEmployee cse){
        EmployeeDAOMemory.getInstance().addEmployee(cse);
    }

    public void createOrderPreparationEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate){
        OrderPreparationEmployee orderPreparationEmployee = new OrderPreparationEmployee(username, firstname, password, lastname ,phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        EmployeeDAOMemory.getInstance().addEmployee(orderPreparationEmployee);
    }

    public void createOrderPreparationEmployee(OrderPreparationEmployee orderPreparationEmployee){
        EmployeeDAOMemory.getInstance().addEmployee(orderPreparationEmployee);
    }

    public void createUpdateCatalogueEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate){
        UpdateCatalogueEmployee updateCatalogueEmployee = new UpdateCatalogueEmployee(username, firstname, password, lastname ,phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        EmployeeDAOMemory.getInstance().addEmployee(updateCatalogueEmployee);
    }

    public void createUpdateCatalogueEmployee(UpdateCatalogueEmployee updateCatalogueEmployee){
        EmployeeDAOMemory.getInstance().addEmployee(updateCatalogueEmployee);
    }

    public void createDeliverer(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress,
                                String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate, int quan,
                                boolean available){
        Deliverer deliverer = new Deliverer(username, firstname, password, lastname ,phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate, quan, available);
        EmployeeDAOMemory.getInstance().addEmployee(deliverer);
    }

    public void createDeliveret(Deliverer deliverer){
        EmployeeDAOMemory.getInstance().addEmployee(deliverer);
    }

    public void deleteEmployee(Employee emp){
        rep.removeEmployee(emp);
    }

    public void createUpdateRequest(Date submissionDate, String updateDescription, ProductType product, AllowedRequest type, int requestId){
        CatalogueUpdateRequest request = new CatalogueUpdateRequest(submissionDate , updateDescription , product , type , requestId);
        UpdateRequestDAOMemory.getInstance().addUpdateRequest(request);
    }
}