package gr.softeng.team21.view.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.ProductType;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<ProductType> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(ProductType product);
    }

    public ProductAdapter(ArrayList<ProductType> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {//tha to allkso
        ProductType product = productList.get(position);
        holder.txtProductName.setText(product.getProductName());
        String codeimg = product.getProductCode().toLowerCase().replace("-", "_");
        Context context = holder.itemView.getContext();
        int imageResId = context.getResources().getIdentifier(codeimg, "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.productImage.setImageResource(imageResId);
        } else {
            holder.productImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        if (product.getPrice() != null) {
            holder.txtProductPrice.setText(product.getPrice().toString() + " €");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        TextView txtProductPrice;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductNameItem);
            txtProductPrice = itemView.findViewById(R.id.txtProductPriceItem);
            productImage=itemView.findViewById(R.id.imgProductItem);
        }
    }
}