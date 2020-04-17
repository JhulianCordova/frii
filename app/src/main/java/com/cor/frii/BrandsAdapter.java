package com.cor.frii;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cor.frii.pojo.Brands;

import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.viewHolder> implements View.OnClickListener {

    List<Brands> brands;

    private View.OnClickListener listener;

    public BrandsAdapter(List<Brands> brands) {
        this.brands = brands;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_brands,parent,false);
        view.setOnClickListener(this);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.BrandsTitle.setText(brands.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {

        this.listener=onClickListener;

    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView BrandsTitle;
        ImageView BrandsImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            BrandsTitle=itemView.findViewById(R.id.BrandsTitle);
        }
    }
}
