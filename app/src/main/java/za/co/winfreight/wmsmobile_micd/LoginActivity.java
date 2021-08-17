package za.co.winfreight.wmsmobile_micd;


import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    MediaPlayer noSound;
    MediaPlayer okSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        noSound = MediaPlayer.create(this, +R.raw.error_sound);
//        okSound = MediaPlayer.create(this, +R.raw.beep_sound);
    }

    public void login_btnLogin_Clicked(View v){
        EditText txtUser = findViewById(+R.id.login_txtUser);
        EditText txtPassword = findViewById(+R.id.login_txtPassword);

        String user;
        user = txtUser.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        query q = new query(getBaseContext());
        tblErrorMessage error = q.checkUserAccess(user,password);
        if (error.getErrorCode() == 1){

            Toast.makeText(this, error.getMsg(), Toast.LENGTH_SHORT).show();
//            noSound.start();
            return;
        }
//        okSound.start();
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        master tmpMaster = new master(user,""); //we allow warehouses to be selected on the next screen
        ((master)this.getApplication()).setUser(user);
        ((master)this.getApplication()).setWarehouse(user);
        q.logMessage(tmpMaster,"User logged in");

        startActivity(intent);

    }
    public void login_btnExit_Clicked(View v){
        finish();
        System.exit(0);
    }

}
