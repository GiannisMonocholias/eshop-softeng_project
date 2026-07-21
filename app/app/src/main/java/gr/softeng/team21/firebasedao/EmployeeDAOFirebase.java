package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;

/**
 * Firebase implementation of the {@link EmployeeDAO} interface.
 * Bridges Firebase's async Tasks to Java's CompletableFuture for non-blocking UI.
 * Handles Firestore database operations for the Employee entity.
 * @author Γιάννης Μονοχολιάς
 */
public class EmployeeDAOFirebase implements EmployeeDAO {

    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "employees";

    /**
     * Initializes the Firebase Firestore instance.
     */
    public EmployeeDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }


    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<HashMap<String, Employee>> getEmployees() {
        CompletableFuture<HashMap<String, Employee>> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashMap<String, Employee> employeesMap = new HashMap<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Employee employee = document.toObject(Employee.class);
                        employeesMap.put(employee.getEmployeeId(), employee);
                    }
                    future.complete(employeesMap);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Employee> getEmployee(String id) {
        CompletableFuture<Employee> future = new CompletableFuture<>();

        if (id == null || id.isEmpty()) {
            future.completeExceptionally(new IllegalArgumentException("ID cannot be null or empty"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Employee employee = documentSnapshot.toObject(Employee.class);
                        future.complete(employee);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Employee> getEmployeeByEmail(String emailAddress) {
        CompletableFuture<Employee> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME)
                .whereEqualTo("emailAddress.email", emailAddress)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Employee employee = queryDocumentSnapshots.getDocuments().get(0).toObject(Employee.class);
                        future.complete(employee);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> addEmployee(Employee employee) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (employee == null || employee.getEmployeeId() == null) {
            future.completeExceptionally(new IllegalArgumentException("Employee or ID cannot be null"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(employee.getEmployeeId())
                .set(employee)
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> removeEmployee(Employee employee) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (employee == null || employee.getEmployeeId() == null) {
            future.completeExceptionally(new IllegalArgumentException("Employee or ID cannot be null"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(employee.getEmployeeId())
                .delete()
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> clear() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                    future.complete(null);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}