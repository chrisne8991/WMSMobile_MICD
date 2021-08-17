package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoadEmptyContainerActivity extends AppCompatActivity {
    Spinner cbDocumentNr;
    Spinner cbContainerNr;
    Spinner cbContainerNr2;
    TextView txtDocumentInfo;

    String warehouse;
    String containerNr;
    String containerNr2;

    Boolean first_run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_empty_container);
        warehouse = ((master)getApplicationContext()).getWarehouse();
        cbContainerNr = findViewById(+R.id.LoadEmptyContainer_cbContainer);
        cbContainerNr2 = findViewById(+R.id.LoadEmptyContainer_cbContainer2);
        cbDocumentNr = findViewById(+R.id.LoadEmptyContainer_cbDocumentNr);
        txtDocumentInfo = findViewById(+R.id.LoadEmptyContainer_txtDocumentInfo);
        first_run = true;

        cbDocumentNr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (first_run) return;
                cbDocumentNr_ItemSelected();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        cbContainerNr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (first_run) return;
                cbContainerNr_ItemSelected();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        cbContainerNr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (first_run) return;
                cbContainerNr2_ItemSelected();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        queryDispatch q = new queryDispatch(getBaseContext());

        q.CreateAdapter(
                cbDocumentNr,
                q.getECLDocuments(warehouse)
        );
        first_run = false;
    }

    public void cbDocumentNr_ItemSelected(){
        if (cbDocumentNr.getSelectedItem() == null) return;
        if (cbDocumentNr.getSelectedItem().toString().equals("")) return;
        if (cbDocumentNr.getSelectedItem().toString().equals("None")) return;

        queryDispatch q = new queryDispatch(getBaseContext());

        q.CreateAdapter(
                cbContainerNr,
                q.getECLContainers(warehouse, cbDocumentNr.getSelectedItem().toString())
        );

        q.CreateAdapter(
                cbContainerNr2,
                q.getECLContainers(warehouse, cbDocumentNr.getSelectedItem().toString())
        );

    }

    public void cbContainerNr_ItemSelected(){
        if (cbContainerNr.getSelectedItem() == null) return;

        containerNr = cbContainerNr.getSelectedItem().toString();
        if (containerNr.equals("")) return;
        if (containerNr.equals("None")) return;

        queryDispatch q = new queryDispatch(getBaseContext());
        tblErrorMessage documentInfo = q.getECLContainerInfo(containerNr, containerNr2);
        txtDocumentInfo.setText("None");
        if (documentInfo.getErrorCode() == 0){
            txtDocumentInfo.setText(documentInfo.getMsg());
        }
    }

    public void cbContainerNr2_ItemSelected(){
        if (cbContainerNr2.getSelectedItem() == null) return;

        containerNr2 = cbContainerNr2.getSelectedItem().toString();
        if (containerNr2.equals("")) return;
        if (containerNr2.equals("None")) return;

        queryDispatch q = new queryDispatch(getBaseContext());
        tblErrorMessage documentInfo = q.getECLContainerInfo(containerNr, containerNr2);
        txtDocumentInfo.setText("");
        if (documentInfo.getErrorCode() == 0){
            txtDocumentInfo.setText(documentInfo.getMsg());
        }
    }

    public void btnBack_Clicked(View v){
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void btnNext_Clicked(View v){
        if (cbContainerNr.getSelectedItem() == null) {
            Toast.makeText(this,"Please supply container number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(cbDocumentNr.getSelectedItem() == null){
            Toast.makeText(this, "Please supply document number",Toast.LENGTH_SHORT).show();
            return;
        }
        String containerNr = cbContainerNr.getSelectedItem().toString();
        String containerNr2 = cbContainerNr2.getSelectedItem().toString();
        String documentNr = cbDocumentNr.getSelectedItem().toString();
        String userName = ((master)getApplicationContext()).getUser();
        EmptyContainerLoadingClass emptyContainerLoadingClass = new EmptyContainerLoadingClass();
        emptyContainerLoadingClass.setPadNr(documentNr);
        emptyContainerLoadingClass.setContainerNr(containerNr);
        emptyContainerLoadingClass.setContainerNr2(containerNr2);
        emptyContainerLoadingClass.setWarehouse(warehouse);
        emptyContainerLoadingClass.setUserName(userName);

        ((master)getApplicationContext()).set_emptyContainerLoading(emptyContainerLoadingClass);

        Intent intent = new Intent(this, LoadEmptyContainerDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
