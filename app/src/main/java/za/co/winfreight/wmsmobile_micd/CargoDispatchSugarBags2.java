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
import java.util.List;

public class CargoDispatchSugarBags2 extends AppCompatActivity {
    //Leave the following code alone {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    String _pictureType;
    //}

    TextView lblSlingNr;
    EditText txtCount;
    EditText txtWeight;
    TextView txtTotalCount;
    TextView txtTotalWeight;
    EditText txtBatchNr;
    LinearLayout ll;
    Button btnPhoto;
    Button btnNext;
    int SlingCount;
    String ContainerNr;
    String DocumentNr;
    boolean photoHalfFullContainer;
    boolean yesClicked;
    boolean noClicked;

    int SlingNr;
    int SlingLimit;
    int HalfWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_dispatch_sugar_bags2);
        SlingCount=1;
        photoHalfFullContainer = false;
        txtBatchNr = findViewById(R.id.CargoDispatchSugarBags2_txtBatchNr);
        txtCount = findViewById(R.id.CargoDispatchSugarBags2_txtCount);
        txtWeight = findViewById(R.id.CargoDispatchSugarBags2_txtWeight);
        txtTotalCount = findViewById(R.id.CargoDispatchSugarBags2_txtTotalCount);
        txtTotalWeight = findViewById(R.id.CargoDispatchSugarBags2_txtTotalWeight);
        lblSlingNr = findViewById(R.id.CargoDispatchSugarBags2_lblBagNr);
        btnPhoto = findViewById(R.id.CargoDispatchSugarBags2_btnPhoto);
        btnNext = findViewById(R.id.CargoDispatchSugarBags2_btnNext);

        ll = findViewById(R.id.CargoDispatchSugarBags2_llConfirmSection);
        ll.setVisibility(View.INVISIBLE);

        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        DocumentNr = myCargo.getDocumentNr();
        ContainerNr = myCargo.getContainerNr();
        queryDispatch q = new queryDispatch(getApplicationContext());
        q.ClearBags(DocumentNr,ContainerNr);

        List<String> values = ((master)getApplicationContext()).get_value2();
//        values.add(String.format("%s",SlingNr));
//        values.add(String.format("%s",SlingLimit));
//        values.add(String.format("%s",Halfway));
        SlingNr = Integer.parseInt(values.get(0));
        SlingLimit = Integer.parseInt(values.get(1));
        HalfWay = Integer.parseInt(values.get(2));
        checkPicturesTaken();
    }
    private void checkPicturesTaken()
    {
        queryDispatch q = new queryDispatch(getBaseContext());
        CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
        tblErrorMessage errorMessage = new tblErrorMessage();
        errorMessage = q.CheckGateOutPictureTaken(myCargo.getDocumentNr(),myCargo.getContainerNr(),"HalfFullContainer");
        if (errorMessage.getErrorCode() == 1){
            _pictureType = "HalfFullContainer";
            afterPhotoTakenAction();
        }
    }

    public void btnNext_Clicked(View v){
        if (txtWeight.getText() == null) return;
        if (txtCount.getText() == null) return;
        if (txtCount.getText().toString().equals("0")) return;
        if (txtWeight.getText().toString().equals("0")) return;

        queryDispatch q = new queryDispatch(getApplicationContext());

        String count = txtCount.getText().toString();
        String weight = txtWeight.getText().toString();
        String user = ((master)getApplicationContext()).getUser();
        String batchNr = txtBatchNr.getText().toString();

        if (SlingCount > SlingLimit){
            if (!yesClicked && !noClicked){
                Toast.makeText(this,"Select either yes or no to proceed",Toast.LENGTH_SHORT).show();
                return;
            }
            if (yesClicked) {
                Intent intent = new Intent(this, CargoDispatchSugarBags3.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return;
            }
            //No clicked
            CargoDispatchClass myCargo = ((master)getApplicationContext()).get_cargoDispatch();
            q.setDispatchDetails(
                    myCargo.getUserName(),
                    myCargo.getDocumentNr(),
                    myCargo.getContainerNr(),
                    myCargo.getWeatherConditions(),
                    "",
                    "",
                    "",
                    "",
                    ""
            );
            q.setContinueSugarDispatchTransaction(((master)getApplicationContext()).getUser(), DocumentNr, ContainerNr);
            Intent intent = new Intent(this, CargoDispatchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        if (SlingCount == SlingLimit){
            tblErrorMessage errorMessage = q.setBags(user,DocumentNr, ContainerNr,count,weight,batchNr);
            if (errorMessage.getErrorCode() > 0){
                Toast.makeText(this, errorMessage.getMsg(), Toast.LENGTH_SHORT).show();
                return;
            }

            String value1 = q.getDocumentContainerBagWeight(DocumentNr,ContainerNr);
            String value2 = q.getDocumentContainerBagCount(DocumentNr,ContainerNr);
            txtTotalWeight.setText(String.format("%s",value1));
            txtTotalCount.setText(String.format("%s",value2));
            Toast.makeText(this,"Click next to confirm",Toast.LENGTH_SHORT).show();
            ll.setVisibility(View.VISIBLE);
            SlingCount++;
            return;
        }
        if (SlingCount >= HalfWay)
        {
            if (!photoHalfFullContainer){
                btnPhoto.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.INVISIBLE);
                return;
            }
        }

        tblErrorMessage errorMessage = q.setBags(user,DocumentNr, ContainerNr,count,weight,batchNr);
        if (errorMessage.getErrorCode() > 0){
            Toast.makeText(this, errorMessage.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        String value1 = q.getDocumentContainerBagWeight(DocumentNr,ContainerNr);
        String value2 = q.getDocumentContainerBagCount(DocumentNr,ContainerNr);
        txtTotalWeight.setText(String.format("%s",value1));
        txtTotalCount.setText(String.format("%s",value2));
        SlingCount++;
        lblSlingNr.setText(String.format("%s", SlingCount));
    }

    public void btnBack_Clicked(View v){
        Intent intent = new Intent(this, CargoDispatch1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void CargoDispatchSugarBags2_rb_Clicked(View v){
        yesClicked = false;
        noClicked = false;
        switch (v.getId()){
            case +R.id.CargoDispatchSugarBags2_rbYes: {
                yesClicked = true;
                break;
            }
            case +R.id.CargoDispatchSugarBags2_rbNo : {
                noClicked = true;
                break;
            }
        }
    }

    public void btnPhoto_Clicked(View v){
        String buttonTag = v.getTag().toString();
        if (buttonTag.equals("btn1")) {
            _pictureType = "HalfFullContainer";
        } else {
            return;
        }
        takePhoto(_pictureType);
    }
    //Call takePhoto to take the bloody photo.
    //Add the result of what should happen once the picture has been taken here{
    public void afterPhotoTakenAction(){
        //will only execute if successful
        Button btn;
        if (_pictureType.equals("HalfFullContainer")) {
            btn = findViewById(R.id.CargoDispatchSugarBags2_btnPhoto);
            photoHalfFullContainer = true;
            btnPhoto.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            lblSlingNr.setText(String.format("%s", SlingCount));
        } else {
            return;
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
                tblErrorMessage errorMessage = q.UploadDispatchPictures(DocumentNr, ContainerNr,UserName,_pictureType,inputData);
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
