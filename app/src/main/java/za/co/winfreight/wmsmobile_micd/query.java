package za.co.winfreight.wmsmobile_micd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class query {
    private Context _context;
    query(Context context){_context = context;}
    void CreateAdapter(Spinner spinner, List<String> contents){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(_context, android.R.layout.simple_spinner_item, contents);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    tblErrorMessage checkUserAccess(String user, String password){
        String sql = String.format("EXEC dbo.spMobileCheckUserAccess '%s','%s'",user,password);
        return getErrorMessage(sql);
    }
    List<String> getWarehouses(){
        String sql = "EXEC dbo.spMobileGetWarehouses";
        return getStrings(sql);
    }
    List<String> getBinLocations(
            String warehouse
    ){
        String sql = String.format("EXEC dbo.spMobileGetBinLocations '%s'",warehouse);
        return getStrings(sql);
    }
    void logMessage(master Master, String logMessage){
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        Connection connection = sqlCon.Connect();
        try{

            String sql = String.format("EXEC dbo.spMobileLogMessage '%s',%s','%s'", Master.getUser(),Master.getWarehouse(),logMessage);
            statement = connection.createStatement();
            statement.execute(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
    }

    String getShippingLine(
            String itemNr
    )
    {
        String sql = String.format("exec mobile.spGetShippingLine '%s'",itemNr);
        return getString(sql);
    }
    List<String> getGateInDocuments(
            String Warehouse,
            String GateInType
    ){
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        ResultSet resultSet = null;
        List<String> ret = new ArrayList<>();
        Connection connection = sqlCon.Connect();
        try{

            String sql = String.format("EXEC dbo.spMobileGetGateInDocuments '%s','%s'",Warehouse,GateInType);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String item = resultSet.getString(1);
                ret.add(item);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception ignore) {}
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
        return ret;
    }

    tblErrorMessage getGateInDocumentInfo(
            String Warehouse,
            String DocumentNr
    ){
        tblErrorMessage values = new tblErrorMessage();
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = sqlCon.Connect();
        try{

            String sql = String.format("EXEC dbo.spMobileGetCargoReceiptInfo  '%s','%s'",
                    Warehouse,
                    DocumentNr);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                values = new tblErrorMessage (
                        resultSet.getInt(1),
                        resultSet.getString(2));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception ignore) {}
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
        return values;
    }
    tblErrorMessage UploadGateInPicture(
            String DocumentNr,
            String UserName,
            String PictureType,
            byte[] Picture
    ){
        int pictureId = 0;
        tblErrorMessage values = new tblErrorMessage();
        SQLCon sqlCon = new SQLCon(_context);
        PreparedStatement statement = null ;
        Statement otherStatement;
        ResultSet resultSet = null;
        byte[] newPicture = resizeBitmap(Picture);
        Connection connection = sqlCon.Connect();
        try{

            otherStatement = connection.createStatement();
            String sql = String.format("DELETE FROM GateInPictures WHERE DocumentNr = '%s' AND PictureType = '%s'", DocumentNr, PictureType);
            otherStatement.execute(sql);

            sql = ("INSERT INTO GateInPictures(DocumentNr, Username, PictureType, Picture) VALUES (?, ?, ?, ?) ");
            statement = connection.prepareStatement(sql);
            statement.setString(1, DocumentNr);
            statement.setString(2, UserName);
            statement.setString(3, PictureType);
            statement.setBytes(4, newPicture);
            statement.executeUpdate();

            sql = String.format("SELECT TOP(1) CONVERT(varchar(10),Id) " +
                    "FROM GateInPictures " +
                    "WHERE DocumentNr = '%s' " +
                    "AND PictureType = '%s' " +
                    "AND UserName = '%s' " +
                    "ORDER BY DateCreated DESC", DocumentNr, PictureType, UserName);
            String result = getString(sql);
            try{
                pictureId = Integer.parseInt(result);
            }catch (Exception e){
                return new tblErrorMessage(1, "Picture did not upload");
            }
        }
        catch (Exception e){
            return new tblErrorMessage(1, e.getMessage());
        }
        finally {
            if (statement != null) try { statement.close(); } catch(Exception e) {values =  new tblErrorMessage(1, e.getMessage());}
            if (connection != null) try { connection.close(); } catch(Exception e) {values =  new tblErrorMessage(1, e.getMessage());}
        }
        values = new tblErrorMessage (0,String.format("Picture uploaded (%s)",pictureId));
        return values;
    }
    tblErrorMessage setGateInSugarBags(
            String UserName,
            String DocumentNr,
            String BagsCount,
            String SlingWeight,
            String SlingNr
    ){
        String sql = String.format("EXEC dbo.spMobileSetGateInSugarBags  '%s','%s','%s','%s','%s'",
                UserName,
                DocumentNr,
                BagsCount,
                SlingWeight,
                SlingNr);
        return getErrorMessage(sql);
    }
    tblErrorMessage updateGateInSugarBags(
            String UserName,
            String DocumentNr,
            String Type,
            String BagsCount
    ){
        String sql = String.format("EXEC mobile.spUpdateGateInSugarBags '%s','%s','%s','%s'",
                UserName,
                DocumentNr,
                Type,
                BagsCount);
        return getErrorMessage(sql);
    }

    tblErrorMessage ScanGateInSugarBagsRandomBatch(
            String DocumentNr,
            String BatchNo,
            String DateCreated
    ){
        String sql = String.format("EXEC dbo.spMobileScanGateInSugarBagsRandomBatch  '%s','%s','%s'",
                DocumentNr,
                BatchNo,
                DateCreated);

        return getErrorMessage(sql);
    }
    tblErrorMessage setGateInDetails(
            String UserName,
            String DocumentNr,
            String Warehouse,
            String WeatherConditions,
            String EndWeatherConditions,
            String BayLocation,
            String MissingBagCount,
            String GateInType,
            String IsTarpDamaged,
            String IsOperationalDamage,
            String OperationalDamagedSlingNr,
            String OperationalDamagedQty,
            String OperationalDamagedComments
    ){
        tblErrorMessage values = new tblErrorMessage();
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = sqlCon.Connect();
        try{

            String sql = String.format("EXEC dbo.spMobileGateInCapture  '%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'",
                    UserName,
                    DocumentNr,
                    Warehouse,
                    WeatherConditions,
                    EndWeatherConditions,
                    BayLocation,
                    MissingBagCount,
                    GateInType,
                    IsTarpDamaged,
                    IsOperationalDamage,
                    OperationalDamagedSlingNr,
                    OperationalDamagedQty,
                    OperationalDamagedComments);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                values = new tblErrorMessage (
                        resultSet.getInt(1),
                        resultSet.getString(2));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception ignore) {}
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
        return values;
    }
    tblErrorMessage setGateInDetailsFinished(
            String UserName,
            String DocumentNr,
            String Warehouse,
            String Comments
    ){
        tblErrorMessage values = new tblErrorMessage();
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = sqlCon.Connect();
        try{

            String sql = String.format("EXEC dbo.spMobileSetGateInDetailsFinished '%s','%s','%s','%s'",
                    UserName,
                    DocumentNr,
                    Warehouse,
                    Comments);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                values = new tblErrorMessage (
                        resultSet.getInt(1),
                        resultSet.getString(2));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception ignore) {}
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
        return values;

    }
    CargoGrnClass getGateInDetails(
            String DocumentNr,
            String Warehouse
    ){
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = sqlCon.Connect();
        CargoGrnClass value = new CargoGrnClass();
        try{

            String sql = String.format("" +
                            "SELECT " +
                            "[DocumentNr]" +
                            ",[Username]" +
                            ",[Warehouse]" +
                            ",[WeatherConditions]" +
                            ",[EndWeatherConditions]" +
                            ",[BayLocation]" +
                            ",[MissingBagCount]" +
                            ",[GateInType]" +
                            ",[IsTarpDamaged]" +
                            ",[IsOperationalDamage]" +
                            ",[OperationalDamagedSlingNr]" +
                            ",[OperationalDamagedQty]" +
                            ",[OperationalDamagedComments]" +
                            "FROM " +
                            "[dbo].[GateInDetails]" +
                            "WHERE" +
                            "[DocumentNr] = '%s' AND [Warehouse] = '%s'",
                    DocumentNr,
                    Warehouse);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                value.setDocumentNr(resultSet.getString(1));
                value.setUserName(resultSet.getString(2));
                value.setWarehouse(resultSet.getString(3));
                value.setWeatherConditions(resultSet.getString(4));
                value.setEndWeatherConditions(resultSet.getString(5));
                value.setBayLocation(resultSet.getString(6));
                try{
                    value.setMissingBagCount(Integer.parseInt(resultSet.getString(7)));
                }catch (Exception ex){
                    value.setMissingBagCount(0);
                }
                value.setGateInType(resultSet.getString(8));
                try{
                    int isit = Integer.parseInt(resultSet.getString(9));
                    if (isit == 1) {
                        value.setTarpDamaged(true);
                    }else{
                        value.setTarpDamaged(false);
                    }
                }catch (Exception ex) {
                    value.setTarpDamaged(false);
                }
                try{
                    int isit = Integer.parseInt(resultSet.getString(10));
                    if (isit == 1) {
                        value.setOperationalDamage(true);
                    }else{
                        value.setOperationalDamage(false);
                    }
                }catch (Exception ex) {
                    value.setOperationalDamage(false);
                }
                value.setOperationalDamagedSlingNr(resultSet.getString(11));
                value.setOperationalDamagedQty(resultSet.getString(12));
                value.setOperationalDamagedComments(resultSet.getString(13));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception ignore) {}
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
        return value;
    }
    void ReleaseMineralLooseBulkDocument(
            String documentNr
    ){
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        Connection connection = sqlCon.Connect();
        try{

            String sql = String.format("UPDATE _gad SET _gad.MineralLooseBulkReleased = 1 FROM GateInDetails _gad WHERE _gad.DocumentNr = '%s'", documentNr);
            statement = connection.createStatement();
            statement.execute(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
    }
    void LockMineralLooseBulkDocument(
            String documentNr
    ){
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        Connection connection = sqlCon.Connect();
        try{

            String sql = String.format("UPDATE _gad SET _gad.MineralLooseBulkReleased = 0 FROM GateInDetails _gad WHERE _gad.DocumentNr = '%s'", documentNr);
            statement = connection.createStatement();
            statement.execute(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
    }

    public byte[] resizeBitmap(byte[] picture) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeByteArray(picture,0,picture.length);
        Bitmap bitmap1;
        if (bitmap.getWidth() > bitmap.getHeight()){
            bitmap1 = Bitmap.createScaledBitmap(bitmap,800,600,true);
        }else{
            bitmap1 = Bitmap.createScaledBitmap(bitmap,600,800,true);
        }
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG,90, blob);
        return blob.toByteArray();
    }

    public void Exec(
            String sql
    ){
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        Connection connection = sqlCon.Connect();
        try{

            statement = connection.createStatement();
            statement.execute(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
    }
    public List<String> getStrings(
            String sql
    ){
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        ResultSet resultSet = null;
        List<String> ret = new ArrayList<>();
        Connection connection = sqlCon.Connect();
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String item = resultSet.getString(1);
                ret.add(item);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception ignore) {}
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
        return ret;
    }
    public String getString(String sql){
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        ResultSet resultSet = null;
        String ret = "";
        Connection connection = sqlCon.Connect();
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                ret = resultSet.getString(1);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception ignore) {}
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
        return ret;

    }
    public tblErrorMessage getErrorMessage(
            String sql
    )
    {
        tblErrorMessage values = new tblErrorMessage();
        SQLCon sqlCon = new SQLCon(_context);
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = sqlCon.Connect();
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                values = new tblErrorMessage (
                        resultSet.getInt(1),
                        resultSet.getString(2));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception ignore) {}
            if (statement != null) try { statement.close(); } catch(Exception ignore) {}
            if (connection != null) try { connection.close(); } catch(Exception ignore) {}
        }
        return values;
    }
    void RemoveLastGateInSugarBags(String Username, String DocumentNr){
        String sql = String.format("EXEC dbo.spMobileRemoveGateInSugarBags '%s','%s'",Username, DocumentNr);
        Exec(sql);
    }
    String AllotActivityAccess(String UserName, String FormName){
        String sql = String.format("EXEC mobile.spAllowActivityAccess '%s','%s'", UserName, FormName);
        return getString(sql);
    }
    tblErrorMessage CheckGateInProceed(String DocumentNr, String UserName){
        String sql = String.format("EXEC mobile.spCheckGateInProceed '%s','%s'", DocumentNr, UserName);
        return getErrorMessage(sql);
    }
    List<String> getGrnAmountCaptures(String DocumentNr){
        String sql = String.format("EXEC mobile.spGetGrnAmountsCaptures '%s'",DocumentNr);
        return getStrings(sql);
    }
    void ResetGateInSugarBags(String DocumentNr){
        String sql = String.format("DELETE FROM GateInSugarBagSlings where DocumentNr = '%s'",DocumentNr);
        Exec(sql);
    }
    void SetGateInSlingCount(String DocumentNr, String SlingCount){
        String sql = String.format("UPDATE TOP(1) GateInDocuments SET SlingCount = '%s' WHERE DocumentNr = '%s'", DocumentNr, SlingCount);
        Exec(sql);
    }
    int GetGateInSlingCount(String DocumentNr){
        String sql = String.format("SELECT TOP(1) SlingCount FROM GateInDocuments WHERE DocumentNr = '%s'", DocumentNr);
        String sqlRes = getString(sql);
        int value;
        try{
            value = Integer.parseInt(sqlRes);
        }catch (Exception e){
            value = 0;
        }
        return value;
    }
    List<String> getBagSlingInfo(String UserName, String DocumentNr, String SlingNr){
        String sql = String.format("EXEC mobile.spGetBagSlingInfo '%s','%s','%s'",UserName, DocumentNr, SlingNr);
        return getStrings(sql);
    }
    tblErrorMessage CheckGateInPictureTaken(
            String DocumentNr,
            String PictureType
    )
    {
        String sql = String.format("EXEC mobile.spCheckGateInPictureTaken '%s','%s'",DocumentNr, PictureType);
        return getErrorMessage(sql);
    }
}
