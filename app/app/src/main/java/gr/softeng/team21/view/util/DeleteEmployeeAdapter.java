package gr.softeng.team21.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Employee;

/**
 * Adapter for displaying a list of employees for the deletion process.
 * Handles item click events to navigate to the confirmation screen.
 */
public class DeleteEmployeeAdapter extends RecyclerView.Adapter<DeleteEmployeeAdapter.ViewHolder> {

    private final List<Employee> employees;
    private final OnEmployeeClickListener listener;

    /**
     * Interface for handling click events on employee list items.
     */
    public interface OnEmployeeClickListener {
        void onEmployeeClick(Employee employee);
    }

    public DeleteEmployeeAdapter(List<Employee> employees, OnEmployeeClickListener listener) {
        this.employees = employees;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee_delete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employees.get(position);

        String fullName = employee.getFirstname() + " " + employee.getLastname();
        holder.txtItemEmpName.setText(fullName);
        holder.txtItemEmpUsername.setText("@" + employee.getUsername());

        // Trigger the listener when the entire card is clicked
        holder.itemView.setOnClickListener(v -> listener.onEmployeeClick(employee));
    }

    @Override
    public int getItemCount() {
        return (employees != null) ? employees.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemEmpName, txtItemEmpUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemEmpName = itemView.findViewById(R.id.txtItemEmpName);
            txtItemEmpUsername = itemView.findViewById(R.id.txtItemEmpUsername);
        }
    }
}