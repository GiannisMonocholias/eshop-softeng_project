package gr.softeng.team21.view.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailMessage;

/**
 * RecyclerView Adapter for displaying internal email messages.
 * Features include visual styling for unread messages, date formatting,
 * and real-time filtering (searching) based on subject or sender.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {

    private List<EmailMessage> emailList;
    private List<EmailMessage> emailListFull; // Backup list for filtering
    private OnEmailClickListener listener;

    /**
     * Interface definition for a callback to be invoked when an email item is clicked.
     */
    public interface OnEmailClickListener {
        /**
         * Triggered when the user selects an email from the list.
         * @param email The EmailMessage object associated with the clicked item.
         */
        void onEmailClick(EmailMessage email);
    }

    /**
     * Initializes the adapter with a list of emails and a click listener.
     * @param emailList Initial list of email messages to display.
     * @param listener  Listener to handle email selection events.
     */
    public EmailAdapter(List<EmailMessage> emailList, OnEmailClickListener listener) {
        this.emailList = emailList;
        this.emailListFull = new ArrayList<>(emailList);
        this.listener = listener;
    }

    /**
     * Updates the adapter's data set and refreshes the UI.
     * @param newEmails The updated list of email messages.
     */
    public void updateData(List<EmailMessage> newEmails) {
        this.emailList = new ArrayList<>(newEmails);
        this.emailListFull = new ArrayList<>(newEmails);
        notifyDataSetChanged();
    }

    /**
     * Inflates the layout for individual email items.
     * @return A new EmailViewHolder holding the item view.
     */
    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_email, parent, false);
        return new EmailViewHolder(view);
    }

    /**
     * Binds email data to the UI components.
     * Applies specific styling (bold text and background highlight) if the email is unread.
     * @param holder The ViewHolder to update.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        EmailMessage email = emailList.get(position);

        // Visual distinction for unread messages
        if (!email.isRead()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E3F2FD")); // Light blue tint
            holder.txtSubject.setTypeface(null, Typeface.BOLD);
            holder.txtSender.setTypeface(null, Typeface.BOLD);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.txtSubject.setTypeface(null, Typeface.NORMAL);
            holder.txtSender.setTypeface(null, Typeface.NORMAL);
        }

        // Set text data
        holder.txtSender.setText(email.getFrom().toString());
        holder.txtSubject.setText(email.getSubject());
        holder.txtPreview.setText(email.getBody());

        Date dateSent = email.getDateSent();
        holder.txtDate.setText(dateSent != null ? dateSent.toString() : "");

        // Set click listener for the entire row
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEmailClick(email);
            }
        });
    }

    /**
     * @return The current number of items in the (filtered) list.
     */
    @Override
    public int getItemCount() {
        return emailList.size();
    }

    /**
     * Filters the email list based on a search query.
     * Searches within both the subject line and the sender's address.
     * @param text The search query entered by the user.
     */
    public void filter(String text) {
        emailList.clear();

        if (text.isEmpty()) {
            emailList.addAll(emailListFull);
        } else {
            String query = text.toLowerCase().trim();

            for (EmailMessage item : emailListFull) {
                if (item.getSubject().toLowerCase().contains(query) ||
                        item.getFrom().toString().toLowerCase().contains(query)) {
                    emailList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for caching UI component references.
     */
    public static class EmailViewHolder extends RecyclerView.ViewHolder {
        TextView txtSender, txtSubject, txtDate, txtPreview;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSender = itemView.findViewById(R.id.txtSenderName);
            txtDate = itemView.findViewById(R.id.txtItemOrderSubmissionDateValue);
            txtSubject = itemView.findViewById(R.id.txtEmailSubject);
            txtPreview = itemView.findViewById(R.id.txtEmailPreview);
        }
    }
}