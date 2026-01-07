package gr.softeng.team21.view.contact.editdata.Password;

public class PasswordViewStub implements PasswordView{
    private String message;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void SaveSuccess(String msg) {
message=msg;
    }

    @Override
    public void showError(String msg) {
        message=msg;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void finishView() {
    }
}
