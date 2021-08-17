package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CargoGrnMineralBags3 extends AppCompatActivity {
    TextView lblSlingNr;
    EditText txtWeight;
    EditText txtNumberOfBags;
    int BagCount;
    int BagCountTotal;
    LinearLayout llHowMany;
    TextView txtTotalCount;
    TextView txtTotalWeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_mineral_bags3);

        BagCount = 1;
        BagCountTotal = 0;

        llHowMany = findViewById(R.id.CargoGrnMineralBags3_llHowMany);
        txtWeight = findViewById(R.id.CargoGrnMineralBags3_txtWeight);
        txtNumberOfBags = findViewById(R.id.CargoGrnMineralBags3_txtNumberOfBags);
        lblSlingNr = findViewById(R.id.CargoGrnMineralBags3_lblBagNr);

        txtTotalCount = findViewById(R.id.CargoGrnMineralBags3_txtTotalCount);
        txtTotalWeight = findViewById(R.id.CargoGrnMineralBags3_txtTotalWeight);

        query q = new query(getBaseContext());
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        List<String> totals = q.getGrnAmountCaptures(myCargo.getDocumentNr());
        txtTotalCount.setText(totals.get(0));
        txtTotalWeight.setText(totals.get(1));
    }

    public void CargoGrnMineralBags3_btnNext_Clicked(View v){
        if (txtWeight.getText() == null) return;
        if (txtWeight.getText().toString().equals("0")) return;
        if (txtNumberOfBags.getText() == null) return;
        if (txtNumberOfBags.getText().toString().equals("0")) return;

        query q = new query(getApplicationContext());
        BagCountTotal = Integer.parseInt(txtNumberOfBags.getText().toString());

        String count = "1";
        String weight = txtWeight.getText().toString();
        String user = ((master)getApplicationContext()).getUser();
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();

        tblErrorMessage errorMessage = q.setGateInSugarBags(user,myCargo.getDocumentNr(),count,weight,"");
        if (errorMessage.getErrorCode() != 0) return;
        llHowMany.setVisibility(View.INVISIBLE);
        BagCount++;
        if (BagCount > BagCountTotal){
            Intent intent = new Intent(this, CargoGrnSugarBags6.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            lblSlingNr.setText(String.format("%s", BagCount));
            List<String> totals = q.getGrnAmountCaptures(myCargo.getDocumentNr());
            txtTotalCount.setText(totals.get(0));
            txtTotalWeight.setText(totals.get(1));
        }
    }

    public void CargoGrnMineralBags3_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnMineralBags2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
