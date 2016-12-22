package com.example.just.businesinfo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.just.businesinfo.R;
import com.example.just.businesinfo.location.*;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class WeatherFragment extends Fragment {

    SwipeRefreshLayout mySwipeRefreshLayout;
    SharedPreferences sharedPreferences;
    Editor edDescription, edTemp, edPressure, edHumidity, edSpeed, edDeg, edIcon;
    final String sDescription = "sDescription";
    final String sTemp = "sTemp";
    final String sPressure = "sPressure";
    final String sHumidity = "sHumidity";
    final String sSpeed = "sSpeed";
    final String sDeg = "sDeg";
    final String sIcon = "sIcon";

    MapView mapView;
    GoogleMap mMap;
    Bitmap bmp = null;
    Bitmap bmpfon = null;
    public static final String LOG_TAG = "WeatherFragment.java";
    GPSTracker gps;
    Button btn1;
    TextView description, temp, pressure, humidity, speed, deg;
    ImageView icon, fon;
    private double latitude;
    private double longitude;
    private String appid = "0fac5609a85a713aab5b80d5ebdfd9fb";
    GetWeather getWeather;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mMap = mapView.getMap();

        btn1 = (Button) view.findViewById(R.id.btn1);

        description = (TextView) view.findViewById(R.id.description);
        temp = (TextView) view.findViewById(R.id.temp);
        pressure = (TextView) view.findViewById(R.id.pressure);
        humidity = (TextView) view.findViewById(R.id.humidity);
        speed = (TextView) view.findViewById(R.id.speed);
        deg = (TextView) view.findViewById(R.id.deg);

        icon = (ImageView) view.findViewById(R.id.icon);
        fon = (ImageView) view.findViewById(R.id.fon);

        description.setText(sharedPreferences.getString(sDescription, ""));
        temp.setText(sharedPreferences.getString(sTemp, ""));
        pressure.setText(sharedPreferences.getString(sPressure, ""));
        humidity.setText(sharedPreferences.getString(sHumidity, ""));
        speed.setText(sharedPreferences.getString(sSpeed, ""));
        deg.setText(sharedPreferences.getString(sDeg, ""));
        icon.setImageBitmap(bmp);

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                gps = new GPSTracker(getActivity());
//
//                if (gps.canGetLocation()) {
//                    latitude = gps.getLatitude();
//                    longitude = gps.getLongitude();
//                    Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                } else {
//                    gps.showSettingsAlert();
//                }
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12);
//                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here"));
//                mMap.animateCamera(cameraUpdate);
//                getWeather = new GetWeather();
//                getWeather.execute();
            }
        });

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        try {
                            gps = new GPSTracker(getActivity());
                            if (gps.canGetLocation()) {
                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                            } else {
                                gps.showSettingsAlert();
                            }
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here"));
                            mMap.animateCamera(cameraUpdate);
                            getWeather = new GetWeather();
                            getWeather.execute();

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        MapsInitializer.initialize(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private class GetWeather extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... arg0) {

            ArrayList<HashMap<String, String>> weatherList = new ArrayList<>();
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&lang=ru&appid=" + appid);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.v(LOG_TAG, "line " + line);
                }
                resultJson = buffer.toString();

                JSONObject jsonObj = new JSONObject(resultJson);

                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                edDescription = sharedPreferences.edit();
                edDescription.putString(sDescription, weather.getString("description"));
                edDescription.commit();

                edIcon = sharedPreferences.edit();
                edIcon.putString(sIcon, weather.getString("icon"));
                edIcon.commit();

                JSONObject main = jsonObj.getJSONObject("main");

                edTemp = sharedPreferences.edit();
                edTemp.putString(sTemp, main.getString("temp"));
                edTemp.commit();

                edPressure = sharedPreferences.edit();
                edPressure.putString(sPressure, main.getString("pressure"));
                edPressure.commit();

                edHumidity = sharedPreferences.edit();
                edHumidity.putString(sHumidity, main.getString("humidity"));
                edHumidity.commit();

                JSONObject wind = jsonObj.getJSONObject("wind");
                edSpeed = sharedPreferences.edit();
                edSpeed.putString(sSpeed, wind.getString("speed"));
                edSpeed.commit();

                edDeg = sharedPreferences.edit();
                edDeg.putString(sDeg, wind.getString("deg"));
                edDeg.commit();

                String urlbmp = "http://www.grodlait.by/images/weather/" + weather.get("icon") + ".png";
                bmpfon = downloadImage(urlbmp);
                String urlstr = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";
                bmp = downloadImage(urlstr);
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject dataJsonObj = null;
            String secondName = "";
            try {
                dataJsonObj = new JSONObject(result);

                JSONObject weather = dataJsonObj.getJSONArray("weather").getJSONObject(0);

                description.setText(sharedPreferences.getString(sDescription, ""));

                JSONObject main = dataJsonObj.getJSONObject("main");
                temp.setText(sharedPreferences.getString(sTemp, ""));
                pressure.setText(sharedPreferences.getString(sPressure, ""));
                humidity.setText(sharedPreferences.getString(sHumidity, ""));

                JSONObject wind = dataJsonObj.getJSONObject("wind");
                speed.setText(sharedPreferences.getString(sSpeed, ""));
                deg.setText(sharedPreferences.getString(sDeg, ""));

                fon.setImageBitmap(bmpfon);
                icon.setImageBitmap(bmp);
                mySwipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}
