package com.cor.frii;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cor.frii.Login.LoginActivity;
import com.cor.frii.persistence.Session;
import com.cor.frii.pojo.Product;
import com.cor.frii.utils.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class GasCisternaFragment extends Fragment {

    public GasCisternaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gas_cisterna, container, false);

        //Validar informacion del usuario
        Session session = new Session(getContext());
        final int token = session.getToken();
        if (token == 0 || token < 0) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
            System.out.println("LAS CREDENCIALES SON INVALIDAS");
        }
        //--

        recyclerView = view.findViewById(R.id.ProductsContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        products = new ArrayList<>();

        llenarDatos();
        return view;
    }

    private void llenarDatos() {
        String url = urlBase + "/api/product/gas/gas-cisterna";

        JSONObject jsonArray = new JSONObject();
        JsonObjectRequest arrayRequest =
                new JsonObjectRequest(Request.Method.GET, url, jsonArray, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                String imagen_url = urlBase + object.getString("image");
                                Product product = new Product(
                                        object.getInt("id"),
                                        object.getString("description"),
                                        "Precio UU: S/." +
                                                object.getString("unit_price"),
                                        Float.parseFloat(object.getString("unit_price")),
                                        object.getInt("measurement"),
                                        1,
                                        imagen_url,
                                        "",
                                        object.getJSONObject("marke_id").getString("name")
                                );

                                products.add(product);
                            }

                            productAdapter = new ProductAdapter(products);
                            recyclerView.setAdapter(productAdapter);
                        } catch (Exception e) {
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
                                JSONObject obj = new JSONObject(res);
                                Log.d("Voley post", obj.toString());
                                String msj = obj.getString("message");
                                Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();

                            } catch (UnsupportedEncodingException | JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(arrayRequest);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private OnFragmentInteractionListener mListener;

    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    ArrayList<Product> products;
    //--
    String urlBase = "http://34.71.251.155";
}
