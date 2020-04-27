package com.cor.frii;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cor.frii.pojo.Brands;
import com.cor.frii.pojo.Categories;
import com.cor.frii.utils.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrandsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrandsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrandsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //--
    private String urlBase = "http://34.71.251.155";

    private BrandsAdapter brandsAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Brands> brandsList;

    public BrandsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrandsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrandsFragment newInstance(String param1, String param2) {
        BrandsFragment fragment = new BrandsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brands, container, false);
        recyclerView = view.findViewById(R.id.BrandsContainer);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        llenarDatos();

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

    // Llenar datos
    private void llenarDatos() {
        brandsList = new ArrayList<>();


        Bundle bundle = this.getArguments();
        String url = "";
        if (bundle != null) {
            url = this.urlBase + "/api/markes/" + bundle.getInt("idCategory");
        } else {
            url = this.urlBase + "/api/markes";
            brandsList.add(new Brands(1, "Gas Normal", "", urlBase + "/media/images/none-img.png"));
            brandsList.add(new Brands(2, "Gas Premium", "", urlBase + "/media/images/none-img.png"));
            brandsList.add(new Brands(3, "Camion", "", urlBase + "/media/images/none-img.png"));
        }

        JSONArray jsonArray = new JSONArray();
        JsonArrayRequest arrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, jsonArray, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                String imagen_url = urlBase + object.getString("image");
                                Brands brands = new Brands(
                                        Integer.parseInt(object.getString("id")),
                                        object.getString("name"),
                                        "",
                                        imagen_url);

                                brandsList.add(brands);
                            }

                            brandsAdapter = new BrandsAdapter(brandsList);
                            recyclerView.setAdapter(brandsAdapter);

                            brandsAdapter.setOnClickListener(new View.OnClickListener() {
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();

                                @Override
                                public void onClick(View v) {
                                    String brandsTitle = brandsList.get(recyclerView.getChildAdapterPosition(v)).getName();
                                    brandsTitle = brandsTitle.toLowerCase();
                                    Toast.makeText(getContext(), brandsTitle, Toast.LENGTH_SHORT).show();
                                    if (brandsTitle.equals("gas normal") || brandsTitle.equals("gas premium")) {
                                        GasFragment gasFragment = new GasFragment();

                                        transaction.replace(R.id.mainContainer, gasFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    } else {
                                        Bundle b = new Bundle();
                                        b.putInt("IdMarke", brandsList.get(recyclerView.getChildAdapterPosition(v)).getId());
                                        ProductsFragment productsFragment = new ProductsFragment();
                                        productsFragment.setArguments(b);
                                        transaction.replace(R.id.mainContainer, productsFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();

                                    }

                                }
                            });
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

}
