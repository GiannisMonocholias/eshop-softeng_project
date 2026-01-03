package gr.softeng.team21.view.user.EditData;

import android.content.Intent;

public class UserEditDataPresenter {
    private UserEditDataView view;

    public void setView(UserEditDataView view) {
        this.view = view;
    }

    public UserEditDataPresenter(UserEditDataView view) {
        this.view = view;
    }

    public void Selection(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                view.goToUsername();
                break;

            case 1:
                view.goToPassword();
                break;

            case 2:
                view.goToAddress();
                break;

            case 3:
                view.goToEmail();
                break;

            case 4:
                view.goToPhone();
                break;
        }

    }
}
