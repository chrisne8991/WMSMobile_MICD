package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CargoDispatchGateInActivity extends AppCompatActivity {
    Spinner cbDocumentNr;
    Spinner cbContainerNr;
    TextView txtDocumentInfo;

    String warehouse;
    String documentNr;
    String containerNr;
    Boolean cbDocumentClicked;

    Button continueButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_dispatch_gate_in);
        warehouse = ((master)getApplicationContext()).getWarehouse();
        cbContainerNr = findViewById(+R.id.CargoDispatchGateIn_cbContainer);
        cbDocumentNr = findViewById(+R.id.CargoDispatchGateInDocumentLookup_cbDocumentNr);
        txtDocumentInfo = findViewById(+R.id.CargoDispatchGateInDocumentLookup_txtDocumentInfo);
        cbDocumentClicked = false;
        documentNr = "";

        queryDispatch q = new queryDispatch(getBaseContext());
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();

        cbDocumentNr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!cbDocumentClicked) return;
                cbDocumentNr_ItemSelected();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        cbContainerNr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!cbDocumentClicked) return;
                cbContainerNr_ItemSelected();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        q.CreateAdapter(
                cbDocumentNr,
                q.getDocuments(
                        myCargo.getWarehouse(),
                        myCargo.getGateInType()
                )
        );
        txtDocumentInfo.setMovementMethod(new ScrollingMovementMethod());
        cbDocumentClicked = true;
    }

    public void cbDocumentNr_ItemSelected(){
        if (cbDocumentNr.getSelectedItem() == null) return;

        documentNr = cbDocumentNr.getSelectedItem().toString();
        if (documentNr.equals("")) return;

        queryDispatch q = new queryDispatch(getBaseContext());
        q.CreateAdapter(cbContainerNr,q.getContainers(documentNr));
    }

    public void cbContainerNr_ItemSelected(){
        if (documentNr.equals("")) return;
        if (cbContainerNr.getSelectedItem() == null) return;

        containerNr = cbContainerNr.getSelectedItem().toString();
        if (containerNr.equals("")) return;

        queryDispatch q = new queryDispatch(getBaseContext());
        tblErrorMessage documentInfo = q.getContainerInfo(containerNr);
        txtDocumentInfo.setText("");
        txtDocumentInfo.setText(documentInfo.getMsg());

        continueButton = findViewById(+R.id.CargoGoodsReceiptGateInDocumentLookup_btnContinue);
        nextButton = findViewById(+R.id.CargoGoodsReceiptGateInDocumentLookup_btnNext);

        CargoDispatchClass myCargo =  ((master)this.getApplication()).get_cargoDispatch();

        tblErrorMessage errorMessage = q.checkContinueSugarDispatchTransaction(((master)getApplicationContext()).getUser(), documentNr, containerNr);
        if (errorMessage.getErrorCode() == 0){
            continueButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
            return;
        }
        continueButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.VISIBLE);
    }

    public void CargoDispatchGateInDocumentLookup_btnBack_Clicked(View v){
        queryDispatch q = new queryDispatch(getApplicationContext());
        CargoDispatchClass myCargo =  ((master)this.getApplication()).get_cargoDispatch();
        myCargo.setGateInType("");
        ((master)this.getApplication()).set_cargoDispatch(myCargo);
        Intent intent = new Intent(this, CargoDispatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods dispatch main menu");
        startActivity(intent);
    }

    public void CargoDispatchGateInDocumentLookup_btnContinue_Clicked(View v){
        queryDispatch q = new queryDispatch(getApplicationContext());

        tblErrorMessage checkIfPickSlipExists = q.checkIfPickSlipExists(documentNr, containerNr);
        if (checkIfPickSlipExists.getErrorCode() != 0){
            Toast.makeText(this,checkIfPickSlipExists.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }

        CargoDispatchClass myCargo =  ((master)this.getApplication()).get_cargoDispatch();
        myCargo.setContainerNr(containerNr);
        myCargo.setDocumentNr(documentNr);
        Intent intent = new Intent(this, CargoDispatchSugarBags3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void CargoDispatchGateInDocumentLookup_btnNext_Clicked(View v){
        CargoDispatchClass myCargo =  ((master)this.getApplication()).get_cargoDispatch();
        queryDispatch q = new queryDispatch(getApplicationContext());

        tblErrorMessage checkIfPickSlipExists = q.checkIfPickSlipExists(documentNr, containerNr);
        if (checkIfPickSlipExists.getErrorCode() != 0){
            Toast.makeText(this,checkIfPickSlipExists.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }

        tblErrorMessage errorMessage = q.CheckDispatchProceed(documentNr, containerNr, ((master)getApplicationContext()).getUser());
        if (errorMessage.getErrorCode() > 0){
            Toast.makeText(this,errorMessage.getMsg(),Toast.LENGTH_SHORT).show();
            return;
        }


        myCargo.setContainerNr(containerNr);
        myCargo.setDocumentNr(documentNr);

        ((master)this.getApplication()).set_cargoDispatch(myCargo);
        if (myCargo.getGateInType().equals("SugarBags") || myCargo.getGateInType().equals("MineralBags"))
        {
            Intent intent = new Intent(this, CargoDispatchWeatherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        tblErrorMessage isPhotoTaken = q.isContainerPhotoTaken(documentNr,containerNr);

        if (isPhotoTaken.getErrorCode() == 0){
            //Intent intent = new Intent(this, CargoDispatch1.class);
            Toast.makeText(this, "Continuing with capture process...",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CargoDispatchSugarBags3.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }


        Intent intent = new Intent(this, CargoDispatch0.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
