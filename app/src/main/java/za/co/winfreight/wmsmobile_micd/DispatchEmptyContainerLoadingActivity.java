package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.ClientInfoStatus;
import java.util.List;

public class DispatchEmptyContainerLoadingActivity extends AppCompatActivity {
    Spinner cbBookingReference;
    EditText txtDocumentNr;
    Spinner cbDocumentNr;
    Spinner cbClient;
    Spinner cbShippingLine;
    EditText txtContainersBooked;
    Spinner cbContainer1;
    Spinner cbContainer2;
    Spinner cbContainerStatus;
    EditText txtContainersDispatched;
    EditText txtContainersRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_empty_container_loading);
        cbBookingReference = findViewById(+R.id.decl_cbBookingReference);
        txtDocumentNr = findViewById(+R.id.decl_txtDocumentNr);
        cbDocumentNr = findViewById(+R.id.decl_cbDocumentNr);
        cbClient = findViewById(+R.id.decl_cbClient);
        cbShippingLine = findViewById(+R.id.decl_cbShippingLine);
        txtContainersBooked = findViewById(+R.id.decl_txtContainersBooked);
        cbContainer2 = findViewById(+R.id.decl_cbContainer2);
        cbContainer1 = findViewById(+R.id.decl_cbContainer1);
        cbContainerStatus = findViewById(+R.id.decl_cbContainerStatus);
        txtContainersDispatched = findViewById(+R.id.decl_txtContainersDispatched);
        txtContainersRemaining = findViewById(+R.id.decl_txtRemainingContainers);

        queryDispatch q = new queryDispatch(getBaseContext());
        q.CreateAdapter(cbBookingReference,q.getDeclValues("@BookingReference"));
        q.CreateAdapter(cbDocumentNr,q.getDeclValues("@DocumentNr"));
        q.CreateAdapter(cbClient,q.getDeclValues("@Client"));
        q.CreateAdapter(cbContainer1,q.getDeclValues("@ContainerNumber1"));
        q.CreateAdapter(cbContainer2,q.getDeclValues("@ContainerNumber2"));
        q.CreateAdapter(cbContainerStatus,q.getDeclValues("@ContainerStatus"));
        q.CreateAdapter(cbShippingLine,q.getDeclValues("@ShippingLine"));


        cbBookingReference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (cbBookingReference.getSelectedItem() == null) return;
                if (cbBookingReference.getSelectedItem().toString().equals("")) return;
                queryDispatch q = new queryDispatch(getBaseContext());
                List<String> values = q.getDeclBookingRefValues(cbBookingReference.getSelectedItem().toString());

                List<String> documents = q.getDeclValues("@DocumentNr");
                List<String> clients = q.getDeclValues("@Client");
                List<String> shippinglines = q.getDeclValues("@ShippingLine");
                List<String> containers = q.getDeclValues("@Container1");
                List<String> status = q.getDeclValues("@ContainerStatus");


                cbDocumentNr.setSelection(documents.indexOf(values.get(0)));
                cbClient.setSelection(clients.indexOf(values.get(1)));
                cbShippingLine.setSelection(shippinglines.indexOf(values.get(2)));
                txtContainersBooked.setText(values.get(3));
                cbContainer1.setSelection(containers.indexOf(values.get(4)));
                cbContainer2.setSelection(containers.indexOf(values.get(5)));
                cbContainerStatus.setSelection(status.indexOf(values.get(6)));
                txtContainersDispatched.setText(values.get(7));
                txtContainersRemaining.setText(values.get(8));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    public void btnBack_Clicked(View v){
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void btnNext_Clicked(View v){
        if (cbBookingReference.getSelectedItem() == null){
            Toast.makeText(this, "Invalid Booking Reference", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbBookingReference.getSelectedItem().toString().equals("")){
            Toast.makeText(this, "Invalid Booking Reference", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbBookingReference.getSelectedItem().toString().equals("None")){
            Toast.makeText(this, "Invalid Booking Reference", Toast.LENGTH_LONG).show();
            return;
        }
        String bookingReference = cbBookingReference.getSelectedItem().toString();

        String documentNr = "";
        if (cbDocumentNr.getVisibility() == View.GONE){
            if (txtDocumentNr.getText() == null){
                Toast.makeText(this, "Invalid document Nr", Toast.LENGTH_LONG).show();
                return;
            }
            if (txtDocumentNr.getText().toString().equals("")){
                Toast.makeText(this, "Invalid document Nr", Toast.LENGTH_LONG).show();
                return;
            }
            documentNr = txtDocumentNr.getText().toString();
        }else{
            if (cbDocumentNr.getSelectedItem() == null){
                Toast.makeText(this, "Invalid document Nr", Toast.LENGTH_LONG).show();
                return;
            }
            if (cbDocumentNr.getSelectedItem().toString().equals("")){
                Toast.makeText(this, "Invalid document Nr", Toast.LENGTH_LONG).show();
                return;
            }
            if (cbDocumentNr.getSelectedItem().toString().equals("None")){
                Toast.makeText(this, "Invalid document Nr", Toast.LENGTH_LONG).show();
                return;
            }
            documentNr = cbDocumentNr.getSelectedItem().toString();
        }

        if (cbClient.getSelectedItem() == null){
            Toast.makeText(this, "Invalid client", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbClient.getSelectedItem().toString().equals("")){
            Toast.makeText(this, "Invalid client", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbClient.getSelectedItem().toString().equals("None")){
            Toast.makeText(this, "Invalid client", Toast.LENGTH_LONG).show();
            return;
        }
        String client = cbClient.getSelectedItem().toString();

        if (cbShippingLine.getSelectedItem() == null){
            Toast.makeText(this, "Invalid shipping line", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbShippingLine.getSelectedItem().toString().equals("")){
            Toast.makeText(this, "Invalid shipping line", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbShippingLine.getSelectedItem().toString().equals("None")){
            Toast.makeText(this, "Invalid shipping line", Toast.LENGTH_LONG).show();
            return;
        }
        String shippingLine = cbShippingLine.getSelectedItem().toString();

        if (cbContainer1.getSelectedItem() == null){
            Toast.makeText(this, "Invalid container 1", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbContainer1.getSelectedItem().toString().equals("")){
            Toast.makeText(this, "Invalid container 1", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbContainer1.getSelectedItem().toString().equals("None")){
            Toast.makeText(this, "Invalid container 1", Toast.LENGTH_LONG).show();
            return;
        }
        String container1 = cbContainer1.getSelectedItem().toString();

        if (cbContainer2.getSelectedItem() == null){
            Toast.makeText(this, "Invalid container 2", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbContainer2.getSelectedItem().toString().equals("")){
            Toast.makeText(this, "Invalid container 2", Toast.LENGTH_LONG).show();
            return;
        }
        String container2 = cbContainer2.getSelectedItem().toString();

        if (cbContainerStatus.getSelectedItem() == null){
            Toast.makeText(this, "Invalid container status", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbContainerStatus.getSelectedItem().toString().equals("")){
            Toast.makeText(this, "Invalid container status", Toast.LENGTH_LONG).show();
            return;
        }
        if (cbContainerStatus.getSelectedItem().toString().equals("None")){
            Toast.makeText(this, "Invalid container status", Toast.LENGTH_LONG).show();
            return;
        }
        String containerStatus = cbContainerStatus.getSelectedItem().toString();

        String bookedContainers = txtContainersBooked.getText().toString();
        String dispatchContainers = txtContainersDispatched.getText().toString();
        String remainingContainers = txtContainersRemaining.getText().toString();
        String username = ((master)getApplicationContext()).getUser();

        queryDispatch q = new queryDispatch(getBaseContext());
        tblErrorMessage errorMessage = q.setDecl(
                username,
                bookingReference,
                documentNr,
                client,
                shippingLine,
                bookedContainers,
                container1,
                container2,
                containerStatus,
                dispatchContainers,
                remainingContainers);

        Toast.makeText(this,errorMessage.getMsg(),Toast.LENGTH_LONG).show();
    }
    public void lblDocumentNr_Clicked(View v){
        if (cbDocumentNr.getVisibility() == View.GONE)
        {
            cbDocumentNr.setVisibility(View.VISIBLE);
            txtDocumentNr.setVisibility(View.GONE);
            return;
        }
        cbDocumentNr.setVisibility(View.GONE);
        txtDocumentNr.setVisibility(View.VISIBLE);
    }

}
