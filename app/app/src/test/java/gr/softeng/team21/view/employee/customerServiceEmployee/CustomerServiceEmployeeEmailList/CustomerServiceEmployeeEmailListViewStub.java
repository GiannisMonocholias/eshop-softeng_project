package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeEmailList;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListView;

/**
 * A stub implementation of the {@link CustomerServiceEmployeeEmailListView} interface for unit testing.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeEmailListViewStub implements CustomerServiceEmployeeEmailListView {

    private ArrayList<EmailMessage> loadedEmails;
    private String errorMessage;

    private int navigateToCreateNewMsgCount = 0;
    private String passedEmployeeId = "";

    private int navigateToEmailDetailsCount = 0;
    private String detailsSubject, detailsBody, detailsSender, detailsReceiver, detailsId;

    @Override
    public void updateEmailList(ArrayList<EmailMessage> emails) {
        this.loadedEmails = emails;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    @Override
    public void navigateToCreateNewMsg(String employeeId) {
        navigateToCreateNewMsgCount++;
        this.passedEmployeeId = employeeId;
    }

    @Override
    public void navigateToEmailDetails(String subject, String body, String sender, String receiver, String employeeId) {
        navigateToEmailDetailsCount++;
        this.detailsSubject = subject;
        this.detailsBody = body;
        this.detailsSender = sender;
        this.detailsReceiver = receiver;
        this.detailsId = employeeId;
    }

    public ArrayList<EmailMessage> getLoadedEmails() { return loadedEmails; }
    public String getErrorMessage() { return errorMessage; }
    public int getNavigateToCreateNewMsgCount() { return navigateToCreateNewMsgCount; }
    public String getPassedEmployeeId() { return passedEmployeeId; }
    public int getNavigateToEmailDetailsCount() { return navigateToEmailDetailsCount; }
    public String getDetailsSubject() { return detailsSubject; }
    public String getDetailsId() { return detailsId; }
    public String getDetailsSender(){ return detailsSender; }
    public String getDetailsReceiver(){ return detailsReceiver; }
    public String getDetailsBody(){ return detailsBody; }
}