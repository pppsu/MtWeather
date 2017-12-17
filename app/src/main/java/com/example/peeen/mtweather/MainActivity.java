package com.example.peeen.mtweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static WeatherClient weatherclient;
    private Toolbar toolbar;
    private ListView cityListView;
    private City currentCity;


    private TextView tempView;
    private ImageView weatherIcon;
    private TextView pressView;
    private TextView humView;
    private TextView windView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tempView = (TextView) findViewById(R.id.temp);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        pressView = (TextView) findViewById(R.id.pressure);
        humView = (TextView) findViewById(R.id.hum);
        windView = (TextView) findViewById(R.id.wind);

        initWeatherClient();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Dialog d = createDialog();
            d.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWeatherClient() {
        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();
        config.ApiKey="2e8b64d4db61c739b90c90fe811eabd9";
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.lang = "en";
        config.maxResult = 5;
        config.numDays = 6;

        try {
            weatherclient = builder.attach(this)
                    .provider(new OpenweathermapProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.client.volley.WeatherClientDefault.class)
                    .config(config)
                    .build();
        }
        catch(Throwable t) {
        }
    }

    private void getWeather() {
        weatherclient.getCurrentCondition(new WeatherRequest(currentCity.getId()),
                new WeatherClient.WeatherEventListener() {
                    @Override
                    public void onWeatherRetrieved(CurrentWeather currentWeather) {

                        toolbar.setSubtitle(currentWeather.weather.currentCondition.getDescr());
                        tempView.setText(String.format("%.0f\u00B0C",currentWeather.weather.temperature.getTemp()));
                        pressView.setText(String.valueOf(currentWeather.weather.currentCondition.getPressure()));
                        windView.setText(String.valueOf(currentWeather.weather.wind.getSpeed()));
                        humView.setText(String.valueOf(currentWeather.weather.currentCondition.getHumidity()));
                        weatherIcon.setImageResource(WeatherIconMapper.getWeatherResource(currentWeather.weather.currentCondition.getIcon(), currentWeather.weather.currentCondition.getWeatherId()));

                        //setToolbarColor(currentWeather.weather.temperature.getTemp());
                    }

                    @Override
                    public void onWeatherError(WeatherLibException e) {

                    }

                    @Override
                    public void onConnectionError(Throwable throwable) {

                    }


                });
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.select_city_dialog, null);
        builder.setView(v);

        EditText et = (EditText) v.findViewById(R.id.ptnEdit);
        cityListView = (ListView) v.findViewById(R.id.cityList);
        cityListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCity = (City) parent.getItemAtPosition(position);

            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 3) {

                    weatherclient.searchCity(s.toString(), new WeatherClient.CityEventListener() {
                        @Override
                        public void onCityListRetrieved(List<City> cities) {
                            CityAdapter ca = new CityAdapter(MainActivity.this, cities);
                            cityListView.setAdapter(ca);

                        }

                        @Override
                        public void onWeatherError(WeatherLibException e) {

                        }

                        @Override
                        public void onConnectionError(Throwable throwable) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                toolbar.setTitle(currentCity.getName() + "," + currentCity.getCountry());
                getWeather();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }


    /*private void setToolbarColor(float temp) {
        int color = -1;

        if (temp < -10)
            color = getResources().getColor(R.color.primary_indigo);
        else if (temp >=-10 && temp <=-5)
            color = getResources().getColor(R.color.primary_blue);
        else if (temp >-5 && temp < 5)
            color = getResources().getColor(R.color.primary_light_blue);
        else if (temp >= 5 && temp < 10)
            color = getResources().getColor(R.color.primary_teal);
        else if (temp >= 10 && temp < 15)
            color = getResources().getColor(R.color.primary_light_green);
        else if (temp >= 15 && temp < 20)
            color = getResources().getColor(R.color.primary_green);
        else if (temp >= 20 && temp < 25)
            color = getResources().getColor(R.color.primary_lime);
        else if (temp >= 25 && temp < 28)
            color = getResources().getColor(R.color.primary_yellow);
        else if (temp >= 28 && temp < 32)
            color = getResources().getColor(R.color.primary_amber);
        else if (temp >= 32 && temp < 35)
            color = getResources().getColor(R.color.primary_orange);
        else if (temp >= 35)
            color = getResources().getColor(R.color.primary_red);

        toolbar.setBackgroundColor(color);

    }*/


    class CityAdapter extends ArrayAdapter<City> {

        private List<City> cityList;
        private Context ctx;

        public CityAdapter(Context ctx, List<City> cityList) {
            super(ctx, R.layout.city_row);
            this.cityList = cityList;
            this.ctx = ctx;
        }

        @Override
        public City getItem(int position) {
            if (cityList != null)
                return cityList.get(position);
            return null;
        }

        @Override
        public int getCount() {
            if (cityList == null)
                return 0;

            return cityList.size();
        }

        @Override
        public long getItemId(int position) {
            if (cityList == null)
                return -1;

            return cityList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.city_row, null, false);
            }

            TextView tv = (TextView) v.findViewById(R.id.descrCity);

            tv.setText(cityList.get(position).getName() + "," + cityList.get(position).getCountry());

            return v;
        }
    }
}
