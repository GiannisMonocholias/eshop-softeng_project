package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.ProductType;

public class ProductsWareHouseDAOFirebase implements ProductsWareHouseDAO {

    private final FirebaseFirestore db;
    private final FirebaseFunctions functions;
    private static final String PRODUCTS_COLLECTION = "warehouse_products";
    private static final String CONFIG_COLLECTION = "warehouse_config";
    private static final String CONFIG_DOC = "main_capacity";

    public ProductsWareHouseDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
        this.functions = FirebaseFunctions.getInstance();
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Integer> getProductStock(ProductType type) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        if (type == null) {
            future.completeExceptionally(new IllegalArgumentException("Type cannot be null"));
            return future;
        }

        db.collection(PRODUCTS_COLLECTION).document(type.getProductCode()).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.contains("stock")) {
                        future.complete(doc.getLong("stock").intValue());
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Double> getCapacityUtilization() {
        return getProductStocks().thenCombine(getMaxCapacity(), (stocks, maxCap) -> {
            int totalProducts = stocks.values().stream().mapToInt(Integer::intValue).sum();
            return totalProducts > 0 ? (double) totalProducts / maxCap : 0.0;
        });
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> insertProduct(ProductType type) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (type == null) {
            future.completeExceptionally(new IllegalArgumentException("Type cannot be null"));
            return future;
        }
        db.collection(PRODUCTS_COLLECTION).document(type.getProductCode()).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        future.completeExceptionally(new IllegalArgumentException("Product already exists"));
                    } else {
                        Map<String, Object> data = new HashMap<>();
                        data.put("stock", 0);

                        db.collection(PRODUCTS_COLLECTION).document(type.getProductCode()).set(data)
                                .addOnSuccessListener(v -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> deleteProduct(ProductType type) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (type == null) {
            future.completeExceptionally(new IllegalArgumentException("Type cannot be null"));
            return future;
        }

        db.collection(PRODUCTS_COLLECTION).document(type.getProductCode()).get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        future.completeExceptionally(new NoSuchElementException("Product not in stock"));
                    } else {
                        db.collection(PRODUCTS_COLLECTION).document(type.getProductCode()).delete()
                                .addOnSuccessListener(v -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Boolean> increaseProductStock(ProductType type, int amount) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        isValidAmount(amount).thenAccept(isValid -> {
            if (!isValid || type == null) {
                future.complete(false);
                return;
            }

            db.collection(PRODUCTS_COLLECTION).document(type.getProductCode()).get()
                    .addOnSuccessListener(doc -> {
                        if (!doc.exists()) {
                            future.complete(false);
                        } else {
                            db.collection(PRODUCTS_COLLECTION).document(type.getProductCode())
                                    .update("stock", FieldValue.increment(amount))
                                    .addOnSuccessListener(v -> future.complete(true))
                                    .addOnFailureListener(e -> future.complete(false));
                        }
                    })
                    .addOnFailureListener(e -> future.complete(false));
        });

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Boolean> decreaseProductStock(ProductType type, int amount) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        isValidAmount(amount).thenCombine(sufficientStock(type, amount), (isValid, sufficient) -> {
            if (!isValid || !sufficient) {
                future.complete(false);
                return null;
            }

            db.collection(PRODUCTS_COLLECTION).document(type.getProductCode())
                    .update("stock", FieldValue.increment(-amount))
                    .addOnSuccessListener(v -> future.complete(true))
                    .addOnFailureListener(e -> future.complete(false));

            return null;
        }).exceptionally(e -> {
            future.complete(false);
            return null;
        });

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Boolean> sufficientStock(ProductType type, int amount) {
        return getProductStock(type).thenApply(stock -> stock != null && (stock - amount >= 0));
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Boolean> isValidAmount(int amount) {
        return CompletableFuture.completedFuture(amount > 0);
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<HashMap<ProductType, Integer>> getProductStocks() {
        CompletableFuture<HashMap<ProductType, Integer>> future = new CompletableFuture<>();

        db.collection(PRODUCTS_COLLECTION).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashMap<ProductType, Integer> map = new HashMap<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        try {

                            ProductType pt = doc.toObject(ProductType.class);
                            int stock = doc.contains("stock") ? doc.getLong("stock").intValue() : 0;
                            map.put(pt, stock);
                        } catch (Exception e) {
                            System.err.println("Σφάλμα κατά τη μετατροπή εγγράφου: " + e.getMessage());
                        }
                    }
                    future.complete(map);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;

    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Integer> getMaxCapacity() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        db.collection(CONFIG_COLLECTION).document(CONFIG_DOC).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.contains("maxCapacity")) {
                        future.complete(doc.getLong("maxCapacity").intValue());
                    } else {
                        future.complete(1000);
                    }
                })
                .addOnFailureListener(e -> {
                    future.complete(1000);
                });

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> setMaxCapacity(int maxCapacity) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Map<String, Object> data = new HashMap<>();
        data.put("maxCapacity", maxCapacity);

        db.collection(CONFIG_COLLECTION).document(CONFIG_DOC).set(data)
                .addOnSuccessListener(v -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**{@inheritDoc}*/
    @Override
    public CompletableFuture<Void> clear() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Prepare the data to be sent to the Cloud Function
        Map<String, Object> data = new HashMap<>();
        data.put("collectionPath", PRODUCTS_COLLECTION);

        // Call of the Cloud Function 'deleteCollection'
        functions.getHttpsCallable("deleteCollection")
                .call(data)
                .addOnSuccessListener(result -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}