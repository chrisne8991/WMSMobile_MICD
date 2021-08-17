package za.co.winfreight.wmsmobile_micd;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLCon {
    private static String classs = "net.sourceforge.jtds.jdbc.Driver";
    private static final String LOG = "DEBUG";
    private Context _context;

    public SQLCon(Context context){
        _context = context;
    }

    public Connection Connect() {
        Connection conn = null;
        SQLConSettings _settings = new SQLConSettings(_context);
        String ConnURL = _settings.GetURL();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(classs);
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException e) {
            Log.d(LOG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.d(LOG, e.getMessage());
        }
        return conn;
    }

    public String Test(){
        SQLConSettings _settings = new SQLConSettings(_context);
        String ConnURL = _settings.GetURL();
        String ReturnMessage = "Success!";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(classs);
            Connection conn= DriverManager.getConnection(ConnURL);
        } catch (Exception e) {
            ReturnMessage = e.getMessage();
        }
        return ReturnMessage;
    }

}
