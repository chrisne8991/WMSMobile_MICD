package za.co.winfreight.wmsmobile_micd;

public class CargoGrnClass {

    private String UserName;
    private String Warehouse;
    private String DocumentNr;
    private String WeatherConditions;
    private String EndWeatherConditions;
    private int MissingBagCount;
    private String BayLocation;
    private String GateInType;
    private boolean IsTarpDamaged;
    private boolean IsOperationalDamage;
    private String OperationalDamagedSlingNr;
    private String OperationalDamagedQty;
    private String OperationalDamagedComments;

    public boolean isOperationalDamage() {
        return IsOperationalDamage;
    }

    public void setOperationalDamage(boolean operationalDamage) {
        IsOperationalDamage = operationalDamage;
    }

    public String getOperationalDamagedSlingNr() {
        return OperationalDamagedSlingNr;
    }

    public void setOperationalDamagedSlingNr(String operationalDamagedSlingNr) {
        OperationalDamagedSlingNr = operationalDamagedSlingNr;
    }

    public String getOperationalDamagedQty() {
        return OperationalDamagedQty;
    }

    public void setOperationalDamagedQty(String operationalDamagedQty) {
        OperationalDamagedQty = operationalDamagedQty;
    }

    public String getOperationalDamagedComments() {
        return OperationalDamagedComments;
    }

    public void setOperationalDamagedComments(String operationalDamagedComments) {
        OperationalDamagedComments = operationalDamagedComments;
    }

    public CargoGrnClass()
    {
        UserName = "";
        Warehouse = "";
        DocumentNr = "";
        WeatherConditions = "";
        BayLocation = "";
        GateInType = "";
        IsTarpDamaged=false;
        MissingBagCount=0;
    }

    public CargoGrnClass(
            String userName,
            String warehouse,
            String documentNr,
            String weatherConditions,
            String bayLocation,
            String gateInType)
    {
        UserName = userName;
        Warehouse = warehouse;
        DocumentNr = documentNr;
        WeatherConditions = weatherConditions;
        BayLocation = bayLocation;
        GateInType = gateInType;
        MissingBagCount=0;
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

    public String getDocumentNr() {
        return DocumentNr;
    }

    public void setDocumentNr(String documentNr) {
        DocumentNr = documentNr;
    }

    public String getWeatherConditions() {
        return WeatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        WeatherConditions = weatherConditions;
    }

    public String getBayLocation() {
        return BayLocation;
    }

    public boolean getIsTarpDamaged() {
        return IsTarpDamaged;
    }

    public void setTarpDamaged(boolean tarpDamaged) {
        IsTarpDamaged = tarpDamaged;
    }


    public void setBayLocation(String bayLocation) {
        BayLocation = bayLocation;
    }
    public String getGateInType() {
        return GateInType;
    }

    public void setGateInType(String gateInType) {
        GateInType = gateInType;
    }

    public String getEndWeatherConditions() {
        return EndWeatherConditions;
    }

    public void setEndWeatherConditions(String endWeatherConditions) {
        EndWeatherConditions = endWeatherConditions;
    }

    public int getMissingBagCount() {
        return MissingBagCount;
    }

    public void setMissingBagCount(int missingBagCount) {
        MissingBagCount = missingBagCount;
    }
}
