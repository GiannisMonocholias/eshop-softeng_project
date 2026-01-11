package gr.softeng.team21.view.admin.data;

import gr.softeng.team21.domain.Address;
import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.domain.EmailAddress;

/**
 * Η AdminDataPresenter έχει ένα αντικείμενο Admin το οποίο αντιστοιχεί στον διαχειριστή
 * του e-shop.
 *
 * Λειτουργεί ως συνδετικός κρίκος ανάμεσα στο view και στο dao αρχείο καθώς
 * περιέχει τη λογική η οποία συνδέει τισ δύο αυτές κλάσεις.
 */

public class AdminDataPresenter {

    Admin admin;
    private AdminDataView view;

    public  void setView(AdminDataView view){
        this.view = view;
    }

    /**
     *
     * @param username εκφράζει το όνομα χρήστη του διαχιειριστή.
     * @param email εκφράζει το email του διαχειριστή.
     * @param firstName εκφράζει το όνομα του διαχειριστή.
     * @param lastName εκφράζει το επίθετο του διαχειριστή.
     * @param phone εκφράζει το τηλέφωνο του διαχειριστή.
     * @param address εκφράζει τη διεύθυνση του διαχειριστή.
     *
     * Η saveData(...) ενημερώνει τα στοιχεία του διαχειριστή ανάλογα με τις παραμέτρους που θα τησ δωθούν.
     */
    public void saveData(String username , String email , String firstName , String lastName , String phone , String address){
        admin = Admin.getInstance();
        admin.setUsername(username);
        admin.setEmailaddress(new EmailAddress(email));
        admin.setFirstname(firstName);
        admin.setLastname(lastName);
        admin.setPhonenumber(phone);
        admin.setAddress(new Address());

    }
}
