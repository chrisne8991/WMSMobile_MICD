package za.co.winfreight.wmsmobile_micd;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.InputStream;

public class SQLConSettings {
    private String _server;
    private String _port;
    private String _instance;
    private String _database;
    private String _user;
    private String _password;
    private Context _context;

    public String GetServer(){return _server;}
    public String GetPort(){return _port;}
    public String GetInstance(){return _instance;}
    public String GetDatabase(){return _database;}
    public String GetUser(){return _user;}
    public String GetPassword(){return _password;}

    public void SetServer(String server){_server = server;}
    public void SetPort(String port){_port = port;}
    public void SetInstance(String instance){_instance = instance;}
    public void SetDatabase(String database){_database = database;}
    public void SetUser(String user){_user = user;}
    public void SetPassword(String password){_password = password;}

    public SQLConSettings(Context context){
        _context = context;
        String ret = ";;;;;;";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
                int endIndex = 0;
                int beginIndex = 0;
                endIndex = ret.indexOf(";");
                _server = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _port = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _instance = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _database = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _user = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _password= ret.substring(beginIndex,endIndex);
            }
        }
        catch (FileNotFoundException e) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(ret);
                outputStreamWriter.close();
            }
            catch (IOException ep) {
                Log.e("Exception", "File write failed: " + ep.toString());
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
    public void SaveSettings(){
        String ret = String.format("%s;%s;%s;%s;%s;%s;",
                _server,
                _port,
                _instance,
                _database,
                _user,
                _password
        );
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(_context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(ret);
            outputStreamWriter.close();
        }
        catch (IOException ep) {
            Log.e("Exception", "File write failed: " + ep.toString());
        }
    }
    public String GetURL()
    {
        String ret;

        try {
            InputStream inputStream = _context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
                int endIndex = 0;
                int beginIndex = 0;
                endIndex = ret.indexOf(";");
                _server = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _port = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _instance = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _database = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _user = ret.substring(beginIndex,endIndex);
                beginIndex = endIndex;
                beginIndex++;
                endIndex = ret.indexOf(";",beginIndex);
                _password= ret.substring(beginIndex,endIndex);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File does not exist: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        String ConnURL = "jdbc:jtds:sqlserver://";
        ConnURL += _server;
        if (!_port.trim().equals("")){
            ConnURL += ":" + _port;
        }
        ConnURL += "/" + _database + ";";
        if (!_instance.trim().equals("")){
            ConnURL += "instance=" + _instance + ";";
        }
        ConnURL += String.format("user=%s;password=%s;", _user, _password);

        return ConnURL;
    }

}
