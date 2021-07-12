package com.danroy.haliyaanger.classes;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.danroy.haliyaanger.MainActivity;
import com.danroy.haliyaanger.R;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CitiesRecylerViewAdapter extends RecyclerView.Adapter<CitiesRecylerViewAdapter.MyViewHolder> {
    public static Context context;
    public ArrayList<Country> countries;

    public static Context getContext() {
        return context;
    }

    public CitiesRecylerViewAdapter(Context context, ArrayList<Country> countries){
        CitiesRecylerViewAdapter.context =context;
        this.countries =countries;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.city_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cityName.setText(countries.get(position).getCityName());
        holder.countryName.setText(countries.get(position).getCountryName());
        Picasso.get().load("https://flagcdn.com/w40/"+countries.get(position).getCountryName().toLowerCase()+".png").into(holder.countryImage);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CitiesRecylerViewAdapter.getContext(), MainActivity.class);
                intent.putExtra("lat",countries.get(position).getLatitude());
                intent.putExtra("lng",countries.get(position).getLongitude());
                intent.putExtra("cityName",countries.get(position).getCityName());
                ((Activity)context).setResult(Activity.RESULT_OK,intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cityName,countryName;
        public ImageView countryImage;
        public ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cityName = itemView.findViewById(R.id.cityActivityTV);
            this.countryName = itemView.findViewById(R.id.countryCityActivityTV);
            this.constraintLayout = itemView.findViewById(R.id.citiesCL);
            this.countryImage = itemView.findViewById(R.id.countryCityActivityIV);
        }
    }
}
