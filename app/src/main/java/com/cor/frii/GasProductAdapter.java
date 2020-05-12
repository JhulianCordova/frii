package com.cor.frii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cor.frii.persistence.DatabaseClient;
import com.cor.frii.persistence.entity.ECart;
import com.cor.frii.pojo.Product;
import com.cor.frii.utils.LoadImage;

import java.util.List;

public class GasProductAdapter extends RecyclerView.Adapter<GasProductAdapter.viewHolder> implements View.OnClickListener {


    List<Product> products;
    private Context context;

    public GasProductAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gas_product, parent, false);
        view.setOnClickListener(this);
        context = parent.getContext();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView gasProductTitle;
        ImageView gasProductImage;
        EditText productGasCantidad;
        Button productGasAddCart;
        RadioGroup radioGroup;
        RadioButton peso;

        public viewHolder(@NonNull final View itemView) {
            super(itemView);
            gasProductTitle = itemView.findViewById(R.id.ProductGasTitle);
            gasProductImage = itemView.findViewById(R.id.ProductGasImage);
            productGasCantidad = itemView.findViewById(R.id.ProductGasCantidad);
            productGasAddCart = itemView.findViewById(R.id.ProductGasAddCart);
            radioGroup = itemView.findViewById(R.id.radioGroup);


            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.gas5kl:
                            peso = itemView.findViewById(R.id.gas5kl);
                            break;
                        case R.id.gas10kl:
                            peso = itemView.findViewById(R.id.gas10kl);
                            break;
                        case R.id.gas15kl:
                            peso = itemView.findViewById(R.id.gas15kl);
                            break;

                        case R.id.gas45kl:
                            peso = itemView.findViewById(R.id.gas45kl);
                            break;
                    }
                }
            });
        }

        void bind(final Product product) {

            productGasCantidad.setText("1");

            gasProductTitle.setText(product.getName());
            new LoadImage(gasProductImage).execute(product.getUrl());


            productGasAddCart.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    String pesoText = peso.getText().toString();

                    ECart eCart = new ECart();
                    eCart.setName(product.getName() + " " + pesoText + " KG");
                    eCart.setPrice(product.getPrice());

                    if (productGasCantidad.getText().length() > 0) {
                        eCart.setCantidad(Integer.parseInt(productGasCantidad.getText().toString()));
                        eCart.setTotal(Float.parseFloat(productGasCantidad.getText().toString()) * product.getPrice());
                        DatabaseClient.getInstance(context)
                                .getAppDatabase()
                                .getCartDao()
                                .addCart(eCart);
                        Toast.makeText(context, "Agregado al Carrito", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Ingrese una cantidad mayor a 0", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
