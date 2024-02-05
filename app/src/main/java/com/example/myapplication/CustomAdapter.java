package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList cust_id,cust_name,cust_Email,cust_pwd,cust_mobile,cust_address,cust_city,cust_state,cust_country,cust_pincode;

    CustomAdapter(Context context, ArrayList cust_id, ArrayList cust_name, ArrayList cust_Email, ArrayList cust_pwd, ArrayList cust_mobile, ArrayList cust_address, ArrayList cust_city,  ArrayList cust_state, ArrayList cust_country, ArrayList cust_pincode){

        this.context=context;
        this.cust_id=cust_id;
        this.cust_name=cust_name;
        this.cust_Email=cust_Email;
        this.cust_pwd=cust_pwd;
        this.cust_mobile= cust_mobile;
        this.cust_address= cust_address;
        this.cust_city= cust_city;
        this.cust_state= cust_state;
        this.cust_country= cust_country;
        this.cust_pincode= cust_pincode;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.cust_id_txt.setText(String.valueOf(cust_id.get(position)));
        holder.cust_name_txt.setText(String.valueOf(cust_name.get(position)));
        holder.cust_email_txt.setText(String.valueOf(cust_Email.get(position)));
        holder.cust_pwd_txt.setText(String.valueOf(cust_pwd.get(position)));
        holder.cust_mobile_txt.setText(String.valueOf(cust_mobile.get(position)));
        holder.cust_street_txt.setText(String.valueOf(cust_address.get(position)));
        holder.cust_city_txt.setText(String.valueOf(cust_city.get(position)));
        holder.cust_state_txt.setText(String.valueOf(cust_state.get(position)));
        holder.cust_country_txt.setText(String.valueOf(cust_country.get(position)));
        holder.cust_pincode_txt.setText(String.valueOf(cust_pincode.get(position)));




    }

    @Override
    public int getItemCount() {

        return cust_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cust_id_txt,cust_name_txt,cust_email_txt,cust_pwd_txt,cust_mobile_txt,cust_street_txt,cust_city_txt,cust_state_txt,cust_country_txt,cust_pincode_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cust_id_txt=itemView.findViewById(R.id.cust_id_txt);
            cust_name_txt=itemView.findViewById(R.id.cust_name_txt);
            cust_email_txt=itemView.findViewById(R.id.cust_email_txt);
            cust_pwd_txt=itemView.findViewById(R.id.cust_pwd_txt);
            cust_mobile_txt=itemView.findViewById(R.id.cust_mobile_txt);
            cust_street_txt=itemView.findViewById(R.id.cust_street_txt);
            cust_city_txt=itemView.findViewById(R.id.cust_city_txt);
            cust_state_txt=itemView.findViewById(R.id.cust_state_txt);
            cust_country_txt=itemView.findViewById(R.id.cust_country_txt);
            cust_pincode_txt=itemView.findViewById(R.id.cust_pincode_txt);

        }
    }
}