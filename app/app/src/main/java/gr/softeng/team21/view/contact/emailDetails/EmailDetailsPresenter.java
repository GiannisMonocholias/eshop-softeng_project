package gr.softeng.team21.view.contact.emailDetails;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class EmailDetailsPresenter {
    private EmailDetailsView view;
    EmployeeDAO employeeDAO;
    CustomerDAO customerDAO;

    public EmailDetailsPresenter(EmailDetailsView view, EmployeeDAO employeeDAO, CustomerDAO customerDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.customerDAO = customerDAO;
    }


    public void onViewCreated(String subject, String senderEmailAddress, String receiverEmailAddress, String body, String employeeId) {
        String senderName = findSenderName(senderEmailAddress);


        view.displaySubject(subject!=null?subject: "");
        view.displaySenderName(senderName!=null? senderName : "Άγνωστο όνομα αποστολέα");
        view.displayReceiverName(findReceiverName(employeeId));
        view.displayBody(body != null ? body : "");
        view.displaySenderEmail(senderEmailAddress);
        view.displayReceiverEmail(receiverEmailAddress);;

    }

    public String findReceiverName(String id) {
        Employee employee = employeeDAO.getEmployee(id);
        if (employee != null) {
            return employee.getFirstname() + " " + employee.getLastname();
        }
        Customer customer = customerDAO.getCustomer(id);
        if (customer != null) {
            return customer.getFirstname() + " " + customer.getLastname();
        }

        return "xxxx";
    }

    public String findSenderName(String senderEmailAddress){
        Employee employee = ((EmployeeDAOMemory)employeeDAO).getEmployeeByEmail(senderEmailAddress);
        if(employee != null){
            return employee.getFirstname() + " " +  employee.getLastname() + "(Υπάλληλος)";
        }

        Customer customer = ((CustomerDAOMemory)customerDAO).getCustomerByEmail(senderEmailAddress);
        if(customer != null){
            return customer.getFirstname() + " " +  customer.getLastname() + "(Πελάτης)";
        }

        Admin admin = Admin.getInstance();
        if(admin.getEmailAddress().toString().equals(senderEmailAddress)){
            return admin.getFirstname() + " " + admin.getLastname() + "(Διαχειριστής)";
        }

        return null;
    }
}