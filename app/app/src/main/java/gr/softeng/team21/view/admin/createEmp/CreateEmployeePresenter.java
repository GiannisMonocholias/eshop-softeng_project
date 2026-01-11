package gr.softeng.team21.view.admin.createEmp;

import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.domain.EmployeeState;

/**
 * Η κλάση createEmployeePresenter είναι υπεύθυνη για τη δημιουργία
 * λογαριασμού για έναν υπάλληλο από τον διαχειριστή.
 *
 * Χρησιμοποιεί ένα αντικείμενο Employee για να τη δημιουργία του νέου λογαριασμού.
 *
 * Έχει επίσης ένα αντικείμενο τύπου EmployeeDAOMemory για να μπορεί να τροποποιεί και να
 * αποθηκεύει δεδόμενα της λίστας με τους υπαλλήλους του καταστήματος.
 */

public class CreateEmployeePresenter {

    private Employee employee;
    private EmployeeDAOMemory emps;
    private CreateEmplyeeView view;
    private String key;

    public void setView(CreateEmplyeeView view){
        this.view = view;
    }

    /**
     *
     * @param username εκφράζει το όνομα χρήστη του νέου υπαλλήλου.
     * @param email εκφράζεται το email του νέου υπαλλήλου.
     * @param firstName εκφράζει το όνομα του νέου υπαλλήλου.
     * @param lastName εκφράζει το επίθετο του νέου υπαλλήλου.
     * @param phone εκφράζει το τηλέφωνο του νέου υπαλλήλου.
     * @param address εκφράζει τη διεύθυνση του νέου υπαλλήλου.
     * @param password εκφράζει τον κωδικό πρόσβασης του νέου υπαλλήλου.
     * @param id εκφράζει το μοναδικό id του νέου υπαλλήλου.
     * @param salary εκφράζει τον μισθό του νέου υπαλλήλου.
     *
     * Η saveData(...) δέχεται ως ορίσματα τα παραπάνω δεδομένα και δημιουργεί ένα νέο
     * αντικείμενο υπαλλήλου με αυτά τα χαρακτηριστικά, το οποίο αποθηκεύει στη λίστα με τουσ υπαλλήλους.
     */
    public void saveData(String username , EmailAddress email , String firstName , String lastName , String phone , String address , String password , String id , int salary){
        employee = new Employee(username , firstName , password , lastName , phone , email , id , 0 , salary , 8 , EmployeeState.ACTIVE , new Date());


        emps = EmployeeDAOMemory.getInstance();

        if(emps.getEmployees().size() <= 12){
            key = "000";
        }else {
            key = Integer.toString(Integer.parseInt(key) + 1);
        }


        emps.getEmployees().put(key , employee);
    }
}
