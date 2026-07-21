package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeEmailList;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListView;

/**
 * A stub implementation of the {@link CustomerServiceEmployeeEmailListView} interface for unit testing.
 * It provides a way to verify navigation calls, asynchronous data loading, and the correctness
 * of the data passed from the presenter to the view.
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

    /**
     * Captures navigation to the message composition screen.
     * @param employeeId The ID of the employee initiating the message.
     */
    @Override
    public void navigateToCreateNewMsg(String employeeId) {
        navigateToCreateNewMsgCount++;
        this.passedEmployeeId = employeeId;
    }

    /**
     * Captures navigation to the email details screen and stores the parameters for verification.
     * @param subject The email subject.
     * @param body The email content.
     * @param sender The sender's email address.
     * @param receiver The receiver's email address.
     * @param employeeId The current user's employee ID.
     */
    @Override
    public void navigateToEmailDetails(String subject, String body, String sender, String receiver, String employeeId) {
        navigateToEmailDetailsCount++;
        this.detailsSubject = subject;
        this.detailsBody = body;
        this.detailsSender = sender;
        this.detailsReceiver = receiver;
        this.detailsId = employeeId;
    }

    // --- Accessor methods for verification during assertions ---

    public ArrayList<EmailMessage> getLoadedEmails() {
        return loadedEmails;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getNavigateToCreateNewMsgCount() {
        return navigateToCreateNewMsgCount;
    }

    public String getPassedEmployeeId() {
        return passedEmployeeId;
    }

    public int getNavigateToEmailDetailsCount() {
        return navigateToEmailDetailsCount;
    }

    public String getDetailsSubject() {
        return detailsSubject;
    }

    public String getDetailsId() {
        return detailsId;
    }

    public String getDetailsSender(){
        return detailsSender;
    }

    public String getDetailsReceiver(){
        return detailsReceiver;
    }

    public String getDetailsBody(){
        return detailsBody;
    }
}