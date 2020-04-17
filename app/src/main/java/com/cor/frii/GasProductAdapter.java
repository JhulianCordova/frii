package com.cor.frii;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cor.frii.pojo.Product;

import java.util.List;

public class GasProductAdapter extends RecyclerView.Adapter<GasProductAdapter.viewHolder> implements View.OnClickListener {


    List<Product> products;

    public GasProductAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gas_product,parent,false);
        view.setOnClickListener(this);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.gasProductTitle.setText(products.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView gasProductTitle;
        ImageView gasProductImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            gasProductTitle=itemView.findViewById(R.id.ProductGasTitle);
        }
    }
}
