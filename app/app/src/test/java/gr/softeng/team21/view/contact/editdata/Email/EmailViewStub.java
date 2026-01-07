package gr.softeng.team21.view.contact.editdata.Email;

public class EmailViewStub implements EmailView {

    private String message;
    private String email;

    @Override
    public void SaveSuccess(String msg) {
        message = msg;
    }

    @Override
    public void showError(String msg) {
        message = msg;

    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
        public void finishView() {

    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }


}