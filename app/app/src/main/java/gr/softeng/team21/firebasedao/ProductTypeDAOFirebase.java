package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductType;

/**
 * Firebase implementation of the {@link ProductTypeDAO} interface.
 * Bridges Firebase's async Tasks to Java's CompletableFuture for non-blocking UI.
 * Handles Firestore database operations for Product Types.
 * @author PAVLOS GRATSANIS
 */
public class ProductTypeDAOFirebase implements ProductTypeDAO {

    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "product_types";

    /**
     * Initializes the Firebase Firestore instance.
     */
    public ProductTypeDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<ProductType> getProduct(String productCode) {
        CompletableFuture<ProductType> future = new CompletableFuture<>();

        if (productCode == null) {
            future.completeExceptionally(new IllegalArgumentException("Product code cannot be null"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(productCode).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ProductType product = documentSnapshot.toObject(ProductType.class);
                        future.complete(product);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> addProductType(ProductType product) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (product == null) {
            future.completeExceptionally(new IllegalArgumentException("Product cannot be null"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(product.getProductCode()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        future.completeExceptionally(new IllegalArgumentException("The given product type is already in the repository"));
                    } else {
                        db.collection(COLLECTION_NAME).document(product.getProductCode())
                                .set(product)
                                .addOnSuccessListener(aVoid -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> deleteProductType(ProductType product) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (product == null) {
            future.completeExceptionally(new IllegalArgumentException("Product cannot be null"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(product.getProductCode()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection(COLLECTION_NAME).document(product.getProductCode()).delete()
                                .addOnSuccessListener(aVoid -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    } else {
                        future.completeExceptionally(new IllegalArgumentException("The given product type is not registered in the repository"));
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> processProduct(ProductType updatedProduct) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (updatedProduct == null) {
            future.completeExceptionally(new IllegalArgumentException("Product cannot be null"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(updatedProduct.getProductCode()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection(COLLECTION_NAME).document(updatedProduct.getProductCode())
                                .set(updatedProduct) // Overwrites with updated fields
                                .addOnSuccessListener(aVoid -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    } else {
                        future.completeExceptionally(new IllegalStateException("Product type with id " + updatedProduct.getProductCode() + " is not a registered product type."));
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<HashMap<String, ProductType>> getProducts() {
        CompletableFuture<HashMap<String, ProductType>> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashMap<String, ProductType> productsMap = new HashMap<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        ProductType product = document.toObject(ProductType.class);
                        productsMap.put(product.getProductCode(), product);
                    }
                    future.complete(productsMap);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     */
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