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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CargoDispatch1 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    private boolean photoFullFrame;
    private boolean photoContainerNumber;
    private boolean photoWeightReading;

    Button btnContainer;
    EditText txtWeight;
    TextView txtTareWeight;
    EditText txtSlingCount;
    LinearLayout mineralLinearLayout;
    LinearLayout sugarbagsLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_dispatch1);


        photoContainerNumber=false;
        photoFullFrame=false;
        photoWeightReading=false;

        txtWeight = findViewById(+R.id.CargoDispatch1_txtWeight);
        txtSlingCount = findViewById(+R.id.CargoDispatch1_txtSlings);
        mineralLinearLayout = findViewById(+R.id.CargoDispatch1_mineralLinearLayout);
        sugarbagsLinearLayout = findViewById(+R.id.CargoDispatch1_sugarbagLinearLayout);
        txtTareWeight = findViewById(+R.id.CargoDispatch1_txtTareWeight);
        btnContainer = findViewById(+R.id.CargoDispatch1_btn2);

        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        if (myCargo.getGateInType().equals("MineralLooseBulk")){
            mineralLinearLayout.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.INVISIBLE);
            txtTareWeight.setText(new queryDispatch(getBaseContext()).getContainerTareWeight(myCargo.getContainerNr()));
        }else{
            mineralLinearLayout.setVisibility(View.GONE);
        }

        sugarbagsLinearLayout.setVisibility(View.GONE);
        if (myCargo.getGateInType().equals("SugarBags")){
            sugarbagsLinearLayout.setVisibility(View.VISIBLE);

        }else{
            sugarbagsLinearLayout.setVisibility(View.GONE);
        }
        checkPicturesTaken();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void checkPicturesTaken()
    {
        queryDispatch q = new queryDispatch(getBaseContext());
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        tblErrorMessage errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"FullFrame");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "FullFrame";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"ContainerDoorNr");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "ContainerDoorNr";
            afterPhotoTakenAction();
        }
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"WeightReading");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "WeightReading";
            afterPhotoTakenAction();
        }
    }

    public void btnBack_Clicked(View v){
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        if (myCargo.getGateInType().equals("SugarBags") || myCargo.getGateInType().equals("MineralBags")){
            Intent intent = new Intent(this, CargoDispatchWeatherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(this, CargoDispatchSugarBags3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void btnNext_Clicked(View v){
        if (!photoFullFrame){
            Toast.makeText(this,"Please take photo of the full frame",Toast.LENGTH_SHORT).show();
            return;
        }
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        if (!myCargo.getGateInType().equals("MineralLooseBulk")){
            if (!photoContainerNumber){
                Toast.makeText(this,"Please take photo of the container door with number",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (myCargo.getGateInType().equals("MineralLooseBulk")){
            if (!photoWeightReading){
                Toast.makeText(this,"Please take photo of the weight reading",Toast.LENGTH_SHORT).show();
                return;
            }
            if (txtWeight.getText() == null){
                Toast.makeText(this,"Please provide weight reading",Toast.LENGTH_SHORT).show();
                return;
            }
            if (txtWeight.getText().toString().equals("")){
                Toast.makeText(this,"Please provide weight reading",Toast.LENGTH_SHORT).show();
                return;
            }
            double value = 0;
            try{
                value = Double.parseDouble(txtWeight.getText().toString());
                if (value <= 0){
                    Toast.makeText(this,"Please provide valid weight reading",Toast.LENGTH_SHORT).show();
                    return;
                }

            }catch(Exception ex){
                Toast.makeText(this,"Please provide valid weight reading",Toast.LENGTH_SHORT).show();
                return;
            }
            queryDispatch q = new queryDispatch(getApplicationContext());
            String username = ((master)getApplicationContext()).getUser();
            String documentNr = myCargo.getDocumentNr();
            String containerNr = myCargo.getContainerNr();
            double tareWeight = 0;
            try{
                tareWeight = Double.parseDouble( txtTareWeight.getText().toString());
            }
            catch (Exception e){
                Toast.makeText(this,"Please provide valid tare weight reading",Toast.LENGTH_SHORT).show();
                return;
            }
            value -= tareWeight;

            tblErrorMessage errorMessage = q.setBags(username,documentNr,containerNr,"1", String.format("%s",value),"");
            if (errorMessage.getErrorCode() > 0){
                Toast.makeText(this, errorMessage.getMsg(), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, CargoDispatchSugarBags4.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        if (myCargo.getGateInType().equals("SugarBags")){
            int SlingNr = 1;
            int SlingLimit;
            int Halfway;
            try{
                SlingLimit = Integer.parseInt(String.format("%s",txtSlingCount.getText().toString()));
            }catch (Exception e)
            {
                Toast.makeText(this, "Invalid number of slings",Toast.LENGTH_LONG).show();
                return;
            }

            if (SlingLimit <= 0){
                Toast.makeText(this, "Invalid number of slings",Toast.LENGTH_LONG).show();
                return;
            }
            Halfway = SlingLimit / 2;

            List<String> values = new ArrayList<>();
            values.add(String.format("%s",SlingNr));
            values.add(String.format("%s",SlingLimit));
            values.add(String.format("%s",Halfway));

            ((master)getApplicationContext()).set_value2(values);

            Intent intent = new Intent(this, CargoDispatchSugarBags2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        if (myCargo.getGateInType().equals("MineralBags")){
            Intent intent = new Intent(this, CargoDispatchMineralBags2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }

    public void btn_Clicked(View v){
        String buttonTag = v.getTag().toString();
        switch (buttonTag){
            case "btn1":{
                _pictureType = "FullFrame";
                break;
            }
            case "btn2":{
                _pictureType = "ContainerDoorNr";
                break;
            }
            case "btn3":{
                _pictureType = "WeightReading";
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
            case "FullFrame":{
                btn = findViewById(R.id.CargoDispatch1_btn1);
                photoFullFrame=true;
                break;
            }
            case "ContainerDoorNr":{
                btn = findViewById(R.id.CargoDispatch1_btn2);
                photoContainerNumber=true;
                break;
            }
            case "WeightReading":{
                btn = findViewById(R.id.CargoDispatch1_btn3);
                photoWeightReading=true;
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
