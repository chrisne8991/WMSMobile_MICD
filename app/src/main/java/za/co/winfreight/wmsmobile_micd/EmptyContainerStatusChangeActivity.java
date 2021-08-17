package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EmptyContainerStatusChangeActivity extends AppCompatActivity {
    EditText txtContainerNr;
    Spinner cbStatus;
    TextView lblShippingLine;
    EditText txtComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_container_status_change);
        txtContainerNr = findViewById(R.id.EmptyContainerStatusChange_txtContainerNr);
        cbStatus = findViewById(R.id.EmptyContainerStatusChange_cbStatus);
        lblShippingLine = findViewById(R.id.EmptyContainerStatusChange_lblShippingLine);
        txtComments = findViewById(R.id.EmptyContainerStatusChange_txtComments);

        queryDispatch q = new queryDispatch(getBaseContext());
        q.CreateAdapter(cbStatus,q.getContainerStatus());

        txtContainerNr.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    Container_OnKeyPress();
                    return true;
                }
                return false;
            }
        });
    }

    private void Container_OnKeyPress(){
        String containerNr = txtContainerNr.getText().toString();
        if (containerNr.equals("")){
            return;
        }
        query q = new query(getBaseContext());
        String shippingLine = q.getShippingLine(containerNr);

        lblShippingLine.setText(String.format("Shipping Line: %s",shippingLine));

    }


    public void btnBack_Clicked(View v){
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void btnNext_Clicked(View v){
        if (txtContainerNr.getText() == null){
            Toast.makeText(this, "Please supply container nr",Toast.LENGTH_LONG).show();
            return;
        }
        if (txtContainerNr.getText().toString().equals("")){
            Toast.makeText(this, "Please supply container nr",Toast.LENGTH_LONG).show();
            return;
        }
        if (cbStatus.getSelectedItem() == null){
            Toast.makeText(this, "Please select status",Toast.LENGTH_LONG).show();
            return;
        }
        if (cbStatus.getSelectedItem().toString().equals("")){
            Toast.makeText(this, "Please select status",Toast.LENGTH_LONG).show();
            return;
        }
        String Warehouse = ((master)getApplicationContext()).getWarehouse();
        String User = ((master)getApplicationContext()).getUser();
        String ContainerNr = txtContainerNr.getText().toString();
        String Status = cbStatus.getSelectedItem().toString();
        String Comments = txtComments.getText().toString();
        queryDispatch q = new queryDispatch(getBaseContext());
        tblErrorMessage errorMessage = q.ChangeContainerStatus(Warehouse,User,ContainerNr,Status, Comments);
        Toast.makeText(this, errorMessage.getMsg(),Toast.LENGTH_LONG).show();

    }
}
