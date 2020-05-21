package com.cor.frii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cor.frii.persistence.DatabaseClient;
import com.cor.frii.persistence.entity.ECart;
import com.cor.frii.pojo.Product;
import com.cor.frii.utils.LoadImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewHolder> implements View.OnClickListener {


    List<Product> products;
    private Context context;
    private View.OnClickListener listener;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_products, parent, false);
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

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView productTilte, productDescription;
        ImageView productImage;
        Button productButtonAdd;
        EditText productCantidad;

        viewHolder(@NonNull View itemView) {
            super(itemView);
            productTilte = itemView.findViewById(R.id.ProductTitle);
            productImage = itemView.findViewById(R.id.ProductImage);
            productButtonAdd = itemView.findViewById(R.id.ProductButtonAdd);
            productCantidad = itemView.findViewById(R.id.ProductCantidad);
            productDescription = itemView.findViewById(R.id.ProductDescription);

        }

        void bind(final Product product) {
            productCantidad.setText("1");
            productTilte.setText(product.getName());
            productDescription.setText(product.getDescription());

            Picasso.get().load(product.getUrl()).into(productImage);

            productButtonAdd.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    ECart eCart = new ECart();
                    eCart.setName(product.getName());
                    eCart.setPrice(product.getPrice());

                    if (productCantidad.getText().length() > 0) {
                        eCart.setCantidad(Integer.parseInt(productCantidad.getText().toString()));
                        eCart.setTotal(Float.parseFloat(productCantidad.getText().toString()) * product.getPrice());
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
