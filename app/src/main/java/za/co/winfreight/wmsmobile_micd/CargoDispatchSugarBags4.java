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

public class CargoDispatchSugarBags4 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    boolean photoFullPackedContainerWithSeals;
    boolean photoYardClerk;
    boolean photoSeal1;
    boolean photoSeal2;
    boolean photoSeal3;

    EditText txtSeal1;
    EditText txtSeal2;
    EditText txtSeal3;
    EditText txtComents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_dispatch_sugar_bags4);
        photoFullPackedContainerWithSeals = false;
        photoYardClerk = false;
        photoSeal1 = false;
        photoSeal2 = false;
        photoSeal3 = false;
        txtSeal1 = findViewById(+R.id.CargoDispatchSugarBags4_txtSeal1);
        txtSeal2 = findViewById(+R.id.CargoDispatchSugarBags4_txtSeal2);
        txtSeal3 = findViewById(+R.id.CargoDispatchSugarBags4_txtSeal3);
        txtComents = findViewById(+R.id.CargoDispatchSugarBags4_txtComments);
        checkPicturesTaken();
    }
    private void checkPicturesTaken()
    {
        queryDispatch q = new queryDispatch(getBaseContext());
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        tblErrorMessage errorMessage = new tblErrorMessage();
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"FullPackedContainerWithSeals");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "FullPackedContainerWithSeals";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"YardClerk");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "YardClerk";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"Seal1");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "Seal1";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"Seal2");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "Seal2";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"Seal3");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "Seal3";
            afterPhotoTakenAction();
        }
    }
    public void btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "btn1":{
                _pictureType = "FullPackedContainerWithSeals";
                break;
            }
            case "btn2":{
                _pictureType = "YardClerk";
                break;
            }
            case "btn3":{
                _pictureType = "Seal1";
                break;
            }
            case "btn4":{
                _pictureType = "Seal2";
                break;
            }
            case "btn5":{
                _pictureType = "Seal3";
                break;
            }
            default:{
                return;
            }
        }
        takePhoto(_pictureType);
    }
    public void btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoDispatchSugarBags3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void btnNext_Clicked(View v){
        if (!photoFullPackedContainerWithSeals){
            Toast.makeText(this,"Please take photo of container with seals",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoYardClerk){
            Toast.makeText(this,"Please take photo of yard clerk",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtSeal1 == null) return;
        if (txtSeal2 == null) return;
        if (txtSeal3 == null) return;

        if (txtSeal1.getText().toString().equals("")) {
            Toast.makeText(this,"Please supply seal 1",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtSeal2.getText().toString().equals("")) {
            Toast.makeText(this,"Please supply seal 2",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtSeal3.getText().toString().equals("")) {
            Toast.makeText(this,"Please supply seal 3",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoSeal1 && !(txtSeal1.getText().toString().equals("") && txtSeal1.getText().toString().equals("0"))){
            Toast.makeText(this,"Please take photo of seal 1",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoSeal2 && !(txtSeal2.getText().toString().equals("") && txtSeal2.getText().toString().equals("0"))){
            Toast.makeText(this,"Please take photo of seal 2",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoSeal3 && !(txtSeal3.getText().toString().equals("") && txtSeal3.getText().toString().equals("0"))){
            Toast.makeText(this,"Please take photo of seal 3",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoSeal1 && !photoSeal2 && !photoSeal3){
            Toast.makeText(this,"Please a take photo of at least one seal",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtComents.getText() == null){
            txtComents.setText("");
        }

        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        myCargo.setUserName(((master)getApplicationContext()).getUser());

        queryDispatch q = new queryDispatch(getBaseContext());
        tblErrorMessage errorMessage =
        q.setDispatchDetails(
                myCargo.getUserName(),
                myCargo.getDocumentNr(),
                myCargo.getContainerNr(),
                myCargo.getWeatherConditions(),
                txtSeal1.getText().toString(),
                txtSeal2.getText().toString(),
                txtSeal3.getText().toString(),
                "ContainerPacked",
                txtComents.getText().toString()
        );
        Toast.makeText(this, errorMessage.getMsg(),Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, CargoDispatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    //Call takePhoto to take the bloody photo.
    //Add the result of what should happen once the picture has been taken here{
    public void afterPhotoTakenAction(){
        //will only execute if successful
        Button btn;
        switch (_pictureType){
            case "FullPackedContainerWithSeals":{
                btn = findViewById(R.id.CargoDispatchSugarBags4_btn1);
                photoFullPackedContainerWithSeals=true;
                break;
            }
            case "YardClerk":{
                btn = findViewById(R.id.CargoDispatchSugarBags4_btn2);
                photoYardClerk=true;
                break;
            }
            case "Seal1":{
                btn = findViewById(R.id.CargoDispatchSugarBags4_btnSeal1);
                photoSeal1=true;
                break;
            }
            case "Seal2":{
                btn = findViewById(R.id.CargoDispatchSugarBags4_btnSeal2);
                photoSeal2=true;
                break;
            }
            case "Seal3":{
                btn = findViewById(R.id.CargoDispatchSugarBags4_btnSeal3);
                photoSeal3=true;
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
                String ContainerNr = myCargo.getContainerNr();
                String UserName = ((master)getApplicationContext()).getUser();
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
