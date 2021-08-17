package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void main_btnLogin_Clicked(View v){
        SQLCon sqlCon = new SQLCon(getBaseContext());
        String conResult = sqlCon.Test();
        if (!conResult.trim().equals("Success!")){
            TextView txtMsg = findViewById(+R.id.main_txtMsg);
            txtMsg.setText(R.string.main_error_no_connection_settings);
            return;
        }
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void main_btnExit_Clicked(View v){
        finish();
        System.exit(0);
    }

    public void main_btnSetup_Clicked(View v){
        Intent intent = new Intent(this, SetupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}
