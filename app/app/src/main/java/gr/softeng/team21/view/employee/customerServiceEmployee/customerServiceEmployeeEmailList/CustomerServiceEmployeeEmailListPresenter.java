package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.EmailMessage;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class CustomerServiceEmployeeEmailListPresenter {
    private EmployeeDAO employeeDAO;
    private CustomerServiceEmployeeEmailListView view;

    public CustomerServiceEmployeeEmailListPresenter(CustomerServiceEmployeeEmailListView view, EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
        this.view = view;
    }

    public ArrayList<EmailMessage> getInbox(String employeeId){
        CustomerServiceEmployee employee = (CustomerServiceEmployee) employeeDAO.getEmployee(employeeId);
        return employee.getEmailProvider().getInboxEmails();
    }

    public void onCreateNewMsgSelected(String employeeId){
        view.navigateToCreateNewMsg(employeeId);
    }

    public void onEmailSelected(EmailMessage email, String Id){
        email.setRead(true);
        view.navigateToEmailDetails(
                email.getSubject(), email.getBody(),
                email.getFrom().toString(), email.getTo().toString(),
                Id
        );
    }
}
