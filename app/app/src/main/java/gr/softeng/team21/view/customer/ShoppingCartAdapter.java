package gr.softeng.team21.view.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CartItem;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>{
    private List<CartItem> cartItems;
    private CartListener listener;
    public interface CartListener {
        void plus(CartItem item);
        void minus(CartItem item);
        void delete(CartItem item);
    }
    public ShoppingCartAdapter(List<CartItem> cartItems,CartListener listener) {
        this.cartItems = cartItems;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingcart_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.txtName.setText(item.getProductType().getProductname());
        holder.txtPrice.setText(String.format("%.2f €", item.getProductType().getPrice().getAmount()));
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> listener.plus(item));
        holder.btnMinus.setOnClickListener(v -> listener.minus(item));
        holder.btnDelete.setOnClickListener(v -> listener.delete(item));
    }



    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtQuantity,txtName,txtPrice;
        public Button btnMinus,btnPlus;
        public ImageButton btnDelete;
        public ViewHolder(@NonNull View rowView) {
            super(rowView);
            txtName = rowView.findViewById(R.id.txtCustomerCartItemActivityName);
            txtPrice = rowView.findViewById(R.id.txtCustomerCartItemActivityPrice);
            txtQuantity = rowView.findViewById(R.id.txtCustomerCartItemActivityQuantity);

            btnPlus = rowView.findViewById(R.id.btnCustomerCartItemActivityPlus);
            btnMinus = rowView.findViewById(R.id.btnCustomerCartItemActivityMinus);
            btnDelete = rowView.findViewById(R.id.btnCustomerCartItemActivityDelete);
        }
    }
}
