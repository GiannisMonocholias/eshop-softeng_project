package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeEmailList;

import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListView;

public class CustomerServiceEmployeeEmailListViewStub implements CustomerServiceEmployeeEmailListView {

    private int navigateToCreateNewMsgCount = 0;
    private String passedEmployeeId = "";

    private int navigateToEmailDetailsCount = 0;
    private String detailsSubject, detailsBody, detailsSender, detailsReceiver, detailsId;

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