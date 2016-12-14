package com.example.just.businesinfo.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.just.businesinfo.App;
import com.example.just.businesinfo.ImageLoader.IImageLoader;
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

    SharedPreferences sharedPreferences;
    Editor edDescription, edTemp, edPressure, edHumidity, edSpeed, edDeg, edIcon;
    final String sDescription = "sDescription";
    final String sTemp = "sTemp";
    final String sPressure = "sPressure";
    final String sHumidity = "sHumidity";
    final String sSpeed = "sSpeed";
    final String sDeg = "sDeg";
    final String sIcon = "sIcon";

    private ProgressDialog pDialog;
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
    //    private static String url;
    private String appid = "0fac5609a85a713aab5b80d5ebdfd9fb";
    GetWeather getWeather;

    @Override

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);

// Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
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

        // show location button click event
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(getActivity());

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

//                Log.v(LOG_TAG, "latitude " + latitude + " longitude " + longitude);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(53.66935, 23.81313), 15);
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here"));
                mMap.animateCamera(cameraUpdate);

                getWeather = new GetWeather();
                getWeather.execute();
            }
        });

        MapsInitializer.initialize(getActivity());

        // Updates the location and zoom of the MapView

//        Log.v(LOG_TAG, GPSTracker.);

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

//            HttpHandler sh = new HttpHandler();


//            String jsonStr = sh.makeServiceCall(url);

//            if (jsonStr != null) {
            try {

                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&lang=ru&appid=" + appid);
                Log.v(LOG_TAG, "URL " + url);

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

//                char[] chars = null;
//                       chars = line.toCharArray();
//                for (int i = 0; i < line.length(); i++) {
//                    Log.v(LOG_TAG, "line " + chars[i]);
//
//                }

                resultJson = buffer.toString();

                JSONObject jsonObj = new JSONObject(resultJson);

                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                edDescription = sharedPreferences.edit();
                edDescription.putString(sDescription, weather.getString("description"));
                edDescription.commit();

                edIcon = sharedPreferences.edit();
                edIcon.putString(sIcon, weather.getString("icon"));
                edIcon.commit();

//                String description = weather.getString("description");
//                String icon = weather.getString("icon");

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

//                String temp = main.getString("temp");
//                String pressure = main.getString("pressure");
//                String humidity = main.getString("humidity");

                JSONObject wind = jsonObj.getJSONObject("wind");
                edSpeed = sharedPreferences.edit();
                edSpeed.putString(sSpeed, wind.getString("speed"));
                edSpeed.commit();

                edDeg = sharedPreferences.edit();
                edDeg.putString(sDeg, wind.getString("deg"));
                edDeg.commit();


//                String speed = wind.getString("speed");
//                String deg = wind.getString("deg");
//

                Log.v(LOG_TAG, "description " + description);
                Log.v(LOG_TAG, "icon " + icon);
                Log.v(LOG_TAG, "temp " + temp);
                Log.v(LOG_TAG, "pressure " + pressure);
                Log.v(LOG_TAG, "humidity " + humidity);
                Log.v(LOG_TAG, "speed " + speed);
                Log.v(LOG_TAG, "deg " + deg);

                String urlbmp = "http://www.grodlait.by/images/weather/" + weather.get("icon") + ".png";
//                String urlbmp = "http://www.grodlait.by/images/weather/" + "50n" + ".png";
//                if (weather.get("icon").equals("01d")) {
//                    urlbmp = "https://www.dropbox.com/s/8i6hr5w7klgd122/" + weather.get("icon") + ".png?dl=0";
//                } else if (weather.get("icon").equals("02d")) {
//                    urlbmp = "https://www.dropbox.com/s/u1smgn4ji8lo6jw/" + weather.get("icon") + ".png?dl=0";
//                } else if (weather.get("icon").equals("03d")) {
//                    urlbmp = "https://www.dropbox.com/s/d5hfs4afsw7oluy/" + weather.get("icon") + ".png?dl=0";
//                } else if (weather.get("icon").equals("04d")) {
//                    urlbmp = "https://www.dropbox.com/s/v7z4uw4z6xy6g1z/" + weather.get("icon") + ".png?dl=0";
//                } else {
//                    urlbmp = "https://www.dropbox.com/s/r0ttfkmt7fiz62e/50d.png?dl=0";
//                }

                    Log.v(LOG_TAG, "URL " + urlbmp);

                    bmpfon = downloadImage(urlbmp);

                    String urlstr = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";
                    Log.v(LOG_TAG, "URL " + urlstr);
                    bmp = downloadImage(urlstr);


//                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

//                        HashMap<String, String> weathers = new HashMap<>();
//
//                        weathers.put("description", description);
//                        weathers.put("temp", temp);
//                        weathers.put("pressure", pressure);
//                        weathers.put("humidity", humidity);
//                        weathers.put("speed", speed);
//                        weathers.put("deg", deg);
//                        weathers.put("icon", icon);
//
//                        weatherList.add(weathers);
//
//                } catch (final JSONException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getActivity(),
//                                    "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
                }catch(ProtocolException e){
                    e.printStackTrace();
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
//            } else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });
//            }
                return resultJson;

            }

            @Override
            protected void onPostExecute (String result){
                super.onPostExecute(result);

                JSONObject dataJsonObj = null;
                String secondName = "";
                try {
                    dataJsonObj = new JSONObject(result);
                    //JSONArray friends = dataJsonObj.getJSONArray("friends");

                    JSONObject weather = dataJsonObj.getJSONArray("weather").getJSONObject(0);


                    description.setText(sharedPreferences.getString(sDescription, ""));
//                description.setText(weather.getString("description"));
//                    icon.setText(weather.getString("icon"));

                    JSONObject main = dataJsonObj.getJSONObject("main");
                    temp.setText(sharedPreferences.getString(sTemp, ""));
//                temp.setText(main.getString("temp"));
                    pressure.setText(sharedPreferences.getString(sPressure, ""));
//                pressure.setText(main.getString("pressure"));
                    humidity.setText(sharedPreferences.getString(sHumidity, ""));
//                humidity.setText(main.getString("humidity"));

                    JSONObject wind = dataJsonObj.getJSONObject("wind");
                    speed.setText(sharedPreferences.getString(sSpeed, ""));
//                speed.setText(wind.getString("speed"));
                    deg.setText(sharedPreferences.getString(sDeg, ""));
//                deg.setText(wind.getString("deg"));

//                URL url = new URL("http://openweathermap.org/img/w/" + weather.getString("icon") + ".png");
//                Log.v(LOG_TAG, "URL " + url);
//
//                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    fon.setImageBitmap(bmpfon);
                    icon.setImageBitmap(bmp);

//                    description.setText(result.indexOf("description"));
//                    temp.setText(result.indexOf("temp"));
//                    pressure.setText(result.indexOf("pressure"));
//                    humidity.setText(result.indexOf("humidity"));
//                    speed.setText(result.indexOf("speed"));
//                    deg.setText(result.indexOf("deg"));

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

        // Makes HttpURLConnection and returns InputStream
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
