package gr.softeng.team21.view.user.EditData;

/**
 * Presenter for the User Edit Data activity.
 * Handles user selection from the menu and triggers the appropriate navigation in the view.
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataPresenter {
    private final UserEditDataView view;

    /**
     * Initializes the presenter with the view interface.
     * @param view The view interface.
     */
    public UserEditDataPresenter(UserEditDataView view) {
        this.view = view;
    }

    /**
     * Handles the selection of a menu item based on its position in the list
     * and calls the corresponding navigation method in the view.
     * @param position The index of the selected item.
     */
    public void handleSelection(int position) {
        if (view == null) return;

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
            default:
                // Optionally handle unexpected positions
                break;
        }
    }
}