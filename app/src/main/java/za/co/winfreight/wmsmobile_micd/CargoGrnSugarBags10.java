package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CargoGrnSugarBags10 extends AppCompatActivity {
    CheckBox chkCloudy;
    CheckBox chkRain;
    CheckBox chkSnow;
    CheckBox chkSunny;
    CheckBox chkWindy;
    TextView lblComments;
    TextView lblSlingNumber;
    TextView lblWeight;
    EditText txtCount;
    EditText txtWeight;
    EditText txtComments;
    Boolean isDamaged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_sugar_bags10);

        chkCloudy = findViewById(R.id.CargoGrnSugarBags10_chkCloudy);
        chkRain = findViewById(R.id.CargoGrnSugarBags10_chkRain);
        chkSnow = findViewById(R.id.CargoGrnSugarBags10_chkSnow);
        chkSunny = findViewById(R.id.CargoGrnSugarBags10_chkSunny);
        chkWindy = findViewById(R.id.CargoGrnSugarBags10_chkWindy);

        lblComments = findViewById(R.id.CargoGrnSugarBags10_lblComments);
        lblSlingNumber = findViewById(R.id.CargoGrnSugarBags10_lblSlingNumber);
        lblWeight = findViewById(R.id.CargoGrnSugarBags10_lblWeight);
        txtCount = findViewById(R.id.CargoGrnSugarBags10_txtCount);
        txtWeight = findViewById(R.id.CargoGrnSugarBags10_txtWeight);
        txtComments = findViewById(R.id.CargoGrnSugarBags10_txtComments);
        
        isDamaged = false;
        lblComments.setVisibility(View.INVISIBLE);
        lblSlingNumber.setVisibility(View.INVISIBLE);
        lblWeight.setVisibility(View.INVISIBLE);
        txtCount.setVisibility(View.INVISIBLE);
        txtWeight.setVisibility(View.INVISIBLE);
        txtComments.setVisibility(View.INVISIBLE);

        CargoGrnClass mycargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        if (!mycargo.getGateInType().equals("SugarBags")){
            lblSlingNumber.setText(R.string.bag_nr);
        }
    }

    public void CargoGrnSugarBags10_btnNext_Clicked(View v){
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
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        myCargo.setEndWeatherConditions(weatherCondition);

        if (isDamaged){
            if (txtCount.getText() == null)
            {
                Toast.makeText(this,"Supply damaged sling number",Toast.LENGTH_SHORT).show();
                return;
            }
            try{
                int mySlingNr = Integer.parseInt(txtWeight.getText().toString());
                if (mySlingNr == 0.00){
                    Toast.makeText(this,"Supply damaged sling number",Toast.LENGTH_SHORT).show();
                    return;
                }
            }catch(Exception ex)
            {
                Toast.makeText(this,"Supply damaged weight",Toast.LENGTH_SHORT).show();
                return;
            }

            if (txtWeight.getText() == null){
                Toast.makeText(this,"Supply damaged weight",Toast.LENGTH_SHORT).show();
                return;
            }
            try{
                double myWeight = Double.parseDouble(txtWeight.getText().toString());
                if (myWeight == 0.00){
                    Toast.makeText(this,"Supply damaged weight",Toast.LENGTH_SHORT).show();
                    return;
                }
            }catch(Exception ex)
            {
                Toast.makeText(this,"Supply damaged weight",Toast.LENGTH_SHORT).show();
                return;
            }
            myCargo.setOperationalDamage(true);
            myCargo.setOperationalDamagedSlingNr(txtCount.getText().toString());
            myCargo.setOperationalDamagedQty(txtWeight.getText().toString());
            myCargo.setOperationalDamagedComments(txtComments.getText().toString());
        }

        ((master)getApplicationContext()).set_cargoGoodsReceipt(myCargo);

        query q = new query(getApplicationContext());
        String isTarpDamagedString = "0";
        String isOperationalDamageString = "0";
        if (myCargo.isOperationalDamage()) isOperationalDamageString = "1";
        if (myCargo.getIsTarpDamaged()) isTarpDamagedString = "1";
        tblErrorMessage errorMessage = q.setGateInDetails(
                myCargo.getUserName(),
                myCargo.getDocumentNr(),
                myCargo.getWarehouse(),
                myCargo.getWeatherConditions(),
                myCargo.getEndWeatherConditions(),
                myCargo.getBayLocation(),
                String.format("%s",myCargo.getMissingBagCount()),
                myCargo.getGateInType(),
                isTarpDamagedString,
                isOperationalDamageString,
                myCargo.getOperationalDamagedSlingNr(),
                myCargo.getOperationalDamagedQty(),
                myCargo.getOperationalDamagedComments());

        Intent intent = new Intent(this, CargoGrnSugarBags11.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnSugarBags10_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnSugarBags9.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.CargoGrnSugarBags10_rbYes:{
                if (checked){
                    isDamaged=true;
                    lblComments.setVisibility(View.VISIBLE);
                    lblSlingNumber.setVisibility(View.VISIBLE);
                    lblWeight.setVisibility(View.VISIBLE);
                    txtCount.setVisibility(View.VISIBLE);
                    txtWeight.setVisibility(View.VISIBLE);
                    txtComments.setVisibility(View.VISIBLE);
                }
                break;

            }
            case R.id.CargoGrnSugarBags10_rbNo:{
                if (checked) {
                    isDamaged = false;
                    lblComments.setVisibility(View.INVISIBLE);
                    lblSlingNumber.setVisibility(View.INVISIBLE);
                    lblWeight.setVisibility(View.INVISIBLE);
                    txtCount.setVisibility(View.INVISIBLE);
                    txtWeight.setVisibility(View.INVISIBLE);
                    txtComments.setVisibility(View.INVISIBLE);
                }
                break;
            }
        }
    }
}
