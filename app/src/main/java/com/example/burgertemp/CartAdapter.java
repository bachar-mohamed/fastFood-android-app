package com.example.burgertemp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private Context context;

    public CartAdapter(Context context){
        this.products=new ArrayList<>();
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
        return  new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.prodName.setText(products.get(position).getName());
        holder.prodPrice.setText("¥"+products.get(position).getPrice());
        holder.prodImg.setImageResource(products.get(position).getImg_url());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView prodName;
        private TextView prodPrice;
        private ImageView prodImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.prodName = itemView.findViewById(R.id.prodName);
            this.prodPrice = itemView.findViewById(R.id.prodPrice);
            this.prodImg=itemView.findViewById(R.id.prodImg);
        }

    }
}