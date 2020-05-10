package com.cor.frii;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
    private View.OnClickListener listener;

    public MisPedidosAdapter(List<Order> data) {
        this.data = data;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) listener.onClick(v);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
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
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        holder.titlePedido.setText(data.get(position).getDate());
        holder.cancelar.setEnabled(false);
        switch (data.get(position).getStatus()) {
            case "wait":
                holder.estadoPedido.setText("En espera");
                break;
            case "refuse":
                holder.estadoPedido.setText("Rechazado");
                break;
            case "confirm":
                holder.estadoPedido.setText("Confirmado");
                holder.cancelar.setEnabled(true);
                break;
            case "delivered":
                holder.estadoPedido.setText("Entregado");
                break;
            default:
                holder.estadoPedido.setText("Cancelado");
                break;
        }
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < data.get(position).getDetalles().size(); i++) {
            details.append(data.get(position).getDetalles().get(i)).append("\n");
        }
        holder.detallePedido.setText(details.toString());

        if (data.get(position).getPhone() != null && !data.get(position).getPhone().equals("")) {
            holder.llamar.setEnabled(true);
            holder.mensaje.setEnabled(true);
            holder.llamar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dial = "tel: " + data.get(position).getPhone();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(dial));
                    context.startActivity(intent);
                }
            });

            holder.mensaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto: " + data.get(position).getPhone()));
                    context.startActivity(intent);
                }
            });
        } else {
            holder.llamar.setEnabled(false);
            holder.llamar.setBackgroundColor(Color.GRAY);
            holder.mensaje.setEnabled(false);
            holder.mensaje.setBackgroundColor(Color.GRAY);
        }
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

            llamar = itemView.findViewById(R.id.ButtonLLamarPedido);
            mensaje = itemView.findViewById(R.id.ButtonMensajePedido);
            cancelar = itemView.findViewById(R.id.ButtonCancelarPedido);
        }
    }
}
