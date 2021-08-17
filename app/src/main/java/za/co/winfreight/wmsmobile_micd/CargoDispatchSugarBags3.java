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

public class CargoDispatchSugarBags3 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    boolean photoFullPackedContainerBothDoorOpen;
    boolean photoFullPackedContainerOneDoorOpen;
    boolean photoFullPackedContainerDoorsClosed;
    boolean photoYardClerk;

    Button btnPhotoClerk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_dispatch_sugar_bags3);

        photoFullPackedContainerBothDoorOpen=false;
        photoFullPackedContainerOneDoorOpen=false;
        photoFullPackedContainerDoorsClosed=false;
        photoYardClerk=false;

        btnPhotoClerk = findViewById(R.id.CargoDispatch3_btn4);

        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        if (myCargo.getGateInType().equals("MineralBags")){
            btnPhotoClerk.setVisibility(View.VISIBLE);
        }
        checkPicturesTaken();
    }

    private void checkPicturesTaken()
    {
        queryDispatch q = new queryDispatch(getBaseContext());
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        tblErrorMessage errorMessage = new tblErrorMessage();
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"FullPackedContainerBothDoorOpen");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "FullPackedContainerBothDoorOpen";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"FullPackedContainerOneDoorOpen");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "FullPackedContainerOneDoorOpen";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"FullPackedContainerDoorsClosed");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "FullPackedContainerDoorsClosed";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"YardClerk");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "YardClerk";
            afterPhotoTakenAction();
        }

    }


    public void btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "btn1":{
                _pictureType = "FullPackedContainerBothDoorOpen";
                break;
            }
            case "btn2":{
                _pictureType = "FullPackedContainerOneDoorOpen";
                break;
            }
            case "btn3":{
                _pictureType = "FullPackedContainerDoorsClosed";
                break;
            }
            case "btn4":{
                _pictureType = "YardClerk";
                break;
            }
            default:{
                return;
            }
        }
        takePhoto(_pictureType);
    }

    public void btnBack_Clicked(View v){
        queryDispatch q = new queryDispatch(getBaseContext());
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        if (myCargo.getGateInType().equals("MineralLooseBulk")){
            Intent intent = new Intent(this, CargoDispatchGateInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        if (myCargo.getGateInType().equals("SugarBags")){
            tblErrorMessage errorMessage = q.checkContinueSugarDispatchTransaction(((master)getApplicationContext()).getUser(), myCargo.getDocumentNr(), myCargo.getContainerNr());
            if (errorMessage.getErrorCode() == 0){
                Intent intent = new Intent(this, CargoDispatchGateInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return;
            }
        }

        Intent intent = new Intent(this, CargoDispatchSugarBags2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void btnNext_Clicked(View v){
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        if (!photoFullPackedContainerBothDoorOpen){
            Toast.makeText(this,"Please take photo of the container with both doors open",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoFullPackedContainerOneDoorOpen){
            Toast.makeText(this,"Please take photo of the container with one door open",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoFullPackedContainerDoorsClosed){
            Toast.makeText(this,"Please take photo of the container with doors closed",Toast.LENGTH_SHORT).show();
            return;
        }
        if (myCargo.getGateInType().equals("MineralBags")){
            if (!photoYardClerk){
                Toast.makeText(this,"Please take photo of the yard clerk",Toast.LENGTH_SHORT).show();
                return;
            }
            queryDispatch q = new queryDispatch(getBaseContext());
            try{
                if (myCargo.getWeatherConditions().equals("")){
                    String weatherCondition = q.getDispatchWeatherConditions(myCargo.getUserName(), myCargo.getDocumentNr(), myCargo.getContainerNr());
                    myCargo.setWeatherConditions(weatherCondition);
                }
            }catch (Exception ignore){}

            tblErrorMessage errorMessage = q.setDispatchDetails(
                    myCargo.getUserName(),
                    myCargo.getDocumentNr(),
                    myCargo.getContainerNr(),
                    myCargo.getWeatherConditions(),
                    "",
                    "",
                    "",
                    "ContainerPacked",
                    ""
            );

            q.removeContinueSugarDispatchTransaction(myCargo.getUserName(), myCargo.getDocumentNr(), myCargo.getContainerNr());

            Toast.makeText(this, errorMessage.getMsg(),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CargoDispatchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        if (myCargo.getGateInType().equals("MineralLooseBulk")){
            Intent intent = new Intent(this, CargoDispatch1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(this, CargoDispatchSugarBags4.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Call takePhoto to take the bloody photo.
    //Add the result of what should happen once the picture has been taken here{
    public void afterPhotoTakenAction(){
        //will only execute if successful
        Button btn;
        switch (_pictureType){
            case "FullPackedContainerBothDoorOpen":{
                btn = findViewById(R.id.CargoDispatch3_btn1);
                photoFullPackedContainerBothDoorOpen=true;
                break;
            }
            case "FullPackedContainerOneDoorOpen":{
                btn = findViewById(R.id.CargoDispatch3_btn2);
                photoFullPackedContainerOneDoorOpen=true;
                break;
            }
            case "FullPackedContainerDoorsClosed":{
                btn = findViewById(R.id.CargoDispatch3_btn3);
                photoFullPackedContainerDoorsClosed=true;
                break;
            }
            case "YardClerk":{
                btn = findViewById(R.id.CargoDispatch3_btn4);
                photoYardClerk=true;
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

                CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
                String DocumentNr = myCargo.getDocumentNr();
                String UserName = ((master)getApplicationContext()).getUser();
                String ContainerNr = myCargo.getContainerNr();
                queryDispatch q = new queryDispatch(getApplicationContext());
                tblErrorMessage errorMessage = q.UploadDispatchPictures(DocumentNr, ContainerNr, UserName,_pictureType,inputData);
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
