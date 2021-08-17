package za.co.winfreight.wmsmobile_micd;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;


public class master extends Application {
    private String _user;
    private String _warehouse;
    private CargoGrnClass _cargoGoodsReceipt;
    private CargoDispatchClass _cargoDispatch;
    private EmptyContainerLoadingClass _emptyContainerLoading;
    private String _value;
    private List<String>  _value2;

    public String get_value() {
        return _value;
    }

    public void set_value(String _value) {
        this._value = _value;
    }

    public CargoGrnClass get_cargoGoodsReceipt() {
        return _cargoGoodsReceipt;
    }
    public CargoDispatchClass  get_cargoDispatch() { return _cargoDispatch; }
    public EmptyContainerLoadingClass get_emptyContainerLoading(){ return  _emptyContainerLoading; }

    public void set_cargoGoodsReceipt(CargoGrnClass _cargoGoodsReceipt) {
        this._cargoGoodsReceipt = _cargoGoodsReceipt;
    }

    public void set_emptyContainerLoading(EmptyContainerLoadingClass  _emptyContainerLoading) {
        this._emptyContainerLoading = _emptyContainerLoading;
    }

    public void set_cargoDispatch(CargoDispatchClass  _cargoDispatch) {
        this._cargoDispatch = _cargoDispatch;
    }


    public List<String> get_value2() {
        return _value2;
    }

    public void set_value2(List<String> _value2) {
        this._value2 = _value2;
    }

    public master(String user, String warehouse){
        _user = user;
        _warehouse = warehouse;
        _cargoGoodsReceipt = new CargoGrnClass();
        _cargoGoodsReceipt.setWarehouse(warehouse);
        _cargoGoodsReceipt.setUserName(user);
        _cargoDispatch = new CargoDispatchClass();
        _cargoDispatch.setWarehouse(warehouse);
        _cargoDispatch.setUserName(user);
        _emptyContainerLoading = new EmptyContainerLoadingClass();
        _emptyContainerLoading.setUserName(user);
        _emptyContainerLoading.setWarehouse(warehouse);
        _value = "";
        _value2 = new ArrayList<>();
    }
    public master(){
        _user = "";
        _warehouse = "";
        _cargoGoodsReceipt = new CargoGrnClass();
        _cargoDispatch = new CargoDispatchClass();
        _value = "";
        _value2 = new ArrayList<>();

    }
    public String getUser(){return _user;}
    public String getWarehouse(){return _warehouse;}
    public void setUser(String user){_user = user;}
    public void setWarehouse(String warehouse){_warehouse = warehouse;}
}
