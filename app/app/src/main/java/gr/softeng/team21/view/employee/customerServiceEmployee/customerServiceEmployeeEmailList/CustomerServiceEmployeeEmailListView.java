package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailMessage;

public interface CustomerServiceEmployeeEmailListView {

    void navigateToCreateNewMsg(String employeeId);
    void navigateToEmailDetails(String subject, String body, String sender, String receiver, String employeeId);
}
