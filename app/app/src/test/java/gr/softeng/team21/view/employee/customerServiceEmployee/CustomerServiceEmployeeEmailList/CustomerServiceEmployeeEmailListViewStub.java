package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeEmailList;

import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListView;

/**
 * A stub implementation of the {@link CustomerServiceEmployeeEmailListView} interface for unit testing.
 * It provides a way to verify navigation calls and the correctness of the data
 * passed from the presenter to the view.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeEmailListViewStub implements CustomerServiceEmployeeEmailListView {

    private int navigateToCreateNewMsgCount = 0;
    private String passedEmployeeId = "";

    private int navigateToEmailDetailsCount = 0;
    private String detailsSubject, detailsBody, detailsSender, detailsReceiver, detailsId;

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