package za.co.winfreight.wmsmobile_micd;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class queryDispatch extends query {
    private Context _context;
    queryDispatch(Context context){
        super(context);
        _context = context;
    }

    void logMessage(master Master, String logMessage){
        super.logMessage(Master,logMessage);
    }

    List<String> getDocuments(
            String Warehouse,
            String GateInType
    ){
        String sql = String.format("EXEC mobile.spGetDispatchDocuments '%s','%s'",Warehouse,GateInType);
        return super.getStrings(sql);
    }

    List<String> getECLDocuments(
            String Warehouse
    ){
        String sql = String.format("EXEC mobile.spGetECLDocuments '%s'",Warehouse);
        return super.getStrings(sql);
    }

    List<String> getECLContainers(
            String Warehouse,
            String BookingReference
    ){
        String sql = String.format("EXEC mobile.spGetECLContainers '%s','%s'",Warehouse,BookingReference);
        return super.getStrings(sql);
    }
    tblErrorMessage getECLContainerInfo(
            String ContainerNr,
            String ContainerNr2
    ){
        String sql = String.format("EXEC mobile.spGetECLContainerInfo '%s','%s'", ContainerNr, ContainerNr2);
        return super.getErrorMessage(sql);
    }

    List<String> getContainers(
            String DocumentNr
    ){
        String sql = String.format("EXEC mobile.spGetDispatchDocumentContainers '%s'",DocumentNr);
        return super.getStrings(sql);
    }
    tblErrorMessage getContainerInfo(
            String ContainerNr
    ){
        String sql = String.format("EXEC mobile.spGetDispatchContainerInfo '%s'", ContainerNr);
        return super.getErrorMessage(sql);
    }
    tblErrorMessage UploadDispatchPictures(
            String DocumentNr,
            String ContainerNr,
            String UserName,
            String PictureType,
            byte[] Picture
    ){
        int pictureId = 0;
        tblErrorMessage values;
        SQLCon sqlCon = new SQLCon(_context);
        PreparedStatement statement = null ;
        Statement otherStatement;
        byte[] newPicture = super.resizeBitmap(Picture);
        Connection connection = sqlCon.Connect();
        try{

            otherStatement = connection.createStatement();
            String sql = String.format("DELETE FROM DispatchPictures WHERE DocumentNr = '%s' AND PictureType = '%s' AND ContainerNr = '%s'", DocumentNr, PictureType, ContainerNr);
            otherStatement.execute(sql);

            sql = ("INSERT INTO DispatchPictures(DocumentNr, Username, PictureType, Picture, ContainerNr) VALUES (?, ?, ?, ?, ?) ");
            statement = connection.prepareStatement(sql);
            statement.setString(1, DocumentNr);
            statement.setString(2, UserName);
            statement.setString(3, PictureType);
            statement.setBytes(4, newPicture);
            statement.setString(5, ContainerNr);
            statement.executeUpdate();

            sql = String.format("SELECT TOP(1) CONVERT(varchar(10),Id) " +
                    "FROM DispatchPictures " +
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
            values = new tblErrorMessage (0,"Picture uploaded");
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
    tblErrorMessage setBags(
            String UserName,
            String DocumentNr,
            String Container,
            String BagsCount,
            String SlingWeight,
            String BatchNr
    ){
        String sql = String.format("EXEC mobile.spSetDispatchBags  '%s','%s','%s','%s','%s','%s'",
                UserName,
                DocumentNr,
                Container,
                BagsCount,
                SlingWeight,
                BatchNr);
        return super.getErrorMessage(sql);
    }
    void ClearBags(
        String DocumentNr,
        String ContainerNr
    )
    {
        String sql = String.format("DELETE FROM DispatchBagSlings WHERE DocumentNr = '%s' AND Container = '%s'", DocumentNr,ContainerNr);
        super.Exec(sql);
    }

    tblErrorMessage setDispatchDetails(
            String UserName,
            String DocumentNr,
            String ContainerNr,
            String WeatherConditions,
            String Seal1,
            String Seal2,
            String Seal3,
            String Complete,
            String Comments
    )
    {
        String sql = String.format("EXEC mobile.spSetDispatchDetails '%s','%s','%s','%s','%s','%s','%s','%s','%s'",UserName,DocumentNr,ContainerNr,WeatherConditions,Seal1,Seal2,Seal3,Complete,Comments);
        return getErrorMessage(sql);
    }
    String getDispatchWeatherConditions(
            String UserName,
            String DocumentNr,
            String ContainerNr
    )
    {
        String sql = String.format("SELECT TOP(1) WeatherConditions FROM PickingCargoStockDetails WHERE UserName = '%s' AND DocumentNr = '%s' AND ContainerNr = '%s'", UserName, DocumentNr, ContainerNr);
        return getString(sql);
    }


    String getDocumentContainerBagCount(
            String DocumentNr,
            String ContainerNr
    )
    {
        String sql = String.format("SELECT CONVERT(varchar(250),SUM(BagsCount)) AS [Count] FROM DispatchBagSlings WHERE DocumentNr = '%s' AND Container = '%s'",DocumentNr,ContainerNr);
        List<String> items = new ArrayList(super.getStrings(sql));
        return String.format("%s",items.get(0));
    }
    String getDocumentContainerBagWeight(
            String DocumentNr,
            String ContainerNr
    )
    {
        String sql = String.format("SELECT CONVERT(varchar(250),SUM(SlingWeight)) AS [Weight] FROM DispatchBagSlings WHERE DocumentNr = '%s' AND Container = '%s'",DocumentNr,ContainerNr);
        ArrayList items = new ArrayList(super.getStrings(sql));
        return String.format("%s",items.get(0));
    }
    tblErrorMessage UploadECLPicture(
            String DocumentNr,
            String ContainerNr,
            String UserName,
            String PictureType,
            byte[] Picture
    ){
        tblErrorMessage values = new tblErrorMessage();
        SQLCon sqlCon = new SQLCon(_context);
        PreparedStatement statement = null ;
        Statement otherStatement;
        ResultSet resultSet = null;
        byte[] newPicture = resizeBitmap(Picture);
        Connection connection = sqlCon.Connect();
        try{

            otherStatement = connection.createStatement();
            String sql = String.format("DELETE FROM ECLPictures WHERE DocumentNr = '%s' AND ContainerNr = '%s' AND PictureType = '%s'", DocumentNr, ContainerNr, PictureType);
            otherStatement.execute(sql);

            sql = ("INSERT INTO ECLPictures(DocumentNr, ContainerNr, Username, PictureType, Picture) VALUES (?, ?, ?, ?, ?) ");
            statement = connection.prepareStatement(sql);
            statement.setString(1, DocumentNr);
            statement.setString(2, ContainerNr);
            statement.setString(3, UserName);
            statement.setString(4, PictureType);
            statement.setBytes(5, newPicture);
            statement.executeUpdate();

            values = new tblErrorMessage (0,"Picture uploaded");
        }
        catch (Exception e){
            return new tblErrorMessage(1, e.getMessage());
        }
        finally {
            if (statement != null) try { statement.close(); } catch(Exception e) {values =  new tblErrorMessage(1, e.getMessage());}
            if (connection != null) try { connection.close(); } catch(Exception e) {values =  new tblErrorMessage(1, e.getMessage());}
        }
        values = new tblErrorMessage (0,"Picture uploaded");
        return values;
    }
    tblErrorMessage setECLDetails(
            String DocumentNr,
            String ContainerNr,
            String Username,
            String GateInNumber
    )
    {
        String sql = String.format("EXEC mobile.spSetECLDetail '%s','%s','%s','%s'",DocumentNr,ContainerNr, Username, GateInNumber);
        return getErrorMessage(sql);
    }
    tblErrorMessage isContainerPhotoTaken(
            String DocumentNr,
            String ContainerNr
    )
    {
        String sql = String.format("EXEC mobile.spIsEmptyContainerPhotoTaken '%s','%s'",DocumentNr,ContainerNr);
        return getErrorMessage(sql);
    }
    List<String> getContainerStatus(){
        String sql = "EXEC spGetReturnValues_cfContainerInDocument '1','@ItemStatus'";
        return getStrings(sql);

    }
    tblErrorMessage ChangeContainerStatus(
            String Warehouse,
            String UserName,
            String ContainerNr,
            String Status,
            String Comment
    )
    {
        String sql = String.format("EXEC mobile.spSetContainerStatus '%s','%s','%s','%s','%s'",Warehouse,UserName,ContainerNr,Status,Comment);
        return getErrorMessage(sql);
    }
    List<String> getDeclValues(String Parameter){
        String sql = String.format("EXEC mobile.spGetReturnValues_spDispatchEmptyContainerLoading '%s'",Parameter);
        return getStrings(sql);
    }
    List<String> getDeclBookingRefValues(String BookingReference){
        String sql = String.format("EXEC mobile.spDispatchEmptyContainerLoadingShow '%s'",BookingReference);
        return getStrings(sql);
    }
    tblErrorMessage setDecl(
        String UserName,
        String BookingReference,
        String DocumentNr,
        String Client,
        String ShippingLine,
        String NumberOfContainersBooked,
        String ContainerNumber1,
        String ContainerNumber2,
        String ContainerStatus,
        String NumberOfContainersDispatched,
        String RemainingContainers
    ){
        String sql = String.format("EXEC mobile.spDispatchEmptyContainerLoading '%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'",
                UserName,
                BookingReference,
                DocumentNr,
                Client,
                ShippingLine,
                NumberOfContainersBooked,
                ContainerNumber1,
                ContainerNumber2,
                ContainerStatus,
                NumberOfContainersDispatched,
                RemainingContainers
        );
        return getErrorMessage(sql);
    }
    tblErrorMessage transferContainer(
            String Warehouse,
            String UserName,
            String Customer,
            String BookingRef,
            String ContainerNr,
            String Date
        ){
        String SQL = String.format("EXEC mobile.spContainerTransfer '%s','%s','%s','%s','%s','%s'", Warehouse, UserName, Customer, BookingRef, ContainerNr, Date);
        return getErrorMessage(SQL);
    }
    List<String> getTransferCustomers(){
        String sql = "EXEC dbo.spGetReturnValues 0,'@TransferToCustomer','cfContainerTransfer'";
        return getStrings(sql);
    }
    List<String> getTransferBookingReference(){
        String sql = "EXEC dbo.spGetReturnValues 0,'@TransferToBookingRef','cfContainerTransfer'";
        return getStrings(sql);
    }
    tblErrorMessage checkContinueSugarDispatchTransaction(
            String userName,
            String documentNr,
            String containerNr
    )
    {
        String sql = String.format("EXEC mobile.spCheckContinueSugarDispatchTransaction '%s','%s','%s'",userName,documentNr,containerNr);
        return getErrorMessage(sql);
    }
    tblErrorMessage setContinueSugarDispatchTransaction(
            String userName,
            String documentNr,
            String containerNr
    )
    {
        String sql = String.format("EXEC mobile.spSetContinueSugarDispatchTransaction '%s','%s','%s'",userName,documentNr,containerNr);
        return getErrorMessage(sql);
    }
    tblErrorMessage removeContinueSugarDispatchTransaction(
            String userName,
            String documentNr,
            String containerNr
    )
    {
        String sql = String.format("EXEC mobile.spRemoveContinueSugarDispatchTransaction '%s','%s','%s'",userName,documentNr,containerNr);
        return getErrorMessage(sql);
    }
    tblErrorMessage checkIfPickSlipExists(
            String DocumentNr,
            String ContainerNr
    )
    {
        String sql = String.format("EXEC mobile.spCheckIfPickslipExists '%s','%s'", DocumentNr, ContainerNr);
        return getErrorMessage(sql);
    }
    String getContainerTareWeight(
        String ContainerNr
    )
    {
        String sql = String.format("EXEC mobile.spGetTareWeight '%s'",ContainerNr);
        return getString(sql);
    }
    tblErrorMessage CheckGateOutPictureTaken(
            String DocumentNr,
            String ContainerNr,
            String PictureType
    )
    {
        String sql = String.format("EXEC mobile.spCheckGateOutPictureTaken '%s','%s','%s'",DocumentNr, ContainerNr, PictureType);
        return getErrorMessage(sql);
    }
    tblErrorMessage CheckDispatchProceed(String DocumentNr, String ContainerNr, String UserName){
        String sql = String.format("EXEC mobile.spCheckDispatchProceed '%s','%s','%s'", DocumentNr, ContainerNr, UserName);
        return getErrorMessage(sql);
    }


}
