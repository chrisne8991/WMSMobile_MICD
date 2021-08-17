package za.co.winfreight.wmsmobile_micd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

public class CargoGrnSugarBags4 extends AppCompatActivity {

    EditText txtCount;
    EditText txtWeight;
    TextView txtTotalCount;
    TextView txtTotalWeight;
    Spinner cbSlingNr;
    int BagCount;
    int BagJump; //the mod 3 value of the total amount of bags so we know when to go to batch count
    int BagLimit;
    int BatchesScanned;
    Boolean cbDocumentClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_sugar_bags4);

        BagLimit=0;

        txtCount = findViewById(R.id.CargoGrnSugarBags4_txtCount);
        txtWeight = findViewById(R.id.CargoGrnSugarBags4_txtWeight);
        cbSlingNr = findViewById(R.id.CargoGrnSugarBags4_cbSlingNr);
        txtTotalCount = findViewById(R.id.CargoGrnSugarBags4_txtTotalCount);
        txtTotalWeight = findViewById(R.id.CargoGrnSugarBags4_txtTotalWeight);

        BagLimit = Integer.parseInt(((master)getApplicationContext()).get_value());
        List<String> values = ((master)getApplicationContext()).get_value2();
        query q = new query(getBaseContext());

        if (values.size() == 0)
        {
            BagCount = 1;
            BatchesScanned = 0;

            int randomValue1 = 0;
            int randomValue2 = 0;
            int randomValue3 = 0;

            Random rand = new Random();
            randomValue1 = rand.nextInt(BagLimit) + 1;

            randomValue2 = rand.nextInt(BagLimit) + 1;
            if (BagLimit <= 3) {
                randomValue1 = 1;
                randomValue2 = 2;
                randomValue3 = 3;
            }
            else {
                while (randomValue1 == randomValue2){
                    randomValue2 = rand.nextInt(BagLimit) +1;
                }
                randomValue3 = rand.nextInt(BagLimit) + 1;
                while (randomValue3 == randomValue2 || randomValue3 == randomValue1 ){
                    randomValue3 = rand.nextInt(BagLimit) +1;
                }
            }
            List<Integer> iList = new ArrayList<>();
            iList.add(randomValue1);
            iList.add(randomValue2);
            iList.add(randomValue3);

            Collections.sort(iList);

            BagJump = Integer.parseInt(iList.get(0).toString());
            BagCount = 1;
            BatchesScanned = 0;
            values.add(String.format("%s",BagJump));
            values.add(String.format("%s",BagCount));
            values.add(String.format("%s",BatchesScanned));

            values.add(String.format("%s",iList.get(0)));
            values.add(String.format("%s",iList.get(1)));
            values.add(String.format("%s",iList.get(2)));
        }
        else{
            BagJump = Integer.parseInt(values.get(0));
            BagCount = Integer.parseInt(values.get(1));
            BatchesScanned = Integer.parseInt(values.get(2));
            CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
            List<String> totals = q.getGrnAmountCaptures(myCargo.getDocumentNr());
            txtTotalCount.setText(totals.get(0));
            txtTotalWeight.setText(totals.get(1));
        }

        ((master)getApplicationContext()).set_value2(values);
        List<String> spinnerItems = new ArrayList<String>();
        for(int x = BagCount; x > 0; x--){
            spinnerItems.add(String.format("%s",x));
        }
        q.CreateAdapter(cbSlingNr,spinnerItems);
        cbDocumentClicked = false;

        cbSlingNr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!cbDocumentClicked) return;
                cbSlingNr_ItemSelected();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        cbDocumentClicked = true;
    }

    private void cbSlingNr_ItemSelected(){
        query q  = new query(getBaseContext());
        String user = ((master)getApplicationContext()).getUser();
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();

        List<String> items = q.getBagSlingInfo(user, myCargo.getDocumentNr(), cbSlingNr.getSelectedItem().toString());
        txtCount.setText(items.get(0));
        txtWeight.setText(items.get(1));
    }

    public void cargoGrnSugarBags4_btnNext_Clicked(View v){
        if (txtWeight.getText() == null) return;
        if (txtCount.getText() == null) return;
        if (txtCount.getText().toString().equals("0")) return;
        if (txtWeight.getText().toString().equals("0")) return;

        query q = new query(getApplicationContext());

        String count = txtCount.getText().toString();
        String weight = txtWeight.getText().toString();
        String user = ((master)getApplicationContext()).getUser();
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        int SlingNr = Integer.parseInt(cbSlingNr.getSelectedItem().toString());



        tblErrorMessage errorMessage = q.setGateInSugarBags(user, myCargo.getDocumentNr(), count, weight, String.format("%s",SlingNr));
        if (errorMessage.getErrorCode() != 0) return;

        if (BagCount != SlingNr){
            cbSlingNr.setSelection(0);
            List<String> totals = q.getGrnAmountCaptures(myCargo.getDocumentNr());
            txtTotalCount.setText(totals.get(0));
            txtTotalWeight.setText(totals.get(1));
            Toast.makeText(this, errorMessage.getMsg(),Toast.LENGTH_SHORT).show();
            return;
        }

        if (BagCount == BagJump){
            List<String> values = ((master)getApplicationContext()).get_value2();
            if (values.size() > 3){
                values.remove(3);
                try {
                    values.set(0, values.get(3));
                }catch (Exception ignore){
                }
            }
            ((master)getApplicationContext()).set_value2(values);

            Intent intent = new Intent(this, CargoGrnSugarBags5.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(this, CargoGrnSugarBags6.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void cargoGrnSugarBags4_btnBack_Clicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm cancel");
        builder.setMessage("Are you sure you want to cancel, this will result in all slings already captured being lost?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAction();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO absolutely nothing
            }
        });

        builder.show();

    }
    public void cancelAction(){
        query q = new query(getBaseContext());
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        q.ResetGateInSugarBags(myCargo.getDocumentNr());

        Intent intent = new Intent(this, CargoGrnSugarBags3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
