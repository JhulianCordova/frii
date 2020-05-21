package com.cor.frii;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import com.cor.frii.persistence.DatabaseClient;
import com.cor.frii.persistence.Session;
import com.cor.frii.persistence.entity.Acount;
import com.cor.frii.pojo.Order;

import com.cor.frii.utils.AgendarPedido;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MisPedidosAdapter extends RecyclerView.Adapter<MisPedidosAdapter.viewHolder> implements View.OnClickListener {

    private List<Order> data;
    private Context context;
    private View view;
    private View.OnClickListener listener;
    ViewGroup viewGroup;
    private Socket socket;
    public String HOST_NODEJS = "http://34.71.251.155:9000";

    public static final String TAG = "firebase";
    private final static int NOTIFICATION_ID = 0;
    private final static String CHANNEL_ID = "NOTIFICACION";

    private String buttonEstado = "cancelar";

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
        viewGroup = parent;
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {

        holder.titlePedido.setText(data.get(position).getDate());
        switch (data.get(position).getStatus()) {
            case "wait":
                holder.estadoPedido.setText("En espera");
                holder.cancelar.setText("Cancelar");
                break;
            case "refuse":
                holder.estadoPedido.setText("Rechazado");
                break;
            case "confirm":
                holder.estadoPedido.setText("Confirmado");
                holder.cancelar.setEnabled(true);
                holder.cancelar.setText("Cancelar");
                break;
            case "delivered":
                holder.estadoPedido.setText("Entregado");
                holder.cancelar.setText("Calificar");
                holder.cancelar.setBackgroundColor(Color.rgb(23, 162, 184));
                break;
            default:
                holder.estadoPedido.setText("Cancelado");
                holder.cancelar.setText("Repedir");
                holder.cancelar.setBackgroundColor(Color.rgb(40, 167, 69));
                holder.mensaje.setVisibility(View.GONE);
                holder.llamar.setVisibility(View.GONE);

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
            holder.cancelar.setText("Repedir");
            holder.cancelar.setBackgroundColor(Color.rgb(40, 167, 69));
        } else {

            if (data.get(position).getStatus().equals("wait")) {
                /*holder.timer = new CountDownTimer(100000, 1000) {
                    @SuppressLint("SetTextI18n")
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
                        notificacionCancelado("Pedido cancelado", "El pedido fue cancelado, el tiempo expiro");
                    }
                }.start();*/
            }

            holder.cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.cancelar.getText().equals("Cancelar")) {
                        mensajeConfirmacion(data.get(position).getId());
                        notifyDataSetChanged();
                    } else if (holder.cancelar.getText().equals("Calificar")) {
                        AgendarPedido agendarPedido = new AgendarPedido(context, data.get(position).getId(),
                                data.get(position).getCalification());
                        agendarPedido.show();
                    } else if (holder.cancelar.getText().equals("Repedir")) {
                        Toast.makeText(context, "En implementacion", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

        if (data.get(position).getStatus().equals("confirm")) {
            notificacionCancelado("Pedido ha sido tomado", "El pedido que realizo ha sido tomado por un proveedor");
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        TextView titlePedido, estadoPedido, detallePedido;
        Button llamar, mensaje, cancelar;
        CountDownTimer timer;
        TextView timerAuto;

        viewHolder(@NonNull View itemView) {
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
                                initSocket();
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

    private void notificacionCancelado(String title, String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, noBuilder.build());
    }


    private void initSocket() {

        int id_user = new Session(context).getToken();
        JSONObject data = new JSONObject();
        Acount cuenta = DatabaseClient.getInstance(context)
                .getAppDatabase()
                .getAcountDao()
                .getUser(id_user);

        final JSONObject json_connect = new JSONObject();
        IO.Options opts = new IO.Options();
        // opts.forceNew = true;
        opts.reconnection = true;
        opts.query = "auth_token=thisgo77";
        try {
            json_connect.put("ID", "US01");
            json_connect.put("TOKEN", cuenta.getToken());
            json_connect.put("ID_CLIENT", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            socket = IO.socket(HOST_NODEJS, opts);
            socket.connect();
            // SOCKET.io().reconnectionDelay(10000);
            Log.d(TAG, "Node connect ok");
            //conect();
        } catch (URISyntaxException e) {
            Log.d(TAG, "Node connect error");
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "emitiendo new conect");
                JSONObject data = new JSONObject();
                int id = new Session(context).getToken();
                Acount cuenta = DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .getAcountDao()
                        .getUser(id);
                try {
                    data.put("ID", cuenta.getId());
                    data.put("type", "client");
                    Log.d(TAG, "conect " + data.toString());
                    socket.emit("new connect", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Log.d(TAG, "SERVER connect " + date);


            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Log.d(TAG, "SERVER disconnect " + date);
            }
        });

        socket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String my_date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Log.d(TAG, "SERVER reconnect " + my_date);
            }
        });

        socket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String my_date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Log.d(TAG, "SERVER timeout " + my_date);
            }
        });

        socket.on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String my_date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Log.d(TAG, "SERVER reconnecting " + my_date);
            }
        });

        JSONObject datas = new JSONObject();
        try {
            datas.put("id", id_user);
            datas.put("token", cuenta.getToken());
            Log.d(TAG, "conect " + datas.toString());
            socket.emit("status order", datas);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
