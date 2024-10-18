// ProductAdapter.java
package com.example.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewDescription.setText(product.getDescription());
        holder.textViewPrice.setText(String.format("$%.2f", product.getPrice()));

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imageViewProduct);

        // Set up the "Buy Now" button click listener
        holder.buttonBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open OrderFormActivity
                Intent intent = new Intent(context, OrderFormActivity.class);
                intent.putExtra("product_id", product.getId());
                intent.putExtra("product_name", product.getName());
                intent.putExtra("product_price", product.getPrice());
                intent.putExtra("vendor_id", product.getVendorId());
                context.startActivity(intent);
            }
        });

        // Set up the "Add to Cart" button click listener
        holder.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(product);
            }
        });
    }

    // Method to add the product to the cart
    private void addToCart(Product product) {
        String userId = "670ec41ffc5e3f17ea474301"; // Replace this with the actual user ID
        String url = "https://10.0.2.2:7022/api/Cart/" + userId + "/items";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", product.getId());
            jsonObject.put("productName", product.getName());
            jsonObject.put("quantity", 1); // Add quantity if necessary
            jsonObject.put("price", product.getPrice()); // Add the product price
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("AddToCart", "Request JSON: " + jsonObject.toString()); // Log the JSON

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Error adding to cart", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewName, textViewDescription, textViewPrice;
        Button buttonBuyNow; // Add the Buy Now button

        Button buttonAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonBuyNow = itemView.findViewById(R.id.buttonBuyNow); // Initialize the Buy Now button
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
        }
    }


}