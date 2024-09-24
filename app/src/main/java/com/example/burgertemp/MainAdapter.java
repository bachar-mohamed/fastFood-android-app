package com.example.burgertemp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private Context context;

    public MainAdapter(Context context){
        this.products=new ArrayList<>();
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.productName.setText(products.get(position).getName());
    holder.productIngredients.setText(products.get(position).getIngredients());
    holder.productRating.setText(products.get(position).getRating());
    holder.productPrice.setText("$"+products.get(position).getPrice());
    holder.productImg.setImageResource(products.get(position).getImg_url());

    holder.card.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, ProductActivity.class);
            i.putExtra("name",String.valueOf(products.get( holder.getBindingAdapterPosition()).getName()));
            i.putExtra("calories",String.valueOf(products.get(holder.getBindingAdapterPosition()).getCalories()));
            i.putExtra("time",String.valueOf(products.get(holder.getBindingAdapterPosition()).getDelay()));
            i.putExtra("weight",String.valueOf(products.get(holder.getBindingAdapterPosition()).getWeight()));
            i.putExtra("reviews",String.valueOf(products.get(holder.getBindingAdapterPosition()).getRating()));
            i.putExtra("url",String.valueOf(products.get(holder.getBindingAdapterPosition()).getImg_url()));
            i.putExtra("price",String.valueOf(products.get(holder.getBindingAdapterPosition()).getPrice()));
            i.putExtra("id",String.valueOf(products.get(holder.getBindingAdapterPosition()).getId()));
            i.putExtra("ingredients",String.valueOf(products.get(holder.getBindingAdapterPosition()).getIngredients()));
            context.startActivity(i);
        }
    });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setFilteredList(ArrayList<Product> list){
        products=list;
        notifyDataSetChanged();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView productName;
        private TextView productPrice;
        private TextView productIngredients;
        private TextView productRating;
        private CardView card;
        private ImageView productImg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            productName = itemView.findViewById(R.id.pizzaName);
            productPrice = itemView.findViewById(R.id.pizzaPrice);
            productIngredients=itemView.findViewById(R.id.pizzaIng);
            productRating=itemView.findViewById(R.id.rating);
            productImg=itemView.findViewById(R.id.pizzaImg);
        }
    }
}
