package gr.softeng.team21.view.contact.editdata.Username;

public class UsernameViewStub implements UsernameView {
    private String message;
    private String currentUsername;

    public String getMessage() {
        return message;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void SaveSuccess(String msg) {
        message = msg;
    }

    @Override
    public void showError(String msg) {
        message = msg;
    }

    @Override
    public void setUsername(String username) {
        currentUsername=username;
    }

    @Override
    public void finishView() {

    }
}
