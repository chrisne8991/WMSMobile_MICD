package za.co.winfreight.wmsmobile_micd;

public class ReferenceClass {
    String _asnNr;
    String _reference1;
    String _reference2;
    String _reference3;
    String _reference4;
    String _reference5;

    public ReferenceClass()
    {
        this._asnNr = "";
        this._reference1 = "";
        this._reference2 = "";
        this._reference3 = "";
        this._reference4 = "";
        this._reference5 = "";
    }
    public ReferenceClass(
            String asnNr,
            String reference1,
            String reference2,
            String reference3,
            String reference4,
            String reference5)
    {
        this._asnNr = asnNr;
        this._reference1 = reference1;
        this._reference2 = reference2;
        this._reference3 = reference3;
        this._reference4 = reference4;
        this._reference5 = reference5;
    }

    public String get_reference1() {return _reference1;}
    public void set_reference1(String _reference1) {this._reference1 = _reference1;}
    public String get_reference2() {return _reference2;}
    public void set_reference2(String _reference2) {this._reference2 = _reference2;}
    public String get_reference3() {return _reference3;}
    public void set_reference3(String _reference3) {this._reference3 = _reference3;}
    public String get_reference4() {return _reference4;}
    public void set_reference4(String _reference4) {this._reference4 = _reference4;}
    public String get_reference5() {return _reference5;}
    public void set_reference5(String _reference5) {this._reference5 = _reference5;}
    public String get_asnNr() {return _asnNr;}
    public void set_asnNr(String _asnNr) {this._asnNr = _asnNr;}

}
