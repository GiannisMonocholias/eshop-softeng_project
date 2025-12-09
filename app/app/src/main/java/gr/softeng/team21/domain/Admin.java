package gr.softeng.team21.domain;

import java.util.HashMap;

public class Admin extends User {

    private static Admin instance;
    int salary;
    EmployeeRepository rep = EmployeeRepository.getInstance();



    private Admin(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, int salary){
        super(username, firstname, password, lastname, phoneNumber, emailaddress);
        this.salary = salary;
        this.emailProviderStub = new EmailProviderStub();
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
        new Employee(username , firstname , password , lastname , phoneNumber , emailaddress , employeeId , bonus , salary , workingHours , employeeState , hireDate);
    }

    public void deleteEmployee(Employee emp){
        rep.removeEmployee(emp);
    }

    public void createUpdateRequest(Date submissionDate, String updateDescription, ProductType product, AllowedRequest type, int requestId){
        CatalogueUpdateRequest request = new CatalogueUpdateRequest(submissionDate , updateDescription , product , type , requestId);
        UpdateRequestsRepository.getInstance().addUpdateRequest(request);
    }
}