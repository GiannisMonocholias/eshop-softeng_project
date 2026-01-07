package gr.softeng.team21.view.customer.EmailList;

public class CustomerEmailListViewStub implements CustomerEmailListView {

    private int CreateNewMsgCount = 0;
    private String passedCustomerId = "";

    private int EmailDetailsCount = 0;
    private String detailsSubject, detailsBody, detailsSender, detailsReceiver, detailsId;

    @Override
    public void goToCreateNewMessge(String customerId) {
        CreateNewMsgCount++;
        this.passedCustomerId = customerId;
    }

    @Override
    public void goToEmailDetails(String subject, String body, String sender, String receiver, String customerId) {
        EmailDetailsCount++;
        this.detailsSubject = subject;
        this.detailsBody = body;
        this.detailsSender = sender;
        this.detailsReceiver = receiver;
        this.detailsId = customerId;
    }

    // Getters for Assertions in Test
    public int getCreateNewMsgCount() {
        return CreateNewMsgCount;
    }

    public String getPassedCustomerId() {
        return passedCustomerId;
    }

    public int getEmailDetailsCount() {
        return EmailDetailsCount;
    }

    public String getDetailsSubject() {
        return detailsSubject;
    }

    public String getDetailsBody() {
        return detailsBody;
    }

    public String getDetailsSender() {
        return detailsSender;
    }

    public String getDetailsReceiver() {
        return detailsReceiver;
    }

    public String getDetailsId() {
        return detailsId;
    }
}