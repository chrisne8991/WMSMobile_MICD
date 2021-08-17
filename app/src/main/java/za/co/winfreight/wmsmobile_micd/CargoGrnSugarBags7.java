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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CargoGrnSugarBags7 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}
    boolean isDamaged;
    boolean photoTaken;
    Button btn1;
    TextView txt;
    int Count;
    TextView txtBags;
    LinearLayout llHowMany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn_sugar_bags7);
        txt = findViewById(R.id.CargoGrnSugarBags7_lblTakePhotoPrompt);
        btn1 = findViewById(R.id.CargoGrnSugarBags7_btn1);
        llHowMany = findViewById(R.id.CargoGrnSugarBags7_llHowMany);
        txtBags = findViewById(R.id.CargoGrnSugarBags7_txtNumberOfBags);
        llHowMany.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);
        btn1.setVisibility(View.INVISIBLE);

        CargoGrnClass grn = ((master)getApplicationContext()).get_cargoGoodsReceipt();
        if (grn.getGateInType().equals("SugarBags")){
            List<String> values = ((master)getApplicationContext()).get_value2();
            Count = Integer.parseInt(values.get(1));
        }else{
            Count = 1;
        }
        photoTaken = false;
        checkPicturesTaken();
    }
    private void checkPicturesTaken()
    {
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.CargoGrnSugarBags7_rbYes:{
                if (checked){
                    isDamaged=true;
                    txt.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    llHowMany.setVisibility(View.VISIBLE);
                }
                break;

            }
            case R.id.CargoGrnSugarBags7_rbNo:{
                if (checked) {
                    isDamaged = false;
                    txt.setVisibility(View.INVISIBLE);
                    btn1.setVisibility(View.INVISIBLE);
                    llHowMany.setVisibility(View.INVISIBLE);
                }
                break;
            }
        }
    }

    public void CargoGrnSugarBags7_btn_Clicked(View v){
        photoTaken = true;
        _pictureType = String.format("WetBag%s",Count);
        takePhoto(_pictureType);
    }

    public void CargoGrnSugarBags7_btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoGrnSugarBags6.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoGrnSugarBags7_btnNext_Clicked(View v){
        if (isDamaged){
            if (!photoTaken){
                Toast.makeText(this,"Take photos of wet bags", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txtBags.getText() == null){
                Toast.makeText(this,"Please provide number of bags", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txtBags.getText().toString().equals("")){
                Toast.makeText(this,"Please provide number of bags", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txtBags.getText().toString().equals("0")){
                Toast.makeText(this,"Invalid number of bags", Toast.LENGTH_SHORT).show();
                return;
            }
            CargoGrnClass myCargo = ((master)getApplicationContext()).get_cargoGoodsReceipt();
            query q = new query(getBaseContext());
            q.updateGateInSugarBags(((master)getApplicationContext()).getUser(), myCargo.getDocumentNr(),"Wet", txtBags.getText().toString());
        }
        Intent intent = new Intent(this, CargoGrnSugarBags8.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Call takePhoto to take the bloody photo.
    //Add the result of what should happen once the picture has been taken here{
    public void afterPhotoTakenAction(){
        //will only execute if successful
        //Toast.makeText(this,String.format("Photo Nr %s has been taken",Count),Toast.LENGTH_SHORT).show();
        btn1.setBackground(getDrawable(R.drawable.rounded_button_press));
    }
    public void afterPhotoFailedAction(){
        //will only execute if successful
        Count--;
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
            return;
        }
        afterPhotoFailedAction();
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
