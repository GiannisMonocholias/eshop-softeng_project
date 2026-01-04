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
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailMessage;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {

    private List<EmailMessage> emailList;
    private List<EmailMessage> emailListFull;
    private OnEmailClickListener listener;

    public interface OnEmailClickListener {
        void onEmailClick(EmailMessage email);
    }

    public EmailAdapter(List<EmailMessage> emailList, OnEmailClickListener listener) {
        this.emailList = emailList;
        this.emailListFull = new ArrayList<>(emailList);
        this.listener = listener;
    }

    public void updateData(List<EmailMessage> newEmails) {
        this.emailList = new ArrayList<>(newEmails);
        this.emailListFull = new ArrayList<>(newEmails);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_email, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        EmailMessage email = emailList.get(position);


        if (!email.isRead()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E3F2FD"));
            holder.txtSubject.setTypeface(null, Typeface.BOLD);
            holder.txtSender.setTypeface(null, Typeface.BOLD);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.txtSubject.setTypeface(null, Typeface.NORMAL);
            holder.txtSender.setTypeface(null, Typeface.NORMAL);
        }


        holder.txtSender.setText(email.getFrom().toString());
        holder.txtSubject.setText(email.getSubject());
        holder.txtDate.setText("12:30 pm");
        holder.txtPreview.setText(email.getBody());

        Date dateSent = email.getDateSent();

        holder.txtDate.setText(dateSent != null? dateSent.toString():"");

            holder.txtDate.setText(dateSent != null? dateSent.toString():"");


        holder.itemView.setOnClickListener(v -> listener.onEmailClick(email));
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public void filter(String text) {
        emailList.clear();

        if (text.isEmpty()) {
            emailList.addAll(emailListFull);
        } else {
            text = text.toLowerCase().trim();

            for (EmailMessage item : emailListFull) {
                if (item.getSubject().toLowerCase().contains(text) ||
                        item.getFrom().toString().toLowerCase().contains(text)) {

                    emailList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

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