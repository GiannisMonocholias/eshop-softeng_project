package gr.softeng.team21.view.contact.editdata.Phone;

public interface PhoneView {
    void SaveSuccess(String message);
    void showError(String message);
    void setPhone(String phone); // Για να εμφανιστεί το παλιό τηλέφωνο
    void finishView();
}