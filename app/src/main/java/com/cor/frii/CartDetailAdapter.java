package com.cor.frii;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cor.frii.pojo.CartDetail;

import java.util.List;

public class CartDetailAdapter extends RecyclerView.Adapter<CartDetailAdapter.viewHolder> implements View.OnClickListener{

    List<CartDetail> cartDetails;

    public CartDetailAdapter(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_detail,parent,false);
        view.setOnClickListener(this);
        return  new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.cartTitle.setText(cartDetails.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return cartDetails.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView cartTitle,cartPrecioU,cartSubtotal;
        EditText cartCantidad;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cartTitle=itemView.findViewById(R.id.cartProductTitle);

        }
    }
}
