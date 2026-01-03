package gr.softeng.team21.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.EmailMessage;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {

    private List<EmailMessage> emailList;
    private OnEmailClickListener listener;

    public interface OnEmailClickListener {
        void onEmailClick(EmailMessage email);
    }

    public EmailAdapter(List<EmailMessage> emailList, OnEmailClickListener listener) {
        this.emailList = emailList;
        this.listener = listener;
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

        holder.txtSender.setText(email.getFrom().toString());

        holder.txtSubject.setText(email.getSubject());
        holder.txtDate.setText("12:30 pm");
        holder.txtPreview.setText(email.getBody());

        holder.itemView.setOnClickListener(v -> listener.onEmailClick(email));

    }

    @Override
    public int getItemCount() {
        return emailList.size();
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