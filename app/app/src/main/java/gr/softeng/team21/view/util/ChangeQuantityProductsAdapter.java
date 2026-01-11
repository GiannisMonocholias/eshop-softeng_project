package gr.softeng.team21.view.util;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

public class ChangeQuantityProductsAdapter extends RecyclerView.Adapter<ChangeQuantityProductsAdapter.ViewHolder> {

    private final List<ProductType> products;

    public ChangeQuantityProductsAdapter (List<ProductType> products){
        this.products = products;
    }

    @NonNull
    @Override
    public ChangeQuantityProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_quantity, parent, false);
        return new ChangeQuantityProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeQuantityProductsAdapter.ViewHolder holder, int position) {

        ProductType product = products.get(position);

        holder.txtProductName.setText(product.getProductname());
        holder.txtProductCode.setText("Κωδικός: " + product.getProductCode());

        holder.edtAvailableQuantity.setText(
                String.valueOf(ProductsWareHouseDAOMemory.getInstance().getProductStock(product))
        );

        holder.edtAvailableQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String value = holder.edtAvailableQuantity.getText().toString();
                if (!value.isEmpty()) {
                    ProductsWareHouseDAOMemory.getInstance().increaseProductStock(product , Integer.parseInt(value));
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return (products != null) ? products.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName , txtProductCode;
        EditText edtAvailableQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductCode = itemView.findViewById(R.id.txtProductCode);
            edtAvailableQuantity = itemView.findViewById(R.id.edtAvailableQuantity);

        }
    }


}
