package com.cor.frii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cor.frii.pojo.Order;

import java.util.List;

public class MisPedidosAdapter extends RecyclerView.Adapter<MisPedidosAdapter.viewHolder> implements View.OnClickListener {

    private List<Order> data;
    private Context context;
    private View view;

    public MisPedidosAdapter(List<Order> data) {
        this.data = data;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_mispedidos, parent, false);
        view.setOnClickListener(this);
        context = parent.getContext();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.titlePedido.setText(data.get(position).getDate());

        if (data.get(position).getStatus().equals("wait")) {
            holder.estadoPedido.setText("En espera");
        }
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < data.get(position).getDetalles().size(); i++) {
            details.append(data.get(position).getDetalles().get(i)).append("\n");
        }
        holder.detallePedido.setText(details.toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView titlePedido, estadoPedido, detallePedido;
        Button llamar, mensaje, cancelar;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titlePedido = itemView.findViewById(R.id.TitlePedidos);
            estadoPedido = itemView.findViewById(R.id.EstadoPedido);
            detallePedido = itemView.findViewById(R.id.DetallePedido);
        }
    }
}
