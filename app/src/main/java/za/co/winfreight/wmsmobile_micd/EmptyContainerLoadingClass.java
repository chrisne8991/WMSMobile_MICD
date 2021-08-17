package za.co.winfreight.wmsmobile_micd;

public class EmptyContainerLoadingClass {
    private String _userName;
    private String _warehouse;
    private String _padNr;
    private String _gateInDocumentNr;
    private String _containerNr;
    private String _containerNr2;

    public EmptyContainerLoadingClass() {
        this._userName = "";
        this._gateInDocumentNr = "";
        this._containerNr = "";
        this._padNr = "";
        this._warehouse = "";
        this._containerNr2 = "";
    }

    public String getContainerNr() {
        return _containerNr;
    }

    public void setContainerNr(String _containerNr) {
        this._containerNr = _containerNr;
    }

    public String getContainerNr2() {
        return _containerNr2;
    }

    public void setContainerNr2(String _containerNr) {
        this._containerNr2 = _containerNr;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String _userName) {
        this._userName = _userName;
    }

    public String getWarehouse() {
        return _warehouse;
    }

    public void setWarehouse(String _warehouse) {
        this._warehouse = _warehouse;
    }

    public String getPadNr() {
        return _padNr;
    }

    public void setPadNr(String _padNr) {
        this._padNr = _padNr;
    }

    public String getGateInDocumentNr() {
        return _gateInDocumentNr;
    }

    public void setGateInDocumentNr(String _gateInDocumentNr) {
        this._gateInDocumentNr = _gateInDocumentNr;
    }
}
