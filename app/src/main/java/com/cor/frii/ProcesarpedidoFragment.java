package com.cor.frii;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.cor.frii.persistence.entity.ECart;
import com.cor.frii.utils.GpsUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ProcesarpedidoFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationSource.OnLocationChangedListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String TAG = "GAS";
    private static final int DEFAULT_ZOOM = 16;
    private Location mLastKnownLocation;
    private GoogleMap map;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private Marker currentMarker;
    private LatLng startLng;
    private Address address;
    private String baseURL = "http://34.71.251.155/api";
    private String HOST_NODEJS = "http://34.71.251.155:9000";
    private Thread thread = null;

    private Context context;

    private LatLng point_move;
    private Socket socket;

    private TextView lblDireccion;

    private OnFragmentInteractionListener mListener;

    public ProcesarpedidoFragment() {
        // Required empty public constructor
    }


    public static ProcesarpedidoFragment newInstance(String param1, String param2) {
        ProcesarpedidoFragment fragment = new ProcesarpedidoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        initSocket();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_procesarpedido, container, false);
        lblDireccion = view.findViewById(R.id.lblDireccion);
        ImageView markerIcon = view.findViewById(R.id.markerIcon);

        Button procesarPedido = view.findViewById(R.id.ButtonConfirmarPedido);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map_pedidos);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        procesarPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmarPedido();
                Intent intent = new Intent(getContext(), PedidosActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMarkerClickListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        getLocationPermission();
        getDeviceLocation();
        updateLocationUI();

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (currentMarker != null) {
                    currentMarker.remove();
                }

                startLng = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                String direccion = getStringAddress(cameraPosition.target.latitude, cameraPosition.target.longitude);
                lblDireccion.setText(direccion);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;


                }
            }
        }
        updateLocationUI();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getContext(), "Location not found", Toast.LENGTH_LONG).show();
        } else {
            startLng = new LatLng(location.getLatitude(), location.getLongitude());
            lblDireccion.setText(getStringAddress(startLng.latitude, startLng.longitude));
        }

        if (currentMarker == null) {
            MarkerOptions options = new MarkerOptions();
            options.position(startLng);
            currentMarker = map.addMarker(options);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLng, 15));
        } else {
            currentMarker.setPosition(startLng);
        }

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = map.getCameraPosition().target;
                if (currentMarker != null) {
                    currentMarker.remove();
                    map.addMarker(new MarkerOptions().position(center).title("New posicition"));
                    startLng = currentMarker.getPosition();
                    System.out.println(startLng);
                    String direccion = getStringAddress(startLng.latitude, startLng.longitude);
                    lblDireccion.setText(direccion);
                }
            }
        });

        if (currentMarker != null) {
            currentMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        currentMarker = map.addMarker(markerOptions);

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));

    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                if (currentMarker != null) currentMarker.remove();

                                LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                                startLng = latLng;
                                String direccion = getStringAddress(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                lblDireccion.setText(direccion);

                            } else {
                                new GpsUtils(Objects.requireNonNull(getActivity())).turnGPSOn(new GpsUtils.onGpsListener() {
                                    @Override
                                    public void gpsStatus(boolean isGPSEnable) {
                                        if (isGPSEnable) {
                                            //---------------------------------
                                            updateLocationUI();
                                            getDeviceLocation();
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            } else {
                updateLocationUI();
                getDeviceLocation();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }


    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
        }
    }

    private String getStringAddress(Double lat, Double lng) {
        String address = "";
        String city;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    // Confirmar pedido
    private void confirmarPedido() {
        //Obtener el token del cliente
        final Acount acount = DatabaseClient.getInstance(getContext())
                .getAppDatabase()
                .getAcountDao()
                .getUser(new Session(getContext()).getToken());

        if (lblDireccion.getText().toString().length() == 0 && lblDireccion.getText().toString().equals("")) {
            return;
        }

        JSONObject jsonObject = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        try {
            jsonObject.put("latitud", String.valueOf(point_move.latitude));
            jsonObject.put("longitud", String.valueOf(point_move.longitude));
            jsonObject.put("client_id", new Session(getContext()).getToken());

            JSONArray jsonArray = new JSONArray();
            List<ECart> eCarts = DatabaseClient.getInstance(getContext())
                    .getAppDatabase()
                    .getCartDao()
                    .getCarts();

            if (eCarts != null) {
                for (ECart e : eCarts) {
                    JSONObject orders_detal = new JSONObject();
                    orders_detal.put("description", e.getName());
                    orders_detal.put("quantity", e.getCantidad());
                    orders_detal.put("unit_price", e.getPrice());
                    jsonArray.put(orders_detal);
                }
            }

            jsonObject.put("detalle_orden", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = this.baseURL + "/client/order/";


        System.out.println(jsonObject.toString());

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int status = response.getInt("status");
                            if (status == 201) {
                                String message = response.getString("message");
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();


                                JSONObject datas = new JSONObject();
                                datas.put("id", response.getJSONObject("data").getInt("order_id"));
                                datas.put("latitude", point_move.latitude);
                                datas.put("longitude", point_move.longitude);
                                Log.d(TAG, "conect " + datas.toString());
                                socket.emit("get orders", datas);

                                DatabaseClient.getInstance(getContext())
                                        .getAppDatabase()
                                        .getCartDao()
                                        .deleteAllCart();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
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
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        Log.d("Voley get", acount.getToken());
                        headers.put("Authorization", "JWT " + acount.getToken());
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

        queue.add(jsonObjectRequest);
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
            json_connect.put("ID_CLIENT", id_user);
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


    }

}
