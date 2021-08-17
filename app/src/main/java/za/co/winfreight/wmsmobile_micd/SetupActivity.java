package za.co.winfreight.wmsmobile_micd;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    @Override
    protected void onResume(){
        super.onResume();
        EditText txtServer = findViewById(+R.id.setup_txtServer);
        EditText txtPort = findViewById(+R.id.setup_txtPortNumber);
        EditText txtInstance = findViewById(+R.id.setup_txtInstance);
        EditText txtDatabase = findViewById(+R.id.setup_txtDatabase);
        EditText txtUser = findViewById(+R.id.setup_txtUserName);
        EditText txtPassword = findViewById(+R.id.setup_txtPassword);

        SQLConSettings _constring = new SQLConSettings(getBaseContext());
        String _server = _constring.GetServer();
        String _port = _constring.GetPort();
        String _instance = _constring.GetInstance();
        String _database =  _constring.GetDatabase();
        String _user = _constring.GetUser();
        String _password = _constring.GetPassword();

        if (_server == null) _server = "";
        if (_port == null) _port = "";
        if (_instance == null) _instance = "";
        if (_database == null) _database = "";
        if (_user == null) _user = "";
        if (_password == null) _password = "";

        if (_server.equals("") && _port.equals("") && _instance.equals("") && _database.equals("") && _user.equals("") && _password.equals("")){
            _server = "hosted5.iconnix.co.za";
            _port = "20117";
            _database = "wms_grindrod_micd";
            _user = "Grindrod";
            _password = "p@s$0n3";
        }

        txtServer.setText(_server);
        txtPort.setText(_port);
        txtInstance.setText(_instance);
        txtDatabase.setText(_database);
        txtUser.setText(_user);
        txtPassword.setText(_password);
    }

    public void setup_btnTest_Clicked(View v){
        setup_btnSave_Clicked(v);

        String msg = new SQLCon(getBaseContext()).Test();
        TextView editText = findViewById(+R.id.setup_lblError);
        editText.setText(msg);
    }

    public void setup_btnSave_Clicked(View v){
        EditText txtServer = findViewById(+R.id.setup_txtServer);
        EditText txtPort = findViewById(+R.id.setup_txtPortNumber);
        EditText txtInstance = findViewById(+R.id.setup_txtInstance);
        EditText txtDatabase = findViewById(+R.id.setup_txtDatabase);
        EditText txtUser = findViewById(+R.id.setup_txtUserName);
        EditText txtPassword = findViewById(+R.id.setup_txtPassword);
        TextView txtError = findViewById(+R.id.setup_lblError);

        String _server = txtServer.getText().toString().trim();
        String _port = txtPort.getText().toString().trim();
        String _instance = txtInstance.getText().toString().trim();
        String _database = txtDatabase.getText().toString().trim();
        String _user = txtUser.getText().toString().trim();
        String _password = txtPassword.getText().toString().trim();

        if (_server.trim().equals("")){
            txtError.setText(+R.string.setup_error_server_string);
            txtServer.requestFocus();
            return;
        }
        if(_database.trim().equals("")){
            txtError.setText(+R.string.setup_error_database_string);
            txtDatabase.requestFocus();
            return;
        }
        if(_user.trim().equals("")){
            txtError.setText(+R.string.setup_error_username_string);
            txtUser.requestFocus();
            return;
        }
        if(_password.trim().equals("")){
            txtError.setText(+R.string.setup_error_password_string);
            txtPassword.requestFocus();
            return;
        }

        SQLConSettings _constring = new SQLConSettings(getBaseContext());
        _constring.SetServer(_server);
        _constring.SetPort(_port);
        _constring.SetDatabase(_database);
        _constring.SetInstance(_instance);
        _constring.SetUser(_user);
        _constring.SetPassword(_password);
        _constring.SaveSettings();

        txtError.setText(+R.string.setup_msg_settings_saved_string);
    }

    public void setup_btnBack_Clicked(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
