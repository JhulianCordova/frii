package com.cor.frii;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cor.frii.pojo.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class MisPedidosAdapter extends RecyclerView.Adapter<MisPedidosAdapter.viewHolder> implements View.OnClickListener {

    private List<Order> data;
    private Context context;
    private View view;
    private View.OnClickListener listener;


    public static int nuMin = 10;
    public static int numSeg = 0;
    public static int numHor = 0;

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
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {


        holder.titlePedido.setText(data.get(position).getDate());
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
                holder.cancelar.setEnabled(false);
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

        if (data.get(position).getStatus().equals("cancel")) {
            holder.cancelar.setEnabled(false);
            holder.cancelar.setBackgroundColor(Color.GRAY);
        } else {

            if (data.get(position).getStatus().equals("wait")) {
                holder.timer = new CountDownTimer(100000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        NumberFormat f = new DecimalFormat("00");
                        long hour = (millisUntilFinished / 3600000) % 24;
                        long min = (millisUntilFinished / 60000) % 60;
                        long sec = (millisUntilFinished / 1000) % 60;

                        holder.timerAuto.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    }

                    @Override
                    public void onFinish() {
                        tiempoCancelar(data.get(position).getId());
                    }
                }.start();
            }

            holder.cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mensajeConfirmacion(data.get(position).getId());
                    notifyDataSetChanged();

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView titlePedido, estadoPedido, detallePedido;
        Button llamar, mensaje, cancelar;
        CountDownTimer timer;
        TextView timerAuto;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titlePedido = itemView.findViewById(R.id.TitlePedidos);
            estadoPedido = itemView.findViewById(R.id.EstadoPedido);
            detallePedido = itemView.findViewById(R.id.DetallePedido);

            llamar = itemView.findViewById(R.id.ButtonLLamarPedido);
            mensaje = itemView.findViewById(R.id.ButtonMensajePedido);
            cancelar = itemView.findViewById(R.id.ButtonCancelarPedido);
            timerAuto = itemView.findViewById(R.id.timerAuto);
        }
    }

    private void mensajeConfirmacion(final int idOrden) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Desea realmente cancelar el pedido")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tiempoCancelar(idOrden);
                    }


                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
    }

    private void tiempoCancelar(int idOrden) {
        final String url = "http://34.71.251.155/api/order/client/cancel/";
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", idOrden);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 200) {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley get", "error voley" + error.toString());
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                System.out.println(res);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        queue.add(jsonObjectRequest);
    }

}
