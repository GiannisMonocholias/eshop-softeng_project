package gr.softeng.team21.view.customer.OrdersHistoric;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.firebasedao.OrderDAOFirebase;
import gr.softeng.team21.view.util.OrderHistoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerOrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerOrderHistoryFragment extends Fragment implements CustomerOrderHistoryView {


    private static final String ARG_CUSTOMER_ID = "param1";

   private RecyclerView recyclerView;
   private OrderHistoryAdapter adapter;
   private String customerId;
   private  CustomerOrderHistoryPresenter presenter;

    private String mParam1;
    private String mParam2;

    public CustomerOrderHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CustomerOrderHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerOrderHistoryFragment newInstance(String customerId) {
        CustomerOrderHistoryFragment fragment = new CustomerOrderHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CUSTOMER_ID, customerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerId = getArguments().getString(ARG_CUSTOMER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_order_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewOrderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        presenter=new CustomerOrderHistoryPresenter(this,new OrderDAOFirebase());
        presenter.loadOrders(customerId);
        return view;
    }


    @Override
    public void showOrders(ArrayList<Order> ordersList) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                adapter = new OrderHistoryAdapter(ordersList,selectedOrder ->{
                    presenter.OrderClicked(selectedOrder);
                });
                recyclerView.setAdapter(adapter);
            });
        }
    }
    /** {@inheritDoc} */
    @Override
    public void showError(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show());
        }
    }

}