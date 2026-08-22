package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.ProductType;

public class ProductsWareHouseDAOMemory implements ProductsWareHouseDAO {

    private static ProductsWareHouseDAOMemory instance;
    private static int maxCapacity = 1000;
    private static double totalProducts;
    private static HashMap<ProductType, Integer> productStocks;


    private ProductsWareHouseDAOMemory() {
        totalProducts = 0;
        productStocks = new HashMap<>();
    }

    /**
     * Επιστρέφει τη μοναδική (singleton) παρουσία της κλάσης.
     */
    public static ProductsWareHouseDAOMemory getInstance() {
        if (instance == null) {
            instance = new ProductsWareHouseDAOMemory();
        }
        return instance;
    }

    @Override
    public CompletableFuture<Integer> getProductStock(ProductType type) {
        // 1. Δημιουργούμε ένα κενό "κουτί" (future) που στο μέλλον θα περιέχει έναν ακέραιο (Integer).
        CompletableFuture<Integer> future = new CompletableFuture<>();

        if (type == null) {
            // 2α. Αν υπάρχει λάθος, αντί για `throw new Exception`, "πετάμε" το σφάλμα
            // μέσα από το future χρησιμοποιώντας την completeExceptionally.
            future.completeExceptionally(new IllegalArgumentException("type argument cannot be null"));
        } else {
            // 2β. Αν όλα είναι καλά, γεμίζουμε το future με το αποτέλεσμα (το απόθεμα του προϊόντος).
            future.complete(productStocks.get(type));
        }

        // 3. Επιστρέφουμε το future, το οποίο ο καλών μπορεί να εξετάσει όποτε θέλει.
        return future;
    }

    @Override
    public CompletableFuture<Double> getCapacityUtilization() {
        double utilization = totalProducts > 0 ? totalProducts / maxCapacity : 0.0;

        // Συντόμευση: Επειδή ο υπολογισμός γίνεται ακαριαία στη μνήμη και δεν έχουμε ελέγχους,
        // μπορούμε να δημιουργήσουμε και να επιστρέψουμε κατευθείαν ένα "ολοκληρωμένο" future
        // χρησιμοποιώντας την `CompletableFuture.completedFuture(...)`.
        return CompletableFuture.completedFuture(utilization);
    }

    @Override
    public CompletableFuture<Void> insertProduct(ProductType type) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (type == null) {
            // Σφάλμα: Το type είναι null.
            future.completeExceptionally(new IllegalArgumentException("type argument cannot be null"));
        } else if (!productStocks.containsKey(type)) {
            // Επιτυχία: Το προϊόν δεν υπήρχε, οπότε το προσθέτουμε με αρχικό απόθεμα 0.
            productStocks.put(type, 0);
            // Επειδή η μέθοδος επιστρέφει Void, ολοκληρώνουμε το future περνώντας την τιμή null.
            future.complete(null);
        } else {
            // Σφάλμα: Το προϊόν υπάρχει ήδη.
            future.completeExceptionally(new IllegalArgumentException("The provided type already exists in stock"));
        }

        return future;
    }

    @Override
    public CompletableFuture<Void> deleteProduct(ProductType type) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (type == null) {
            future.completeExceptionally(new IllegalArgumentException("type argument cannot be null"));
        } else if (!productStocks.containsKey(type)) {
            future.completeExceptionally(new NoSuchElementException("Product not in stock"));
        } else {
            // Πριν διαγράψουμε το προϊόν, αφαιρούμε το απόθεμά του από το συνολικό (totalProducts)
            totalProducts -= productStocks.get(type);
            productStocks.remove(type); // Διαγραφή
            future.complete(null); // Επιτυχής ολοκλήρωση της ενέργειας
        }

        return future;
    }

    @Override
    public CompletableFuture<Boolean> increaseProductStock(ProductType type, int amount) {
        if (!productStocks.containsKey(type) || amount <= 0) {
            // Αν το προϊόν δεν υπάρχει ή το amount είναι άκυρο, επιστρέφουμε κατευθείαν ένα future με τιμή false.
            return CompletableFuture.completedFuture(false);
        }

        int previousValue = productStocks.get(type);
        productStocks.put(type, previousValue + amount);
        totalProducts += amount; // Ενημερώνουμε και τον μετρητή συνολικών προϊόντων

        // Η λειτουργία πέτυχε, επιστρέφουμε future με τιμή true.
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> decreaseProductStock(ProductType type, int amount) {
        if (!productStocks.containsKey(type) || amount <= 0) {
            return CompletableFuture.completedFuture(false);
        }

        int previousValue = productStocks.get(type);
        if (previousValue - amount >= 0) { // Έλεγχος ότι έχουμε επαρκές απόθεμα (δεν γίνεται αρνητικό)
            productStocks.put(type, previousValue - amount);
            totalProducts -= amount; // Μειώνουμε τον μετρητή συνολικών προϊόντων
            return CompletableFuture.completedFuture(true);
        }

        // Αν δεν υπήρχε επαρκές απόθεμα (π.χ. πήγαμε να αφαιρέσουμε 10 ενώ είχαμε 5), επιστρέφουμε false.
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public CompletableFuture<Boolean> sufficientStock(ProductType type, int amount) {
        Integer previousValue = productStocks.get(type);
        if (previousValue == null) {
            return CompletableFuture.completedFuture(false); // Το προϊόν δεν υπάρχει
        }
        // Ελέγχει αν η αφαίρεση του amount θα αφήσει το απόθεμα σε αριθμό >= 0.
        return CompletableFuture.completedFuture(previousValue - amount >= 0);
    }

    @Override
    public CompletableFuture<Boolean> isValidAmount(int amount) {
        // Ελέγχει αν το amount είναι θετικός αριθμός.
        return CompletableFuture.completedFuture(amount > 0);
    }

    @Override
    public CompletableFuture<HashMap<ProductType, Integer>> getProductStocks() {
        // Επιστρέφει όλο το HashMap τυλιγμένο μέσα σε ένα CompletedFuture.
        return CompletableFuture.completedFuture(productStocks);
    }

    @Override
    public CompletableFuture<Integer> getMaxCapacity() {
        // Επιστρέφει τη μέγιστη χωρητικότητα.
        return CompletableFuture.completedFuture(maxCapacity);
    }

    @Override
    public CompletableFuture<Void> setMaxCapacity(int newMaxCapacity) {
        maxCapacity = newMaxCapacity;
        return CompletableFuture.completedFuture(null); // Δεν επιστρέφουμε κάποια τιμή (Void), οπότε βάζουμε null
    }

    @Override
    public CompletableFuture<Void> clear() {
        // Καθαρίζει τα πάντα (Λίστα και Μετρητή)
        productStocks.clear();
        totalProducts = 0;
        return CompletableFuture.completedFuture(null);
    }
}