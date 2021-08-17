package za.co.winfreight.wmsmobile_micd;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CargoGrnSugarBags11 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    private boolean photoForkLift;
    private boolean photoWhsTallyClerk;
    private boolean doneWithTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_sugar_bags11);

        photoForkLift=false;
        photoWhsTallyClerk=false;
        doneWithTransaction=false;

        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        if (myCargo.getGateInType().equals("MineralLooseBulk") || myCargo.getGateInType().equals("MineralBags")){
            Button forkLiftDriver = findViewById(+R.id.CargoGrnSugarBags11_btn1);
            Button tallyClerk = findViewById(+R.id.CargoGrnSugarBags11_btn2);

            tallyClerk.setText(R.string.mineral_tally_clerk);
            forkLiftDriver.setVisibility(View.GONE);
            photoForkLift=true;
        }
        else {
            Button tallyClerk = findViewById(+R.id.CargoGrnSugarBags11_btn2);
            tallyClerk.setText(R.string.whs_tally_clerk);
        }
        checkPicturesTaken();
    }
    private void checkPicturesTaken()
    {
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void CargoGrnSugarBags11_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnWeatherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnSugarBags11_btnNext_Clicked(View v){
        if (doneWithTransaction){
            Intent intent = new Intent(this, CargoGrnActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (!photoForkLift){
            Toast.makeText(this,"Please take photo of forklift driver",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoWhsTallyClerk){
            Toast.makeText(this,"Please take photo of Tally clerk",Toast.LENGTH_SHORT).show();
            return;
        }
        EditText txtComments = findViewById(+R.id.CargoGrnSugarBags11_txtComments);
        if (txtComments.getText() == null){
            Toast.makeText(this,"Please supply comments",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtComments.getText().toString().trim().equals("")){
            Toast.makeText(this,"Please supply valid comments",Toast.LENGTH_SHORT).show();
            return;
        }
        TextView lblGrnNr = findViewById(+R.id.CargoGrnSugarBags11_txtGrnNr);
        query q = new query(getApplicationContext());
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        tblErrorMessage errorMessage = q.setGateInDetailsFinished(myCargo.getUserName(),myCargo.getDocumentNr(),myCargo.getWarehouse(),txtComments.getText().toString());

        ((master)getApplicationContext()).set_value("");
        ((master)getApplicationContext()).set_value2(new ArrayList<String>());

        lblGrnNr.setText(errorMessage.getMsg());
        doneWithTransaction = true;
    }

    public void CargoGrnSugarBags11_btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "CargoGrnSugarBags11_btn1":{
                _pictureType = "ForkliftDriver";
                break;
            }
            case "CargoGrnSugarBags11_btn2":{
                _pictureType = "WHSTallyClerk";
                break;
            }
            default:{
                return;
            }
        }
        takePhoto(_pictureType);
    }

    //Call takePhoto to take the bloody photo.
    //Add the result of what should happen once the picture has been taken here{
    public void afterPhotoTakenAction(){
        //will only execute if successful
        Button btn;
        switch (_pictureType){
            case "WHSTallyClerk":{
                btn = findViewById(R.id.CargoGrnSugarBags11_btn2);
                photoWhsTallyClerk=true;
                break;
            }
            case "ForkliftDriver":{
                btn = findViewById(R.id.CargoGrnSugarBags11_btn1);
                photoForkLift=true;
                break;
            }
            default:{
                return;
            }
        }
        btn.setBackground(getDrawable(R.drawable.rounded_button_press));

    }
    //}
    //Leave the following code alone{
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults){
        if (requestCode == PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (_pictureType.equals("")) return;
        if (resultCode == RESULT_OK){
            //we can get the image from the imageUri
            try{
                InputStream iStream =   getContentResolver().openInputStream(imageUri);
                if (iStream == null) return;

                byte[] inputData = getBytes(iStream);

                CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
                String DocumentNr = myCargo.getDocumentNr();
                String UserName = ((master)getApplicationContext()).getUser();
                query q = new query(getApplicationContext());
                tblErrorMessage errorMessage = q.UploadGateInPicture(DocumentNr, UserName,_pictureType,inputData);
                Toast.makeText(this,errorMessage.getMsg(),Toast.LENGTH_SHORT).show();
                afterPhotoTakenAction();
            }catch(IOException ignored){
            }
        }
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public void takePhoto(String pictureType){
        if (pictureType.equals("")) return;
        _pictureType = pictureType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            }
            else{
                openCamera();
            }
        }
        else{
            openCamera();
        }
    }
    //}
}

