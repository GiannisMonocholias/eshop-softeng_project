package gr.softeng.team21.view.admin.requests;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;
import gr.softeng.team21.view.util.AdminRequestsAdapter;

/**
 * Activity that shows all the requests submitted by the admin.
 */

public class AdminRequestsActivity extends AppCompatActivity implements AdminRequestsView {


    //Presenter
    private AdminRequestsPresenter presenter;

    //Adapter connects requests' list with RecyclerView
    AdminRequestsAdapter adapter;

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

        presenter = new AdminRequestsPresenter(this , UpdateRequestDAOMemory.getInstance());

        RecyclerView recyclerView = findViewById(R.id.rvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<CatalogueUpdateRequest> availableRequests = presenter.loadRequests();

        adapter = new AdminRequestsAdapter(availableRequests);
        recyclerView.setAdapter(adapter);


    }
}