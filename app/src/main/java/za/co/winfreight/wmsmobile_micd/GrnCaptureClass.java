package za.co.winfreight.wmsmobile_micd;

import java.io.Serializable;

public class GrnCaptureClass implements Serializable {
    private String UserName;
    private String Warehouse;
    private String PONr;
    private String Reference1;
    private String Reference2;
    private String Reference3;
    private String Reference4;
    private String Reference5;
    private String ItemCode;
    private String Quantity;
    private String Exception;

    public GrnCaptureClass(){}
    public GrnCaptureClass(
            String userName,
            String warehouse,
            String poNr,
            String reference1,
            String reference2,
            String reference3,
            String reference4,
            String reference5,
            String itemCode,
            String quantity,
            String exception)
    {
        UserName = userName;
        Warehouse = warehouse;
        PONr = poNr;
        Reference1 = reference1;
        Reference2 = reference2;
        Reference3 = reference3;
        Reference4 = reference4;
        Reference5 = reference5;
        ItemCode = itemCode;
        Quantity = quantity;
        Exception = exception;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getWarehouse() {
        return Warehouse;
    }

    public void setWarehouse(String warehouse) {
        Warehouse = warehouse;
    }

    public String getPONr() {
        return PONr;
    }

    public void setPONr(String poNr) {
        PONr = poNr;
    }

    public String getReference1() {
        return Reference1;
    }

    public void setReference1(String reference1) {
        Reference1 = reference1;
    }

    public String getReference2() {
        return Reference2;
    }

    public void setReference2(String reference2) {
        Reference2 = reference2;
    }

    public String getReference3() {
        return Reference3;
    }

    public void setReference3(String reference3) {
        Reference3 = reference3;
    }

    public String getReference4() {
        return Reference4;
    }

    public void setReference4(String reference4) {
        Reference4 = reference4;
    }

    public String getReference5() {
        return Reference5;
    }

    public void setReference5(String reference5) {
        Reference5 = reference5;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getException() {
        return Exception;
    }

    public void setException(String exception) {
        Exception = exception;
    }


}
