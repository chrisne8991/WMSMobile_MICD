package za.co.winfreight.wmsmobile_micd;

public class CargoDispatchClass {

    private String UserName;
    private String Warehouse;
    private String DocumentNr;
    private String GateInType;
    private String ContainerNr;

    private String WeatherConditions;

    public CargoDispatchClass()
    {
        UserName = "";
        Warehouse = "";
        DocumentNr = "";
        WeatherConditions = "";
        GateInType = "";
        ContainerNr = "";
    }

    public CargoDispatchClass(
            String userName,
            String warehouse,
            String documentNr,
            String weatherConditions,
            String containerNr,
            String gateInType)
    {
        UserName = userName;
        Warehouse = warehouse;
        DocumentNr = documentNr;
        WeatherConditions = weatherConditions;
        GateInType = gateInType;
        ContainerNr = containerNr;
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

    public String getGateInType() {
        return GateInType;
    }

    public void setGateInType(String gateInType) {
        GateInType = gateInType;
    }

    public void setContainerNr(String containerNr){
        ContainerNr = containerNr;
    }

    public String getContainerNr(){
        return ContainerNr;
    }
}
