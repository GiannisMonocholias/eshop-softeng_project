package gr.softeng.team21.view.contact.editdata.Username;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class UsernamePresenter {
    private UsernameView view;
    private User user;
    private UserCredentialsDAOMemory credentialsDAO;

    public UsernamePresenter(UsernameView view, String userId) {
        this.view = view;
        this.credentialsDAO = UserCredentialsDAOMemory.getInstance();
        findUser(userId);
    }

    private void findUser(String userId) {
        user = CustomerDAOMemory.getInstance().getCustomer(userId);

        if (user == null) {
            user = EmployeeDAOMemory.getInstance().getEmployee(userId);
        }

        if (user == null) {
            view.showError("Ο χρήστης δεν βρέθηκε.");
            view.finishView();
            return;
        }

        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            view.setUsername(user.getUsername());
        }
    }

    public void saveUsernameClicked(String newName) {
        if (user == null) return;
        if (newName.isEmpty()) {
            view.showError("Παρακαλώ εισάγετε Username");
            return;
        }

        if (user.getUsername().equals(newName)) {
            view.SaveSuccess("Δεν έγιναν αλλαγές.");
            return;
        }

        if (credentialsDAO.getUsersCredentials().containsKey(newName)) {
            view.showError("Το username χρησιμοποιείται ήδη.");
            return;
        }

            String oldUsername = user.getUsername();
            credentialsDAO.removeUser(oldUsername);

            user.editData("1", newName, null, null);

            credentialsDAO.addUser(user);

            view.SaveSuccess("Το username ενημερώθηκε επιτυχώς!");


    }
}