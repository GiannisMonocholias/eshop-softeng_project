package gr.softeng.team21.view.admin.deleteEmp;

import java.util.HashMap;

import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Η EmpInfoPresenter βρίσκει με βάση τα δεδομένα που παίρνει απο την DeleteEmployeeActivity
 * τον υπάλληλο που αναζητάει ο διαχειριστής και παραθέτει τα στοιχεία του, ώστε να είναι βέβαιος ότι
 * διαγράφει τον σωστό υπάλληλο.
 *
 * Για να γίνει η αναζήτηση χρησιμοποιεί το employeeDAOMemory, το οποίο επιτρέπει πρόσβαση
 * στα δεδομένα των υπαλλήλων, τα αντικείμενα των οποίων αποθηκεύει σε ένα τοπικό hashmap.
 */

public class EmpInfoPresenter {

    private EmployeeDAOMemory employeeDAOMemory = EmployeeDAOMemory.getInstance();
    private HashMap<String , Employee> map = employeeDAOMemory.getEmployees();
    private Employee toDelete = null;

    /**
     *
     * @param firstname εκφράζει το όνομα του προς διαγραφή υπαλλήλου.
     * @param lastname εκφράζει το επίθετο του προς διαγραφή υπαλλήλου.
     * @param phone εκφράζει το τηλέφωνο του προς διαγραφή υπαλλήλου.
     *
     * Η deleteEmp(...) βρίσκει τον υπάλληλο που αναζητά ο διαχειριστής και τον αφαιρεί απο την λίστα
     * των υπαλλήλων του καταστήματος.
     */

    public void deleteEmp(String firstname , String lastname , String phone){
        for(Employee emp : map.values()){
            if((emp.getFirstname().equals(firstname)) && (emp.getLastname().equals(lastname)) && (emp.getPhonenumber().equals(phone))){
                toDelete = emp;
            }
        }

        if(toDelete != null){
            employeeDAOMemory.removeEmployee(toDelete);
        }

    }
}
