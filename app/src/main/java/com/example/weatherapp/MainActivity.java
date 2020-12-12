package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    TextView resultTv;

    public class Downloadtask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = urlConnection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(stream);
                int data = stream.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = streamReader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                String weatherInfo = json.getString("weather");
                Log.i("weather", weatherInfo);
                JSONArray jsonArray = new JSONArray(weatherInfo);
                String message = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String desc = jsonPart.getString("description");
                    if (!main.equals("") && !desc.equals("")) {
                        message += main + ": " + desc;
                    }
                }
                if (!message.equals("")) {
                    resultTv.setText(message);
                }else {
                    Toast.makeText(getApplicationContext(), "Error occurred",Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        button = findViewById(R.id.button);
        resultTv = findViewById(R.id.resultTv);


    }

    public void getWeather(View view) {
        Downloadtask task = new Downloadtask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=4b74f67cf0b07d01b38fa272f953b290");
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }
}