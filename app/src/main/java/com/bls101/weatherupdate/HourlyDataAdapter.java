

package com.bls101.weatherupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;

public class HourlyDataAdapter extends Adapter<HourlyDataAdapter.DataViewHolder> {
    ArrayList<ModelClass_RecyclerView> arr_hourlyData;
    Context context;

    HourlyDataAdapter(Context context, ArrayList<ModelClass_RecyclerView> arr_hourlyData) {
        this.arr_hourlyData = arr_hourlyData;
        this.context = context;
    }

    @NonNull
    public HourlyDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.hourly_template, parent, false);
        return new HourlyDataAdapter.DataViewHolder(view);
    }

    public void onBindViewHolder(@NonNull HourlyDataAdapter.DataViewHolder holder, int position) {
        holder.iv_weatherHourlyImageIcon.setImageResource(((ModelClass_RecyclerView)this.arr_hourlyData.get(position)).img);
        holder.tv_time.setText(((ModelClass_RecyclerView)this.arr_hourlyData.get(position)).time);
        holder.tv_temperature.setText(((ModelClass_RecyclerView)this.arr_hourlyData.get(position)).temperature);
    }

    public int getItemCount() {
        return this.arr_hourlyData.size();
    }

    public class DataViewHolder extends ViewHolder {
        TextView tv_time;
        TextView tv_temperature;
        ImageView iv_weatherHourlyImageIcon;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_time = (TextView)itemView.findViewById(R.id.tv_time);
            this.tv_temperature = (TextView)itemView.findViewById(R.id.tv_avg_temperature);
            this.iv_weatherHourlyImageIcon = (ImageView)itemView.findViewById(R.id.iv_hour_weather_icon);
        }
    }
}
