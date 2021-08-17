package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CargoDispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_dispatch);
    }

    public void CargoGoodsdispatch_btnBack_Clicked(View v){
        query q = new query(getApplicationContext());
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods dispatch main menu");
        startActivity(intent);
    }

    public void CargoGoodsdispatch_btnScanSugarBags_Clicked(View v){
        queryDispatch q = new queryDispatch(getApplicationContext());
        CargoDispatchClass myCargo =  ((master)this.getApplication()).get_cargoDispatch();
        myCargo.setGateInType("SugarBags");
        ((master)this.getApplication()).set_cargoDispatch(myCargo);
        Intent intent = new Intent(this, CargoDispatchGateInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods dispatch main menu");
        startActivity(intent);
    }

    public void CargoGoodsdispatch_btnMineraldispatchLooseBulk_Clicked(View v){
        queryDispatch q = new queryDispatch(getApplicationContext());
        CargoDispatchClass myCargo =  ((master)this.getApplication()).get_cargoDispatch();
        myCargo.setGateInType("MineralLooseBulk");
        ((master)this.getApplication()).set_cargoDispatch(myCargo);
        Intent intent = new Intent(this, CargoDispatchGateInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods dispatch main menu");
        startActivity(intent);
    }

    public void CargoGoodsdispatch_btnMineraldispatchBags_Clicked(View v){
        queryDispatch q = new queryDispatch(getApplicationContext());
        CargoDispatchClass myCargo =  ((master)this.getApplication()).get_cargoDispatch();
        myCargo.setGateInType("MineralBags");
        ((master)this.getApplication()).set_cargoDispatch(myCargo);
        Intent intent = new Intent(this, CargoDispatchGateInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods dispatch main menu");
        startActivity(intent);
    }
}
