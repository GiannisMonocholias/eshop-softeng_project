package gr.softeng.team21.view.employee.deliverer.delivererMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.view.employee.deliverer.delivererOrdersList.DelivererOrdersListActivity;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;

/**
 * Main Activity representing the Deliverer's dashboard.
 * Manages UI interactions and implements the {@link DelivererMenuView}
 * to handle navigation and feedback as directed by the Presenter.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererMenuActivity extends AppCompatActivity implements DelivererMenuView {

    private DelivererMenuPresenter presenter;
    private static final String EMP_ID_EXTRA = "DELIVERER_ID";
    private String employeeId;

    /**
     * Initializes the layout, binds UI listeners, and notifies the presenter
     * that the view has been created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deliverer_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityDelivererDashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new DelivererMenuPresenter(this, EmployeeDAOMemory.getInstance());

        employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        presenter.onViewCreated(employeeId);


        findViewById(R.id.btnDelivererMenuOrdersList).setOnClickListener(v ->
                presenter.onOrdersListSelected(employeeId)
        );

        findViewById(R.id.btnDelivererMenuProcessAccount).setOnClickListener(v ->
                presenter.onProcessAccountSelected(employeeId)
        );

        findViewById(R.id.btnDelivererMenuDeleteAccount).setOnClickListener(v ->
                presenter.onDeleteAccountSelected()
        );

        findViewById(R.id.btnDelivererMenuLogout).setOnClickListener(v -> {
            finish();
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEmployeeName(String fullname) {
        ((TextView) findViewById(R.id.txtDelivererMenuName)).setText(fullname);
    }

    /**
     * {@inheritDoc}
     * The navigation is executed via Intent object, with employee id as an intent extra
     */
    @Override
    public void navigateToOrdersList(String employeeId) {
        Intent intent = new Intent(DelivererMenuActivity.this, DelivererOrdersListActivity.class);
        intent.putExtra(EMP_ID_EXTRA, employeeId);
        startActivity(intent);
    }


    /**
     * {@inheritDoc}
     * Shows an Android {@link AlertDialog} with confirmation buttons.
     */
    @Override
    public void showDeleteAccountConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Διαγραφή Λογαριασμού")
                .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε τον λογαριασμό σας; Αυτή η ενέργεια δεν μπορεί να αναιρεθεί.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onDeleteAccountConfirmed(employeeId);
                    }
                })
                .setNegativeButton("ΟΧΙ", null)
                .show();
    }

    /**
     * {@inheritDoc}
     * Clears the activity stack to ensure security after logout or deletion.
     */
    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
        finish();
    }

    /**
     * {@inheritDoc}
     * The navigation is executed via Intent object, with employee id (user id) as an intent extra
     */
    @Override
    public void navigateToProcessAccount(String employeeId){
        Intent intent = new Intent(this, UserEditDataActivity.class);

        intent.putExtra("user_id",employeeId);

        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}