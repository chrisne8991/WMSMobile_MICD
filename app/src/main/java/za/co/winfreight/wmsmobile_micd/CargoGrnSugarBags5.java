package za.co.winfreight.wmsmobile_micd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ArrowKeyMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class CargoGrnSugarBags5 extends AppCompatActivity {
    DatePickerDialog picker;
    EditText txtDate;
    EditText txtBatchNr;
    TextView lblSlingNr;
    boolean enableKeyBoard;
    Button btnKeyMode;
    int BatchesScanned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableKeyBoard = false;
        setContentView(R.layout.activity_cargo_grn_sugar_bags5);
        txtBatchNr = findViewById(R.id.CargoGrnSugarBags5_txtBatchNr);
        txtDate = findViewById(R.id.CargoGrnSugarBags5_txtDate);
        lblSlingNr = findViewById(R.id.CargoGrnSugarBags5_lblBagNr);

        List<String> values = ((master)getApplicationContext()).get_value2();
        String SlingNr = values.get(1);
        BatchesScanned = Integer.parseInt(values.get(2));

        lblSlingNr.setText(SlingNr);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);

                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(CargoGrnSugarBags5.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    if (monthOfYear + 1 < 10){
                                        if (dayOfMonth < 10 ){
                                            txtDate.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                        }
                                        else{
                                            txtDate.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                        }
                                    }else{
                                        if (dayOfMonth < 10 ){
                                            txtDate.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                        }
                                        else{
                                            txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                        }
                                    }
                                }
                            }, year, month, day);
                    picker.show();

                }
            }
        });


    }

    public void CargoGrnSugarBags5_btnBack_Clicked(View v){
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        String DocumentNr = myCargo.getDocumentNr();
        String UserName = myCargo.getUserName();
        query q = new query(getBaseContext());
        q.RemoveLastGateInSugarBags(DocumentNr, UserName);
        Intent intent = new Intent(this, CargoGrnSugarBags4.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    public void CargoGrnSugarBags5_btnNext_Clicked(View v){
        query q = new query(getApplicationContext());
        if(txtBatchNr.getText() == null) return;
        if(txtDate.getText() == null) return;

        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        String DocumentNr = myCargo.getDocumentNr();
        String BatchNr = txtBatchNr.getText().toString();
        String DateCreated = txtDate.getText().toString();
        tblErrorMessage errorMessage = q.ScanGateInSugarBagsRandomBatch(DocumentNr,BatchNr,DateCreated);
        Toast.makeText(this,errorMessage.getMsg(),Toast.LENGTH_SHORT).show();

        if (errorMessage.getErrorCode() != 0){
            return;
        }
        BatchesScanned++;
        List<String> values = ((master)getApplicationContext()).get_value2();
        values.set(2,String.format("%s",BatchesScanned));

        ((master)getApplicationContext()).set_value2(values);

        Intent intent = new Intent(this, CargoGrnSugarBags6.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnSugarBags5_txtDate_Clicked(View v){
        Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);

        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(CargoGrnSugarBags5.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10){
                            if (dayOfMonth < 10 ){
                                txtDate.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                            }
                            else{
                                txtDate.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }else{
                            if (dayOfMonth < 10 ){
                                txtDate.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                            }
                            else{
                                txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                    }
                }, year, month, day);
        picker.show();
    }
}
