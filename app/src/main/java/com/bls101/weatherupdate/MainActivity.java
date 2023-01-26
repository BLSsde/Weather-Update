package com.bls101.weatherupdate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ArrayList<ModelClass_RecyclerView> arr_hourlyData = new ArrayList();
    ArrayList<ModelClass_RecyclerView> arr_next10DaysData = new ArrayList();
    Toolbar toolbar;
    CardView search_field;
    ImageView search_img;
    TextView tv_Wind;
    TextView tv_Precip;
    TextView tv_Pressure;
    EditText etCity;
    EditText etCountry;
    TextView showCityName;
    private final String AppId = "3eece71f10ee705816163a536b826f5b";
    private final String API_KEY = "3eece71f10ee705816163a536b826f5b";
    private final String New_URL_my = "https://api.openweathermap.org/data/2.5/forecast";
    DecimalFormat df = new DecimalFormat("#.##");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    boolean isCross = false;

    @SuppressLint({"MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.toolbar = (Toolbar)this.findViewById(R.id.toolbarMy);
        this.search_field = (CardView)this.findViewById(R.id.search_field);
        this.search_img = (ImageView)this.findViewById(R.id.iv_search_IMG);
        this.tv_Precip = (TextView)this.findViewById(R.id.tv_Precip);
        this.tv_Wind = (TextView)this.findViewById(R.id.tv_wind);
        this.tv_Pressure = (TextView)this.findViewById(R.id.tv_Pressure);
        this.showCityName = (TextView)this.findViewById(R.id.tv_showCityName);
        this.setSupportActionBar(this.toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            this.getSupportActionBar().setTitle("Weather Update");
        }

        this.toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
        this.etCity = (EditText)this.findViewById(R.id.ed_get_city_name);
        final RecyclerView recyclerView = (RecyclerView)this.findViewById(R.id.recyclerView_hourly);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        final RecyclerView recyclerView10Days = (RecyclerView)this.findViewById(R.id.recyclerView_10Days);
        recyclerView10Days.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.etCity.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    MainActivity.this.arr_hourlyData.clear();
                    MainActivity.this.arr_next10DaysData.clear();
                    MainActivity.this.hourlyWeatherDataForecast();
                    MainActivity.this.getWeatherForNext10Days();
                    HourlyDataAdapter adapter = new HourlyDataAdapter(MainActivity.this, MainActivity.this.arr_hourlyData);
                    recyclerView.setAdapter(adapter);
                    HourlyDataAdapter adapter2 = new HourlyDataAdapter(MainActivity.this, MainActivity.this.arr_next10DaysData);
                    recyclerView10Days.setAdapter(adapter2);
                    return true;
                } else {
                    return false;
                }
            }
        });
        HourlyDataAdapter adapter = new HourlyDataAdapter(this, this.arr_hourlyData);
        recyclerView.setAdapter(adapter);
        HourlyDataAdapter adapter2 = new HourlyDataAdapter(this, this.arr_next10DaysData);
        recyclerView10Days.setAdapter(adapter2);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        (new MenuInflater(this)).inflate(R.menu.toolbar_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.opt_setting) {
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void hourlyWeatherDataForecast() {
        String city = this.etCity.getText().toString().trim();
        String endPointURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + "3eece71f10ee705816163a536b826f5b";
        JsonObjectRequest request = new JsonObjectRequest(0, endPointURL, (JSONObject)null, new Listener<JSONObject>() {
            @SuppressLint({"SetTextI18n"})
            public void onResponse(JSONObject response) {
                try {
                    JSONObject city = response.getJSONObject("city");
                    String name = city.getString("name");
                    String country = city.getString("country");
                    MainActivity.this.showCityName.setText(name + "(" + country + ")");
                    JSONArray list = response.getJSONArray("list");
                    boolean flag = false;

                    for(int i = 0; i < list.length(); ++i) {
                        JSONObject forecast = list.getJSONObject(i);
                        String date = forecast.getString("dt_txt");
                        JSONObject main = forecast.getJSONObject("main");
                        JSONObject wind = forecast.getJSONObject("wind");
                        if (date.endsWith("00:00:00")) {
                            break;
                        }

                        double temp = main.getDouble("temp");
                        double humidity = main.getDouble("humidity");
                        JSONArray weather = forecast.getJSONArray("weather");
                        String description = weather.getJSONObject(0).getString("description");
                        String[] dateTime = date.split(" ");
                        String dateString = dateTime[0];
                        String timeString = dateTime[1];
                        MainActivity.this.arr_hourlyData.add(new ModelClass_RecyclerView(timeString, "" + temp, 2131165399));
                    }
                } catch (JSONException var21) {
                    var21.printStackTrace();
                }

            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void getWeatherForNext10Days() {
        String city = this.etCity.getText().toString().trim();
        String tempUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + "3eece71f10ee705816163a536b826f5b";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(0, tempUrl, (JSONObject)null, new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");

                    for(int i = 0; i < list.length(); ++i) {
                        JSONObject forecast = list.getJSONObject(i);
                        String date = forecast.getString("dt_txt");
                        if (!date.startsWith(MainActivity.this.getCurrentDate())) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(MainActivity.this.dateFormat.parse(date));
                            long time = System.currentTimeMillis();
                            calendar.setTimeInMillis(time);
                            int currentDay = calendar.get(6);
                            if (calendar.get(6) > currentDay + 16) {
                                break;
                            }
                        }

                        JSONObject main = forecast.getJSONObject("main");
                        double temp = main.getDouble("temp");
                        double humidity = main.getDouble("humidity");
                        JSONArray weather = forecast.getJSONArray("weather");
                        String description = weather.getJSONObject(0).getString("description");
                        System.out.println("Date: " + date);
                        System.out.println("Temperature: " + temp + "°C");
                        System.out.println("Humidity: " + humidity + "%");
                        System.out.println("Description: " + description);
                        System.out.println();
                        String[] dateTime = date.split(" ");
                        String dateString = dateTime[0];
                        String timeString = dateTime[1];
                        MainActivity.this.arr_next10DaysData.add(new ModelClass_RecyclerView(dateString, "" + temp, 2131165399));
                    }
                } catch (JSONException var16) {
                    var16.printStackTrace();
                } catch (ParseException var17) {
                    var17.printStackTrace();
                }

            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);
    }

    private String getCurrentDate() {
        long time = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public void searchIconChange(View view) {
        if (this.isCross) {
            this.search_field.setVisibility(View.GONE);
            this.search_img.setImageResource(R.drawable.search_icon);
            this.isCross = false;
        } else {
            this.search_field.setVisibility(View.VISIBLE);
            this.search_img.setImageResource(R.drawable.cross_icon_img);
            this.isCross = true;
        }

    }
}
