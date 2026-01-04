package gr.softeng.team21.view.contact.editdata.Password;

public interface PasswordView {
    void SaveSuccess(String message);
    void showError(String message);

    void setPassword(String password);

    void finishView();
}