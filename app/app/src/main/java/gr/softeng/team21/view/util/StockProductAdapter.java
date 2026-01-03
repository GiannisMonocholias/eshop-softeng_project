package gr.softeng.team21.view.util;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

public class StockProductAdapter extends RecyclerView.Adapter<StockProductAdapter.ViewHolder> {

    private final List<CartItem> cartItems;

    public StockProductAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        ProductType product = item.getProductType();

        holder.txtCodeValue.setText(product.getProductCode());
        holder.txtNameValue.setText(product.getProductname());

        if (product.getPrice() != null) {
            holder.txtPriceValue.setText(product.getPrice().getAmount() + " €");
        } else {
            holder.txtPriceValue.setText("-");
        }

        int reqQty = item.getQuantity();
        int stockQty = ProductsWareHouseDAOMemory.getInstance().getProductStock(product);

        holder.txtReqValue.setText(String.valueOf(reqQty));
        holder.txtStockValue.setText(String.valueOf(stockQty));

        if (reqQty > stockQty) {
            holder.txtStockValue.setTextColor(Color.RED);
        } else {
            holder.txtStockValue.setTextColor(Color.parseColor("#2E7D32"));
        }
    }

    @Override
    public int getItemCount() {
        return (cartItems != null) ? cartItems.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodeValue, txtNameValue, txtPriceValue, txtReqValue, txtStockValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCodeValue = itemView.findViewById(R.id.txtItemProductStockProductCodeValue);
            txtNameValue = itemView.findViewById(R.id.txtItemProductStockProductNameValue);
            txtPriceValue = itemView.findViewById(R.id.txtItemProductStockSubTotalValue);
            txtReqValue = itemView.findViewById(R.id.txtItemProductStockRequestedQuantityValue);
            txtStockValue = itemView.findViewById(R.id.txtItemProductStockStockQuantityValue);
        }
    }
}