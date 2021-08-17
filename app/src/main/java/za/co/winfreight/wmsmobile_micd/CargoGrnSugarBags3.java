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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CargoGrnSugarBags3 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    private boolean photoNoRightTarp;
    private boolean photoNoLeftTarp;
    private boolean photoNoTopTarp;

    EditText txtSlingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_sugar_bags3);

        photoNoLeftTarp=false;
        photoNoRightTarp=false;
        photoNoTopTarp=false;
        txtSlingCount = findViewById(+R.id.CargoGrnSugarBags3_txtSlingCount);
        checkPicturesTaken();
    }
    private void checkPicturesTaken()
    {
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void CargoGrnSugarBags3_btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "CargoGrnSugarBags3_btn1":{
                _pictureType = "NoRightTarp";
                break;
            }
            case "CargoGrnSugarBags3_btn2":{
                _pictureType = "NoLeftTarp";
                break;
            }
            case "CargoGrnSugarBags3_btn3":{
                _pictureType = "NoTopTarp";
                break;
            }
            default:{
                return;
            }
        }
        takePhoto(_pictureType);
    }
    public void CargoGrnSugarBags3_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnSugarBags2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void CargoGrnSugarBags3_btnNext_Clicked(View v){
        ((master)getApplicationContext()).set_value("0");
        if (!photoNoTopTarp){
            Toast.makeText(this,"Please take photo of top tarp",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoNoRightTarp){
            Toast.makeText(this,"Please take photo of right tarp",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoNoLeftTarp){
            Toast.makeText(this,"Please take photo of left tarp",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtSlingCount.getText()==null){
            Toast.makeText(this,"Please provide number of slings",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtSlingCount.getText().toString().equals("")){
            Toast.makeText(this,"Please provide number of slings",Toast.LENGTH_SHORT).show();
            return;
        }
        int testValue = 0;
        try
        {
            testValue = Integer.parseInt(txtSlingCount.getText().toString());
            if (testValue == 0){
                Toast.makeText(this,"Please provide valid number of slings",Toast.LENGTH_SHORT).show();
                return;
            }
        }catch(Exception e){
            Toast.makeText(this,"Please provide valid number of slings",Toast.LENGTH_SHORT).show();
            return;
        }
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        String DocumentNr = myCargo.getDocumentNr();

        new query(getBaseContext()).ResetGateInSugarBags(DocumentNr);
        query q = new query(getBaseContext());
        q.SetGateInSlingCount(DocumentNr, txtSlingCount.getText().toString());

        ((master)getApplicationContext()).set_value(txtSlingCount.getText().toString());

        List<String> values = ((master)getApplicationContext()).get_value2();
        values.clear();
        ((master)getApplicationContext()).set_value2(values);

        Intent intent = new Intent(this, CargoGrnSugarBags4.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    //Call takePhoto to take the bloody photo.
    //Add the result of what should happen once the picture has been taken here{
    public void afterPhotoTakenAction(){
        //will only execute if successful
        Button btn;
        switch (_pictureType){
            case "NoRightTarp":{
                btn = findViewById(R.id.CargoGrnSugarBags3_btn1);
                photoNoRightTarp=true;
                break;
            }
            case "NoLeftTarp":{
                btn = findViewById(R.id.CargoGrnSugarBags3_btn2);
                photoNoLeftTarp=true;
                break;
            }
            case "NoTopTarp":{
                btn = findViewById(R.id.CargoGrnSugarBags3_btn3);
                photoNoTopTarp=true;
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
