package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;

public class ProgramActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        query q = new query(getBaseContext());
        List<String> warehouseList = q.getWarehouses();

        Spinner spinner = findViewById(+R.id.program_cbWarehouse);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, warehouseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void program_btnExit_Clicked(View v){
        finish();
        System.exit(0);
    }

    public void program_btnGRN_Clicked(View v){
        query q = new query(getBaseContext());
        if (!q.AllotActivityAccess(((master)getApplicationContext()).getUser(), "Scanner Cargo Receipt").equals("1"))
        {
            Toast.makeText(this, "You do not have access.", Toast.LENGTH_SHORT).show();
            return;
        }

        Spinner spinner = findViewById(+R.id.program_cbWarehouse);
        String warehouse = spinner.getSelectedItem().toString();
        if (warehouse.equals("")){
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.please_select_warehouse),Toast.LENGTH_SHORT).show();
            return;
        }
        ((master)getApplication()).setWarehouse(warehouse);
        new query(getApplicationContext()).logMessage(((master)getApplication()), "Open Cargo Goods Receipt screen");
        Intent intent = new Intent(this, CargoGrnActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void program_btnDispatch_Clicked(View v){
        query q = new query(getBaseContext());
        if (!q.AllotActivityAccess(((master)getApplicationContext()).getUser(), "Scanner Cargo Dispatch").equals("1"))
        {
            Toast.makeText(this, "You do not have access.", Toast.LENGTH_SHORT).show();
            return;
        }

        Spinner spinner = findViewById(+R.id.program_cbWarehouse);
        String warehouse = spinner.getSelectedItem().toString();
        if (warehouse.equals("")){
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.please_select_warehouse),Toast.LENGTH_SHORT).show();
            return;
        }
        ((master)getApplication()).setWarehouse(warehouse);
        new query(getApplicationContext()).logMessage(((master)getApplication()), "Open Cargo Goods Dispatch screen");
        Intent intent = new Intent(this, CargoDispatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void program_btnLoadEmptyContainer_Clicked(View v){
        query q = new query(getBaseContext());
        if (!q.AllotActivityAccess(((master)getApplicationContext()).getUser(), "Scanner Load Container").equals("1"))
        {
            Toast.makeText(this, "You do not have access.", Toast.LENGTH_SHORT).show();
            return;
        }

        Spinner spinner = findViewById(+R.id.program_cbWarehouse);
        String warehouse = spinner.getSelectedItem().toString();
        if (warehouse.equals("")){
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.please_select_warehouse),Toast.LENGTH_SHORT).show();
            return;
        }
        ((master)getApplication()).setWarehouse(warehouse);


        Intent intent = new Intent(this, LoadEmptyContainerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void program_btnEmptyContainerStatusChange_Clicked(View v){
        query q = new query(getBaseContext());
        if (!q.AllotActivityAccess(((master)getApplicationContext()).getUser(), "Scanner Status Update").equals("1"))
        {
            Toast.makeText(this, "You do not have access.", Toast.LENGTH_SHORT).show();
            return;
        }

        Spinner spinner = findViewById(+R.id.program_cbWarehouse);
        String warehouse = spinner.getSelectedItem().toString();
        if (warehouse.equals("")){
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.please_select_warehouse),Toast.LENGTH_SHORT).show();
            return;
        }
        ((master)getApplication()).setWarehouse(warehouse);
        Intent intent = new Intent(this, EmptyContainerStatusChangeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void program_btnDispatchEmptyContaienrLoading_Clicked(View v){
        Intent intent = new Intent(this, DispatchEmptyContainerLoadingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void program_btnDTransferContainer_Clicked(View v){
        Spinner spinner = findViewById(+R.id.program_cbWarehouse);
        String warehouse = spinner.getSelectedItem().toString();
        if (warehouse.equals("")){
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.please_select_warehouse),Toast.LENGTH_SHORT).show();
            return;
        }
        ((master)getApplication()).setWarehouse(warehouse);
        Intent intent = new Intent(this, TransferContainerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
