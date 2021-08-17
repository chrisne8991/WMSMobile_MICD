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

public class CargoGrnMineralLooseBulk5 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    private boolean photoTrailerACargoTipped;
    private boolean photoTrailerACargoEmptyBins;
    private boolean photoTrailerBCargoTipped;
    private boolean photoTrailerBCargoEmptyBins;
    private boolean photoWhsTallyClerk;
    EditText txtComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_mineral_loose_bulk5);

        photoTrailerACargoTipped=false;
        photoTrailerACargoEmptyBins=false;
        photoTrailerBCargoTipped=false;
        photoTrailerBCargoEmptyBins=false;
        photoWhsTallyClerk=false;
        txtComment = findViewById(+R.id.CargoGrnMineralLooseBulk5_txtComments);
        checkPicturesTaken();
    }
    private void checkPicturesTaken()
    {
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void CargoGrnMineralLooseBulk5_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnMineralLooseBulk4.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnMineralLooseBulk5_btnNext_Clicked(View v){
        if (!photoTrailerACargoTipped){
            Toast.makeText(this,"Please take photo of trailer A tipped",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoTrailerACargoEmptyBins){
            Toast.makeText(this,"Please take photo of trailer A empty bins",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoTrailerBCargoTipped){
            Toast.makeText(this,"Please take photo of trailer B tipped",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoTrailerBCargoEmptyBins){
            Toast.makeText(this,"Please take photo of trailer B empty bins",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoWhsTallyClerk){
            Toast.makeText(this,"Please take photo of WHS tally clerk",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtComment.getText() == null) return;
        if (txtComment.getText().toString().equals("")) {
            Toast.makeText(this, "Please supply comments",Toast.LENGTH_SHORT).show();
            return;
        }

        CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        myCargo.setOperationalDamagedComments(txtComment.getText().toString());
        myCargo.setOperationalDamage(false);
        myCargo.setOperationalDamagedSlingNr("");
        myCargo.setOperationalDamagedQty("");

        ((master)getApplicationContext()).set_cargoGoodsReceipt(myCargo);

        query q = new query(getApplicationContext());
        String isTarpDamagedString = "0";
        String isOperationalDamageString = "0";
        try{
            if (myCargo.getIsTarpDamaged()) isTarpDamagedString = "1";
        }catch(Exception ex){
            isTarpDamagedString = "0";
        }
        try{
            if (myCargo.isOperationalDamage()) isOperationalDamageString = "1";
        }catch(Exception ex){
            isOperationalDamageString = "0";
        }

        if (myCargo.getIsTarpDamaged()) isTarpDamagedString = "1";
        q.setGateInDetails(
                myCargo.getUserName(),
                myCargo.getDocumentNr(),
                myCargo.getWarehouse(),
                myCargo.getWeatherConditions(),
                myCargo.getEndWeatherConditions(),
                myCargo.getBayLocation(),
                String.format("%s",myCargo.getMissingBagCount()),
                myCargo.getGateInType(),
                isTarpDamagedString,
                isOperationalDamageString,
                myCargo.getOperationalDamagedSlingNr(),
                myCargo.getOperationalDamagedQty(),
                myCargo.getOperationalDamagedComments());

        tblErrorMessage errorMessage = q.setGateInDetailsFinished(myCargo.getUserName(),myCargo.getDocumentNr(),myCargo.getWarehouse(),txtComment.getText().toString());
        Toast.makeText(this,errorMessage.getMsg(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, CargoGrnActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnMineralLooseBulk5_btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "CargoGrnMineralLooseBulk5_btn1":{
                _pictureType = "TrailerACargoTipped";
                break;
            }
            case "CargoGrnMineralLooseBulk5_btn2":{
                _pictureType = "TrailerACargoEmptyBins";
                break;
            }
            case "CargoGrnMineralLooseBulk5_btn3":{
                _pictureType = "TrailerBCargoTipped";
                break;
            }
            case "CargoGrnMineralLooseBulk5_btn4":{
                _pictureType = "TrailerBCargoEmptyBins";
                break;
            }
            case "CargoGrnMineralLooseBulk5_btn5":{
                _pictureType = "WhsTallyClerk";
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
            case "TrailerACargoTipped":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk5_btn1);
                photoTrailerACargoTipped=true;
                break;
            }
            case "TrailerACargoEmptyBins":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk5_btn2);
                photoTrailerACargoEmptyBins=true;
                break;
            }
            case "TrailerBCargoTipped":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk5_btn3);
                photoTrailerBCargoTipped=true;
                break;
            }
            case "TrailerBCargoEmptyBins":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk5_btn4);
                photoTrailerBCargoEmptyBins=true;
                break;
            }
            case "WhsTallyClerk":{
                btn = findViewById(R.id.CargoGrnMineralLooseBulk5_btn5);
                photoWhsTallyClerk=true;
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

