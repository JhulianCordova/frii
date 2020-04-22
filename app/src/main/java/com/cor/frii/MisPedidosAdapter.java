package com.cor.frii;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MisPedidosAdapter extends RecyclerView.Adapter<MisPedidosAdapter.viewHolder> implements View.OnClickListener {

    List<String> data;

    public MisPedidosAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_mispedidos,parent,false);
        view.setOnClickListener(this);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.titlePedido.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView titlePedido,estadoPedido,detallePedido;
        Button llamar,mensaje,cancelar;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titlePedido=itemView.findViewById(R.id.TitlePedidos);
        }
    }
}
