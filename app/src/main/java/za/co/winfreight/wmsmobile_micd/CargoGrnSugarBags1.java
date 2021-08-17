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
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CargoGrnSugarBags1 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    private boolean photoTruckRegistration;
    private boolean photoVehicle;
//    private boolean photoRightTarp;
//    private boolean photoLeftTarp;
//    private boolean photoTopTarp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_sugar_bags1);

//        photoLeftTarp=false;
//        photoRightTarp=false;
//        photoTopTarp=false;
        photoTruckRegistration=false;
        photoVehicle=false;
        checkPicturesTaken();
    }
    private void checkPicturesTaken()
    {
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void cargoGrnSugarBags1_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnWeatherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void cargoGrnSugarBags1_btnNext_Clicked(View v){
        /*
        if (!photoTopTarp){
            Toast.makeText(this,"Please take photo of top tarp",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoRightTarp){
            Toast.makeText(this,"Please take photo of right tarp",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoLeftTarp){
            Toast.makeText(this,"Please take photo of left tarp",Toast.LENGTH_SHORT).show();
            return;
        }
        */
        if (!photoTruckRegistration){
            Toast.makeText(this,"Please take photo of truck registration",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoVehicle){
            Toast.makeText(this,"Please take photo of vehicle",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, CargoGrnSugarBags2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void cargoGrnSugarBags1_btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "cargoGrnSugarBags1_btn1":{
                _pictureType = "TruckRegistration";
                break;
            }
            case "cargoGrnSugarBags1_btn2":{
                _pictureType = "Vehicle";
                break;
            }
            case "cargoGrnSugarBags1_btn3":{
                _pictureType = "RightTarp";
                break;
            }
            case "cargoGrnSugarBags1_btn4":{
                _pictureType = "LeftTarp";
                break;
            }
            case "cargoGrnSugarBags1_btn5":{
                _pictureType = "TopTarp";
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
            case "Vehicle":{
                btn = findViewById(R.id.cargoGrnSugarBags1_btn2);
                photoVehicle=true;
                break;
            }
            case "TruckRegistration":{
                btn = findViewById(R.id.cargoGrnSugarBags1_btn1);
                photoTruckRegistration=true;
                break;
            }
//            case "RightTarp":{
//                btn = findViewById(R.id.cargoGrnSugarBags1_btn3);
//                photoRightTarp=true;
//                break;
//            }
//            case "LeftTarp":{
//                btn = findViewById(R.id.cargoGrnSugarBags1_btn4);
//                photoLeftTarp=true;
//                break;
//            }
//            case "TopTarp":{
//                btn = findViewById(R.id.cargoGrnSugarBags1_btn5);
//                photoTopTarp=true;
//                break;
//            }
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

