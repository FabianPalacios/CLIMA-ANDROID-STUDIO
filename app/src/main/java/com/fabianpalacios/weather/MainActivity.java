package com.fabianpalacios.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fabianpalacios.weather.services.model.Root;
import com.fabianpalacios.weather.services.model.WeatherService;

public class MainActivity extends AppCompatActivity {

    private EditText txtCountryISOCode = null;
    private EditText txtCityName = null;

    private TextView lblCurrent = null;
    private TextView lblMin = null;
    private TextView lblMax = null;

    private WeatherService service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        service = new WeatherService("a9cf0f7a3cc84a884d84d4df48f057c2");

    }


    public  void initViews(){
        txtCountryISOCode = findViewById(R.id.txtCountryISOCode);
        txtCityName = findViewById(R.id.txtCityName);

        lblCurrent = findViewById(R.id.lblCurrent);
        lblMin = findViewById(R.id.lblMin);
        lblMax = findViewById(R.id.lblMax);
    }

    public void btnGetInfoOnClick(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        StringBuilder text = new StringBuilder();

        if(txtCountryISOCode.getText().toString().isEmpty() || txtCityName.getText().toString().isEmpty()){
            text.append(getString(R.string.Fields_cannot_be_empty));
            alert.setMessage(text);
            alert.setPositiveButton("close",null);

            alert.show();
        }else{
            getWeatherInfo(txtCityName.getText().toString(),txtCountryISOCode.getText().toString());
        }

    }

    public void getWeatherInfo(String cityName, String countryISOCode){
        service.requestWeatherData(cityName, countryISOCode,(isNetworkError,statusCode, root) -> {
            if(!isNetworkError){
                if(statusCode == 200){
                    showWeatherInfo(root);
                }else{
                    Log.d("Weather", "Service error");
                }
            }else{
                Log.d("Weather", "Network error");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public  void showWeatherInfo(Root root) {
        String temp =  String.valueOf(root.getMain().getTemp());
        String tempMin =  String.valueOf(root.getMain().getTempMin());
        String tempMax =  String.valueOf(root.getMain().getTempMax());

        lblCurrent.setText(getString(R.string.current)+" "+temp);
        lblMin.setText(getString(R.string.minimum)+" "+tempMin);
        lblMax.setText(getString(R.string.maximum)+" "+tempMax);
    }
}