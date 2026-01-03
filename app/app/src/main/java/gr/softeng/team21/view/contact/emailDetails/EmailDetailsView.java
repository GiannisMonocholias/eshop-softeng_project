package gr.softeng.team21.view.contact.emailDetails;

public interface EmailDetailsView {
    void displaySubject(String subject);
    void displaySenderName(String sender);
    void displayReceiverName(String receiver);
    void displayBody(String body);

    void displaySenderEmail(String email);
    void displayReceiverEmail(String email);
}