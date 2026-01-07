package gr.softeng.team21.view.contact.editdata.Phone;

public class PhoneViewStub implements PhoneView{
    private String message;
    private String Phone;


    // --- Getters για τα Tests ---
    public String getMessage() {
        return message;
    }

    public String getPhone() {
        return Phone;
    }


    @Override
    public void SaveSuccess(String message) {
        this.message = message;
    }

    @Override
    public void showError(String message) {
        this.message = message;
    }

    @Override
    public void setPhone(String phone) {
        this.Phone = phone;
    }

    @Override
    public void finishView() {
    }
}
