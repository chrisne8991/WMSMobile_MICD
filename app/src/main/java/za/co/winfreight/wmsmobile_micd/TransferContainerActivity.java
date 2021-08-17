package za.co.winfreight.wmsmobile_micd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class TransferContainerActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Spinner cbContainer;
    Spinner cbCustomer;
    Spinner cbBookingRef;
    EditText txtDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_container);
        cbContainer = findViewById(+R.id.transfer_container_cbContainer);
        cbCustomer = findViewById(+R.id.transfer_container_cbCustomer);
        cbBookingRef = findViewById(+R.id.transfer_container_cbBookingRef);
        txtDate = findViewById(+R.id.transfer_container_Date);

        queryDispatch q = new queryDispatch(getBaseContext());
        List<String> container_numbers = q.getContainers("");
        List<String> customers = q.getTransferCustomers();
        List<String> bookingRefs = q.getTransferBookingReference();


        q.CreateAdapter(cbContainer,container_numbers);
        q.CreateAdapter(cbCustomer,customers);
        q.CreateAdapter(cbBookingRef,bookingRefs);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    public void btnBack_Clicked(View v){
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void btnNext_Clicked(View v){
        String Warehouse = ((master)getApplicationContext()).getWarehouse();
        String User = ((master)getApplicationContext()).getUser();
        if (cbContainer.getSelectedItem() == null){
            Toast.makeText(this,"Invalid container",Toast.LENGTH_SHORT).show();
            return;
        }
        if (cbContainer.getSelectedItem().toString().equals("")){
            Toast.makeText(this,"Invalid container",Toast.LENGTH_SHORT).show();
            return;
        }
        if (cbCustomer.getSelectedItem() == null){
            Toast.makeText(this,"Invalid customer",Toast.LENGTH_SHORT).show();
            return;
        }
        if (cbCustomer.getSelectedItem().toString().equals("")){
            Toast.makeText(this,"Invalid customer",Toast.LENGTH_SHORT).show();
            return;
        }
        if (cbBookingRef.getSelectedItem() == null){
            Toast.makeText(this,"Invalid booking reference",Toast.LENGTH_SHORT).show();
            return;
        }
        if (cbBookingRef.getSelectedItem().toString().equals("")){
            Toast.makeText(this,"Invalid booking reference",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtDate.getText() == null){
            Toast.makeText(this,"Invalid transfer date",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtDate.getText().toString().equals("")){
            Toast.makeText(this,"Invalid transfer date",Toast.LENGTH_SHORT).show();
            return;
        }
        String Container = cbContainer.getSelectedItem().toString();
        String Customer = cbCustomer.getSelectedItem().toString();
        String BookingReference = cbBookingRef.getSelectedItem().toString();
        String Date = txtDate.getText().toString();

        queryDispatch q = new queryDispatch(getBaseContext());
        tblErrorMessage errorMessage = q.transferContainer(Warehouse, User, Customer, BookingReference,Container,Date);
        Toast.makeText(this,errorMessage.getMsg(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = android.text.format.DateFormat.format("yyyy-MM-dd", c.getTime()).toString();
        txtDate.setText(currentDateString);
    }
}