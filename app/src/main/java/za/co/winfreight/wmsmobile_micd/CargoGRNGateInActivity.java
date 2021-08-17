package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Queue;

public class CargoGRNGateInActivity extends AppCompatActivity {
    Spinner cbDocumentNr;
    boolean cbDocumentClicked;
    TextView txtDocumentInfo;
    String documentNr;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cbDocumentClicked = true;

        setContentView(R.layout.activity_cargo_grn_gatein);
        cbDocumentNr = findViewById(+R.id.CargoGoodsReceiptGateInDocumentLookup_cbDocumentNr);
        txtDocumentInfo = findViewById(+R.id.CargoGoodsReceiptGateInDocumentLookup_txtDocumentInfo);
        documentNr = "";
        user = ((master)getApplicationContext()).getUser();

        txtDocumentInfo.setMovementMethod(new ScrollingMovementMethod());

        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        String GateInType = myCargo.getGateInType();
        query q = new query(getBaseContext());

        List<String> documentList = q.getGateInDocuments(((master)getApplicationContext()).getWarehouse(),GateInType);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, documentList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbDocumentNr.setAdapter(adapter);

        cbDocumentNr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!cbDocumentClicked) return;
                cbDocumentNr_ItemSelected();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void cbDocumentNr_ItemSelected(){
        if (cbDocumentNr.getSelectedItem() == null) return;

        documentNr = cbDocumentNr.getSelectedItem().toString();
        if (documentNr.equals("")) return;

        String warehouse = ((master)getApplicationContext()).getWarehouse();
        query q = new query(getBaseContext());
        tblErrorMessage documentInfo = q.getGateInDocumentInfo(warehouse,documentNr);
        if (documentInfo.getErrorCode() == 1){
            documentNr = "";

        }
        txtDocumentInfo.setText(documentInfo.getMsg());
    }

    public void CargoGoodsReceiptGateInDocumentLookup_btnBack_Clicked(View v){
        query q = new query(getApplicationContext());
        CargoGrnClass myCargo =  new CargoGrnClass();

        myCargo.setUserName(((master)getApplicationContext()).getUser());
        ((master)getApplicationContext()).set_cargoGoodsReceipt(myCargo);

        Intent intent = new Intent(this, CargoGrnActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods Receipt main menu");
        startActivity(intent);

    }

    public void CargoGoodsReceiptGateInDocumentLookup_btnNext_Clicked(View v){
        if (documentNr.equals("")) return;
        if (documentNr.equals("None")) return;
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        query q = new query(getBaseContext());
        tblErrorMessage errorMessage = q.CheckGateInProceed(documentNr, user);
        if (errorMessage.getErrorCode() > 0){
            Toast.makeText(this,errorMessage.getMsg(),Toast.LENGTH_SHORT).show();
            return;
        }

        if (myCargo.getGateInType().equals("SugarBags") || myCargo.getGateInType().equals("MineralBags"))
        {
            myCargo.setDocumentNr(documentNr);
            myCargo.setUserName(((master)getApplicationContext()).getUser());
            myCargo.setWarehouse(((master)getApplicationContext()).getWarehouse());

            ((master)getApplicationContext()).set_cargoGoodsReceipt(myCargo);

            Intent intent = new Intent(this, CargoGrnWeatherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        if (myCargo.getGateInType().equals("MineralLooseBulk"))
        {
            String gateInType = myCargo.getGateInType();
            String warehouse = ((master)getApplicationContext()).getWarehouse();

            myCargo = q.getGateInDetails(documentNr,warehouse);

            if (myCargo.getGateInType().equals("")){
                myCargo.setDocumentNr(documentNr);
                myCargo.setGateInType(gateInType);
                myCargo.setUserName(((master)getApplicationContext()).getUser());
                myCargo.setWarehouse(((master)getApplicationContext()).getWarehouse());

                ((master)getApplicationContext()).set_cargoGoodsReceipt(myCargo);

                Intent intent = new Intent(this, CargoGrnWeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return;
            }
            ((master)getApplicationContext()).set_cargoGoodsReceipt(myCargo);

            Intent intent = new Intent(this, CargoGrnMineralLooseBulk4.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}
