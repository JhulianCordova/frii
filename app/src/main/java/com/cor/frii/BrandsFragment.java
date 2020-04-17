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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cor.frii.pojo.Brands;
import com.cor.frii.pojo.Categories;

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


    private BrandsAdapter brandsAdapter;
    private RecyclerView recyclerView;
    ArrayList<Brands> brands;

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
        View view=inflater.inflate(R.layout.fragment_brands, container, false);
        recyclerView=view.findViewById(R.id.BrandsContainer);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        brands=new ArrayList<>();
        brands.add(new Brands(1,"Gas Normal","",""));
        brands.add(new Brands(2,"Gas Premium","",""));
        brands.add(new Brands(3,"Cerveza","",""));
        brands.add(new Brands(4,"Agua","",""));
        brands.add(new Brands(5,"Camion","",""));

        brandsAdapter=new BrandsAdapter(brands);
        recyclerView.setAdapter(brandsAdapter);

        brandsAdapter.setOnClickListener(new View.OnClickListener(){

            FragmentManager manager=getActivity().getSupportFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            @Override
            public void onClick(View v) {
                String brandsTitle=brands.get(recyclerView.getChildAdapterPosition(v)).getName();
                brandsTitle=brandsTitle.toLowerCase();
                Toast.makeText(getContext(),brandsTitle,Toast.LENGTH_SHORT).show();
                if (brandsTitle.equals("gas normal") || brandsTitle.equals("gas premium")){
                    GasFragment gasFragment=new GasFragment();

                    transaction.replace(R.id.mainContainer,gasFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    ProductsFragment productsFragment=new ProductsFragment();
                    transaction.replace(R.id.mainContainer,productsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
