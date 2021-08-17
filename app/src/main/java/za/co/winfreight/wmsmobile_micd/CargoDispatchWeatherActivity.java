package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class CargoDispatchWeatherActivity extends AppCompatActivity {
    CheckBox chkCloudy;
    CheckBox chkRain;
    CheckBox chkSnow;
    CheckBox chkSunny;
    CheckBox chkWindy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_dispatch_weather);

        chkCloudy = findViewById(R.id.cargo_dispatch_weather_chkCloudy);
        chkRain = findViewById(R.id.cargo_dispatch_weather_chkRain);
        chkSnow = findViewById(R.id.cargo_dispatch_weather_chkSnow);
        chkSunny = findViewById(R.id.cargo_dispatch_weather_chkSunny);
        chkWindy = findViewById(R.id.cargo_dispatch_weather_chkWindy);

    }

    public void CargoDispatchWeather_btnNext_Clicked(View v){
        String weatherCondition = "";
        int WeatherCount = 0;
        if (chkCloudy.isChecked()) {
            weatherCondition += "Cloudy,";
            WeatherCount++;
        }
        if (chkSunny.isChecked()) {
            weatherCondition += "Sunny,";
            WeatherCount++;
        }
        if (chkSnow.isChecked()) {
            weatherCondition += "Snow,";
            WeatherCount++;
        }
        if (chkRain.isChecked()) {
            weatherCondition += "Rain,";
            WeatherCount++;
        }
        if (chkWindy.isChecked()) {
            weatherCondition += "Windy,";
            WeatherCount++;
        }

        if (weatherCondition.equals("")){
            Toast.makeText(this, "Please provide weather conditions", Toast.LENGTH_LONG).show();
            return;
        }
        if (WeatherCount > 2){
            Toast.makeText(this, "Only two weather conditions allowed", Toast.LENGTH_LONG).show();
            return;
        }

        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        myCargo.setUserName(((master)getApplicationContext()).getUser());
        myCargo.setWeatherConditions(weatherCondition);
        ((master)getApplicationContext()).set_cargoDispatch(myCargo);

        queryDispatch q = new queryDispatch(getApplicationContext());
        tblErrorMessage errorMessage = q.setDispatchDetails(
                myCargo.getUserName(),
                myCargo.getDocumentNr(),
                myCargo.getContainerNr(),
                myCargo.getWeatherConditions(),"","","", "","");

        if (errorMessage.getErrorCode() != 0) return;

        if (myCargo.getGateInType().equals("SugarBags") || myCargo.getGateInType().equals("MineralBags")) {
            Intent intent = new Intent(this, CargoDispatch1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void CargoDispatchWeather_btnBack_Clicked(View v){
        query q =  new query(this.getApplicationContext());
        Intent intent = new Intent(this, CargoDispatchGateInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods Dispatch main menu");
        startActivity(intent);
    }
}
