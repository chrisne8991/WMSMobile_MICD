package za.co.winfreight.wmsmobile_micd;

public class tblWayBillParcelInfo {
    private int _waybillScanned;
    private int _waybillNotScanned;
    private int _parcelScanned;
    private int _parcelNotScanned;

    public int getWaybillScanned(){return _waybillScanned;}
    public int getWaybillNotScanned(){return _waybillNotScanned;}
    public int getParcelScanned(){return _parcelScanned;}
    public int getParcelNotScanned(){return _parcelNotScanned;}

    public void setWaybillScanned(int WaybillScanned){_waybillScanned= WaybillScanned;}
    public void setWaybillNotScanned(int WaybillNotScanned){_waybillNotScanned = WaybillNotScanned;}
    public void setParcelScanned(int ParcelScanned){_parcelScanned = ParcelScanned;}
    public void setParcelNotScanned(int ParcelNotScanned){_parcelNotScanned = ParcelNotScanned;}

    public tblWayBillParcelInfo(){
        setParcelNotScanned(0);
        setParcelScanned(0);
        setWaybillNotScanned(0);
        setWaybillScanned(0);
    }
    public tblWayBillParcelInfo(
            int WaybillScanned,
            int WaybillNotScanned,
            int ParcelScanned,
            int ParcelNotScanned){
        setParcelNotScanned(ParcelNotScanned);
        setParcelScanned(ParcelScanned);
        setWaybillNotScanned(WaybillNotScanned);
        setWaybillScanned(WaybillScanned);
    }
}
