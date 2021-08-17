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

public class CargoGrnMineralLooseBulk1 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    private boolean photoTruckRegistration;
    private boolean photoVehicle;
    private boolean photoStockpileBoard;
    private boolean photoFullTruckWithSeals;
    private boolean photoFullTruckWithTrailerATop;
    private boolean photoFullTruckWithTrailerBTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_mineral_loose_bulk1);

        photoTruckRegistration=false;
        photoVehicle=false;
        photoStockpileBoard=false;
        photoFullTruckWithSeals=false;
        photoFullTruckWithTrailerATop=false;
        photoFullTruckWithTrailerBTop=false;
        checkPicturesTaken();
    }
    private void checkPicturesTaken()
    {
    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    public void CargoGrnMineralLooseBulk1_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnMineralLooseBulk1_btnNext_Clicked(View v){
        if (!photoTruckRegistration){
            Toast.makeText(this,"Please take photo of truck registration",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoVehicle){
            Toast.makeText(this,"Please take photo of vehicle",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoStockpileBoard){
            Toast.makeText(this,"Please take photo of stockpile board",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoFullTruckWithSeals){
            Toast.makeText(this,"Please take photo of full truck with seals",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoFullTruckWithTrailerATop){
            Toast.makeText(this,"Please take photo of full truck with trailer A top",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoFullTruckWithTrailerBTop){
            Toast.makeText(this,"Please take photo of full truck with trailer B top",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, CargoGrnMineralLooseBulk2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnMineralLooseBulk1_btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "CargoGrnMineralLooseBulk1_btn1":{
                _pictureType = "TruckRegistration";
                break;
            }
            case "CargoGrnMineralLooseBulk1_btn2":{
                _pictureType = "Vehicle";
                break;
            }
            case "CargoGrnMineralLooseBulk1_btn3":{
                _pictureType = "StockpileBoard";
                break;
            }
            case "CargoGrnMineralLooseBulk1_btn4":{
                _pictureType = "FullTruckSeals";
                break;
            }
            case "CargoGrnMineralLooseBulk1_btn5":{
                _pictureType = "FullTruckTrailerATop";
                break;
            }
            case "CargoGrnMineralLooseBulk1_btn6":{
                _pictureType = "FullTruckTrailerBTop";
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
                btn = findViewById(R.id.CargoGrnMineralLooseBulk1_btn2);
                photoVehicle=true;
                break;
            }
            case "TruckRegistration":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk1_btn1);
                photoTruckRegistration=true;
                break;
            }
            case "StockpileBoard":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk1_btn3);
                photoStockpileBoard=true;
                break;
            }
            case "FullTruckSeals":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk1_btn4);
                photoFullTruckWithSeals=true;
                break;
            }
            case "FullTruckTrailerATop":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk1_btn5);
                photoFullTruckWithTrailerATop=true;
                break;
            }
            case "FullTruckTrailerBTop":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk1_btn6);
                photoFullTruckWithTrailerBTop=true;
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

