package gr.softeng.team21.view.admin.requests.adminRequestsActivity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.firebasedao.UpdateRequestDAOFirebase;
import gr.softeng.team21.view.util.AdminRequestsAdapter;

/**
 * Activity that presents a visual list (RecyclerView) of all requests
 * submitted by the administrator for catalogue updates.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class AdminRequestsActivity extends AppCompatActivity implements AdminRequestsView {

    private AdminRequestsPresenter presenter;
    private RecyclerView recyclerView;

    /**
     * Initializes the layout, sets up the RecyclerView, and requests data from the presenter.
     * @param savedInstanceState The previously saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_requests);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dependency Injection for Firebase DAO
        UpdateRequestDAO updateRequestDAO = new UpdateRequestDAOFirebase();
        presenter = new AdminRequestsPresenter(this, updateRequestDAO);

        // Fetch data immediately upon creation
        presenter.loadRequests();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showRequests(List<CatalogueUpdateRequest> requests) {
        runOnUiThread(() -> {
            AdminRequestsAdapter adapter = new AdminRequestsAdapter(requests);
            recyclerView.setAdapter(adapter);
        });
    }
}