package gr.softeng.team21.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.ProductReview;

/**
 * Adapter responsible for displaying the list of customer reviews within a RecyclerView.
 * It manages the binding of {@link ProductReview} data (username, date, comment, rating) to the UI views.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<ProductReview> reviews;

    /**
     * Initializes the adapter with the list of reviews.
     * @param reviews The list of reviews to display.
     */
    public ReviewAdapter(ArrayList<ProductReview> reviews) {
        this.reviews = reviews;
    }

    /**
     * {@inheritDoc}
     * Inflates the layout for individual review items.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_review_list_item, parent, false));
    }

    /**
     * {@inheritDoc}
     * Binds the data of a specific {@link ProductReview} to the view elements.
     * @param holder The ViewHolder which should be updated to represent the contents of the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductReview review = reviews.get(position);
        holder.txtUsername.setText(review.getCustomerId());
        holder.txtDate.setText(review.getReviewDate().toString());
        holder.txtComment.setText(review.getComment());
        holder.ratingBar.setRating((float) review.getStars());
    }

    /**
     * {@inheritDoc}
     * Returns the total number of reviews in the list.
     */
    @Override
    public int getItemCount() {
        return reviews.size();
    }

    /**
     * ViewHolder class that holds references to the UI components of a review item layout.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtUsername, txtDate, txtComment;
        public RatingBar ratingBar;

        /**
         * Initializes the ViewHolder and binds the UI components.
         * @param rowView The view representing a single row in the recycler view.
         */
        public ViewHolder(@NonNull View rowView) {
            super(rowView);
            txtUsername = rowView.findViewById(R.id.txtReviewUsername);
            txtDate = rowView.findViewById(R.id.txtReviewDate);
            txtComment = rowView.findViewById(R.id.txtReviewComment);
            ratingBar = rowView.findViewById(R.id.ratingBarReview);
        }
    }
}