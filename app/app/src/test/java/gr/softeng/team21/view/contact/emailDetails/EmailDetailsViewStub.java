package gr.softeng.team21.view.contact.emailDetails;

public class EmailDetailsViewStub implements EmailDetailsView {

    private String displayedSubject = "";
    private String displayedSenderName = "";
    private String displayedReceiverName = "";
    private String displayedBody = "";
    private String displayedSenderEmail = "";
    private String displayedReceiverEmail = "";

    @Override
    public void displaySubject(String subject) {
        this.displayedSubject = subject;
    }

    @Override
    public void displaySenderName(String sender) {
        this.displayedSenderName = sender;
    }

    @Override
    public void displayReceiverName(String receiver) {
        this.displayedReceiverName = receiver;
    }

    @Override
    public void displayBody(String body) {
        this.displayedBody = body;
    }

    @Override
    public void displaySenderEmail(String email) {
        this.displayedSenderEmail = email;
    }

    @Override
    public void displayReceiverEmail(String email) {
        this.displayedReceiverEmail = email;
    }


    public String getDisplayedSubject() {
        return displayedSubject;
    }

    public String getDisplayedSenderName() {
        return displayedSenderName;
    }

    public String getDisplayedReceiverName() {
        return displayedReceiverName;
    }

    public String getDisplayedBody() {
        return displayedBody;
    }

    public String getDisplayedSenderEmail() {
        return displayedSenderEmail;
    }

    public String getDisplayedReceiverEmail() {
        return displayedReceiverEmail;
    }
}