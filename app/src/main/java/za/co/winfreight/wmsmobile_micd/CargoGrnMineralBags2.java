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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CargoGrnMineralBags2 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}
    boolean isDamaged;
    boolean tarpPhotoDamage1;
    boolean tarpPhotoDamage2;
    boolean trailerACargoTop;
    boolean trailerBCargoTop;
    Button btn1;
    Button btn2;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_mineral_bags2);
        txt = findViewById(R.id.CargoGrnMineralBags2_lblTakePhotoPrompt);
        btn1 = findViewById(R.id.CargoGrnMineralBags2_btn1);
        btn2 = findViewById(R.id.CargoGrnMineralBags2_btn2);

        txt.setVisibility(View.INVISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);

        checkPicturesTaken();

    }
    private void checkPicturesTaken()
    {
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.CargoGrnMineralBags2_rbYes:{
                if (checked){
                    isDamaged=true;
                    txt.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.VISIBLE);

                }
                break;

            }
            case R.id.CargoGrnMineralBags2_rbNo:{
                if (checked) {
                    isDamaged = false;
                    txt.setVisibility(View.INVISIBLE);
                    btn1.setVisibility(View.INVISIBLE);
                    btn2.setVisibility(View.INVISIBLE);
                }
                break;
            }
        }
    }

    public void CargoGrnMineralBags2_btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "CargoGrnMineralBags2_btn1":{
                _pictureType = "TarpPhotoDamage1";
                break;
            }
            case "CargoGrnMineralBags2_btn2":{
                _pictureType = "TarpPhotoDamage2";
                break;
            }
            case "CargoGrnMineralBags2_btn3":{
                _pictureType = "TrailerACargoTop";
                break;
            }
            case "CargoGrnMineralBags2_btn4":{
                _pictureType = "TrailerBCargoTop";
                break;
            }
            default:{
                return;
            }
        }
        takePhoto(_pictureType);

    }

    public void CargoGrnMineralBags2_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnMineralBags1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnMineralBags2_btnNext_Clicked(View v){
        if (isDamaged){
            if (!tarpPhotoDamage1 && !tarpPhotoDamage2){
                Toast.makeText(this,"Please supply two images of the damage",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!trailerACargoTop){
            Toast.makeText(this,"Please supply images of trailer A top",Toast.LENGTH_SHORT).show();
            return;

        }
        if (!trailerBCargoTop){
            Toast.makeText(this,"Please supply images of trailer B top",Toast.LENGTH_SHORT).show();
            return;

        }
        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        myCargo.setTarpDamaged(isDamaged);
        ((master)getApplicationContext()).set_cargoGoodsReceipt(myCargo);

        query q = new query(getApplicationContext());
        String isTarpDamagedString = "0";
        if (myCargo.getIsTarpDamaged()) isTarpDamagedString = "1";
        tblErrorMessage errorMessage = q.setGateInDetails(
                myCargo.getUserName(),
                myCargo.getDocumentNr(),
                myCargo.getWarehouse(),
                myCargo.getWeatherConditions(),
                "",
                myCargo.getBayLocation(),
                "",
                myCargo.getGateInType(),
                isTarpDamagedString,
                "0",
                "",
                "",
                "");

        Intent intent = new Intent(this, CargoGrnMineralBags3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Call takePhoto to take the bloody photo.
    //Add the result of what should happen once the picture has been taken here{
    public void afterPhotoTakenAction(){
        //will only execute if successful
        Button btn;
        switch (_pictureType){
            case "TarpPhotoDamage1":{
                btn = findViewById(R.id.CargoGrnMineralBags2_btn1);
                tarpPhotoDamage1=true;
                break;
            }
            case "TarpPhotoDamage2":{
                btn = findViewById(R.id.CargoGrnMineralBags2_btn2);
                tarpPhotoDamage2=true;
                break;
            }
            case "TrailerACargoTop":{
                btn = findViewById(R.id.CargoGrnMineralBags2_btn3);
                trailerACargoTop=true;
                break;
            }
            case "TrailerBCargoTop":{
                btn = findViewById(R.id.CargoGrnMineralBags2_btn4);
                trailerBCargoTop=true;
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
