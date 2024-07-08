package com.temmahadi.ecoroot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private List<UserData> dataList;

    public MyAdapter(Context context, List<UserData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    private void calculateTotalConsumed() {
        float totalConsumed = 0;
        for (UserData data : dataList) {
            totalConsumed += data.getWeight();
        }
        Log.d("MyAdapter", "Total consumed weight: " + totalConsumed);
        // Save total consumed weight to SharedPreferences
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("totalConsumed", totalConsumed);
        editor.apply();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImg()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getTitle());
        holder.recWeight.setText(String.valueOf(dataList.get(position).getWeight()));
        setAnim(holder.itemView);
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPage.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImg());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDesc());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("Weight", String.valueOf(dataList.get(holder.getAdapterPosition()).getWeight()));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView recImage;
        TextView recTitle, recWeight;
        CardView recCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recCard = itemView.findViewById(R.id.recCard);
            recImage = itemView.findViewById(R.id.recImage);
            recWeight = itemView.findViewById(R.id.recWeight);
            recTitle = itemView.findViewById(R.id.recTitle);
        }
    }
    public void searchDataList(ArrayList<UserData> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
    public void setDataList(List<UserData> dataList) {
        this.dataList = dataList;
        calculateTotalConsumed(); // Recalculate total consumed when dataList is updated
        notifyDataSetChanged(); // Notify adapter of data changes
    }
    private void setAnim(@NonNull View view) {
        AnimationSet animationSet = new AnimationSet(true);
        Animation slideIn = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        slideIn.setDuration(750); // Adjust duration as needed
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(750); // Adjust duration as needed
        animationSet.addAnimation(slideIn);
        animationSet.addAnimation(fadeIn);
        view.startAnimation(animationSet);
    }
}
