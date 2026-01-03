package gr.softeng.team21.view.customer.register;

public interface RegisterView {
    void showSuccessMessage(String message);
    void showErrorMessage(String message);

    void clearInputFields();
}