package gr.softeng.team21.view.user.emailComposition;

public interface EmailCompositionView {
    String getRecipientEmail();
    String getSubject();
    String getBody();

    void setSenderDetails(String name, String email);

    void showErrorMessage(String message);

    void showSuccessMessage(String message);

    void finishActivity();
}