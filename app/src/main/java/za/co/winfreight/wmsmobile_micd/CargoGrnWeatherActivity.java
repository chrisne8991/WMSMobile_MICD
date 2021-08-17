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

public class CargoGrnWeatherActivity extends AppCompatActivity {
    CheckBox chkCloudy;
    CheckBox chkRain;
    CheckBox chkSnow;
    CheckBox chkSunny;
    CheckBox chkWindy;
    Spinner cbBayLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_weather);

        chkCloudy = findViewById(R.id.cargo_grn_weather_chkCloudy);
        chkRain = findViewById(R.id.cargo_grn_weather_chkRain);
        chkSnow = findViewById(R.id.cargo_grn_weather_chkSnow);
        chkSunny = findViewById(R.id.cargo_grn_weather_chkSunny);
        chkWindy = findViewById(R.id.cargo_grn_weather_chkWindy);
        cbBayLocation = findViewById(R.id.cargo_grn_weather_cbBaylocation);

        query q = new query(getApplicationContext());
        List<String> documentList = q.getBinLocations(((master)getApplicationContext()).getWarehouse());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, documentList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbBayLocation.setAdapter(adapter);
    }

    public void CargoGrnWeather_btnNext_Clicked(View v){
        if (cbBayLocation.getSelectedItem() == null) return;
        if (cbBayLocation.getSelectedItem().toString().equals("")) return;
        String bayLocation = cbBayLocation.getSelectedItem().toString();
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
        if (bayLocation.equals("")){
            Toast.makeText(this, "Please select bay location area", Toast.LENGTH_LONG).show();
            return;
        }


        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        myCargo.setBayLocation(bayLocation);
        myCargo.setWeatherConditions(weatherCondition);
        ((master)getApplicationContext()).set_cargoGoodsReceipt(myCargo);

        query q = new query(getApplicationContext());
        tblErrorMessage errorMessage = q.setGateInDetails(
                myCargo.getUserName(),
                myCargo.getDocumentNr(),
                myCargo.getWarehouse(),
                myCargo.getWeatherConditions(),
                "",
                myCargo.getBayLocation(),
                "",
                myCargo.getGateInType(),
                "0",
                "0",
                "",
                "",
                "");

        if (errorMessage.getErrorCode() != 0) return;

        if (myCargo.getGateInType().equals("SugarBags")){
            Intent intent = new Intent(this, CargoGrnSugarBags1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (myCargo.getGateInType().equals("MineralLooseBulk")){
            Intent intent = new Intent(this, CargoGrnMineralLooseBulk1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (myCargo.getGateInType().equals("MineralBags")){
            Intent intent = new Intent(this, CargoGrnMineralBags1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    public void CargoGrnWeather_btnBack_Clicked(View v){
        query q =  new query(this.getApplicationContext());
        Intent intent = new Intent(this, CargoGRNGateInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods Receipt main menu");
        startActivity(intent);
    }
}
