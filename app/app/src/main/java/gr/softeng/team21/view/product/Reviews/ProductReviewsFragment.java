package gr.softeng.team21.view.product.Reviews;

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
import gr.softeng.team21.dao.ProductReviewsDao;
import gr.softeng.team21.domain.ProductReview;
import gr.softeng.team21.firebasedao.ProductReviewsDaoFirebase;
import gr.softeng.team21.view.util.ReviewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductReviewsFragment extends Fragment implements ProductReviewsView {

    private String productCode;
    private RecyclerView recyclerView;
    private ProductReviewsPresenter presenter;
    private ReviewAdapter adapter;


    public ProductReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductReviewsFragment newInstance(String productCode) {
        ProductReviewsFragment fragment = new ProductReviewsFragment();
        Bundle args = new Bundle();
        args.putString("PRODUCT_CODE", productCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productCode = getArguments().getString("PRODUCT_CODE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_reviews, container, false);
        view.setClickable(true);
        view.setFocusable(true);

        recyclerView = view.findViewById(R.id.recyclerViewReviews);
        ProductReviewsDao productReviewsDao=new ProductReviewsDaoFirebase();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        presenter = new ProductReviewsPresenter(this, productReviewsDao);
        presenter.loadReviews(productCode);
        return view;
    }

    @Override
    public void showReviews(ArrayList<ProductReview> reviews) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                adapter = new ReviewAdapter(reviews);
                recyclerView.setAdapter(adapter);
            });
        }
    }

    @Override
    public void showMessage(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            );
        }
    }
    }


