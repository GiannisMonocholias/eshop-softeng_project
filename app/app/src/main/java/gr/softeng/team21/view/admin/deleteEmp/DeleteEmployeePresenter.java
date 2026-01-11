package gr.softeng.team21.view.admin.deleteEmp;

import java.util.HashMap;

import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Η DeleteEmployeePresenter διαγράφει όποιιον υπάλληλο επιθυμεί ο διαχειριστής.
 *
 * Για να έχει πρόσβαση στη λίστα υπαλλήλων χρησιμοποιεί ένα αντικείμενο τησ κλάσης EmployeeDAOMemory
 * και προκειμένου να κρατήσει τοπικά τα στοιχεια που χρειάζεται χρησιμοποιεί ενα hashmap στο οποίο
 * αποθηκεύει τιμές τύπου string ( μοναδικό κλειδί κάθε στοιχείου ) και employee ( αντικείμενο υπαλλήλου ).
 */
public class DeleteEmployeePresenter {

    private DeleteEmployeeView view;

    private EmployeeDAOMemory emp = EmployeeDAOMemory.getInstance();
    private HashMap<String , Employee> map = emp.getEmployees();


    public void setView(DeleteEmployeeView view){
        this.view = view;
    }

    /**
     *
     * @param username εκφράζει το όνομα χρήστη του υπαλλήλου που αναζητούμε
     * @param id εκφράζει το μοναδικό id του ίδιου υπαλλήλου
     * @return επιστρέφει τον αντίστοιχο υπάλληλο
     *
     * Η searchEmp(...) αναζητά τον προς διαγραφή υπάλληλο στο σύστημα με βάση το username που χρησιμοποιεί
     * και το id του.
     *
     */

    public Employee searchEmp(String username, String id){
        for(Employee emp : map.values()){
            if((emp.getUsername().equals(username)) && (emp.getEmployeeId().equals(id))){
                return emp;
            }
        }

        return null;
    }


}
