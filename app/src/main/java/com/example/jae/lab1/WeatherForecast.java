package com.example.jae.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";

    public ProgressBar progressBar;
    public TextView curTemp, minView, maxView;
    public ImageView weatherView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        curTemp = (TextView) findViewById(R.id.current_temp);
        minView = (TextView) findViewById(R.id.min_temp);
        maxView = (TextView) findViewById(R.id.max_temp);

        weatherView = (ImageView) findViewById(R.id.WeatherView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        new ForecastQuery().execute();

    }

    public class ForecastQuery extends AsyncTask<String, Integer, String>{

        public String min;
        public String max;
        public String current;
        public Bitmap currentWeather;
        public String iconName;
        public int state;

        public boolean fileExistence(String fName){
            File file = getBaseContext().getFileStreamPath(fName);
            return file.exists();
        }

        protected String doInBackground(String ... args){

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), null);

                int type = XmlPullParser.START_DOCUMENT;

                while(type != XmlPullParser.END_DOCUMENT) {

                    switch (type){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equals("temperature")) {
                            current = parser.getAttributeValue(null, "value");
                            publishProgress(25);
                            min = parser.getAttributeValue(null, "min");
                            publishProgress(50);
                            max = parser.getAttributeValue(null, "max");
                            publishProgress(75);

                            Log.i(ACTIVITY_NAME, "searching for: " + max + min);
                        }

                        if (name.equals("weather")){
                            iconName = parser.getAttributeValue(null, "icon");

                            URL iconURL = new URL("http://openweathermap.org/img/w/"
                                + iconName + ".png");

                            String fileName =  iconName + ".png";

                            if(!fileExistence(fileName)) {
                                HttpURLConnection connection = (HttpURLConnection) iconURL.openConnection();
                                connection.connect();
                                currentWeather = BitmapFactory.decodeStream(connection.getInputStream());

                                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                currentWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                publishProgress(100);
                                Log.i(ACTIVITY_NAME, "Downloading: " + fileName);
                                connection.disconnect();
                            }else{
//                                FileInputStream fis = null;
//                                try {    fis = new FileInputStream(fileName);   }
//                                catch (FileNotFoundException e) {    e.printStackTrace();  }
//                                currentWeather = BitmapFactory.decodeStream(fis);
//                                Log.i(ACTIVITY_NAME, "searching for: " + fileName);
//                                publishProgress(100);

                                Log.i(ACTIVITY_NAME, "searching for: " + fileName); //TODO fix
                                File file = getBaseContext().getFileStreamPath(fileName);
                                FileInputStream fis = new FileInputStream(file);

                                currentWeather = BitmapFactory.decodeStream(fis);

                            }

                        }

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
                    type = parser.next(); //advances to next xml event
                }

            }catch (Exception e){

            }

            return null;
        }

        public void onProgressUpdate(Integer ... value)
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress( value[0] );
        }

        public void onPostExecute(String s){
            curTemp.setText("Current Temp: " + current + "C");
            minView.setText("Minimum Temp: " + min + "C");
            maxView.setText("Maximum Temp: " + max + "C");
            weatherView.setImageBitmap(currentWeather);

            progressBar.setVisibility(View.INVISIBLE);
        }


    }

}
