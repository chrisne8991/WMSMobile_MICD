package za.co.winfreight.wmsmobile_micd;

public class tblErrorMessage {
    private int _errorCode;
    private String _msg;

    public int getErrorCode(){return _errorCode;}
    public String getMsg(){return _msg;}
    public void setErrorCode(int errorCode){_errorCode = errorCode;}
    public void setMsg(String msg){_msg = msg;}

    public tblErrorMessage(){
        setErrorCode(0);
        setMsg("");
    }
    public tblErrorMessage(int errorCode, String msg){
        setMsg(msg);
        setErrorCode(errorCode);
    }
}
