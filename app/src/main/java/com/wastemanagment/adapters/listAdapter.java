package com.wastemanagment.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wastemanagment.R;
import com.wastemanagment.models.User;

import java.util.List;
import java.util.Random;

public class listAdapter extends RecyclerView.Adapter<listAdapter.MyViewHolder> {
    private List<User> list;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView username;
        Button btn_call;

        public MyViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            btn_call = view.findViewById(R.id.btn_call);

            btn_call.setOnClickListener(this);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View v) {
            User user = list.get(getAdapterPosition());
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + user.getPhone()));
            v.getContext().startActivity(intent);
        }
    }
    public listAdapter(List<User> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = list.get(position);

        holder.username.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}