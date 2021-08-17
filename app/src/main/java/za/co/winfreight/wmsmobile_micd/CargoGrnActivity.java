package za.co.winfreight.wmsmobile_micd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CargoGrnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_grn);
    }

    public void CargoGoodsReceipt_btnBack_Clicked(View v){
        query q = new query(getApplicationContext());
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods Receipt main menu");
        startActivity(intent);
    }

    public void CargoGoodsReceipt_btnScanSugarBags_Clicked(View v){
        query q = new query(getApplicationContext());
        CargoGrnClass myCargo =  ((master)this.getApplication()).get_cargoGoodsReceipt();
        myCargo.setGateInType("SugarBags");
        ((master)this.getApplication()).set_cargoGoodsReceipt(myCargo);
        Intent intent = new Intent(this, CargoGRNGateInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods Receipt main menu");
        startActivity(intent);
    }

    public void CargoGoodsReceipt_btnMineralReceiptLooseBulk_Clicked(View v){
        query q = new query(getApplicationContext());
        CargoGrnClass myCargo =  ((master)this.getApplication()).get_cargoGoodsReceipt();
        myCargo.setGateInType("MineralLooseBulk");
        ((master)this.getApplication()).set_cargoGoodsReceipt(myCargo);
        Intent intent = new Intent(this, CargoGRNGateInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods Receipt main menu");
        startActivity(intent);
    }

    public void CargoGoodsReceipt_btnMineralReceiptBags_Clicked(View v){
        query q = new query(getApplicationContext());
        CargoGrnClass myCargo =  ((master)this.getApplication()).get_cargoGoodsReceipt();
        myCargo.setGateInType("MineralBags");
        ((master)this.getApplication()).set_cargoGoodsReceipt(myCargo);
        Intent intent = new Intent(this, CargoGRNGateInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        q.logMessage(((master) getApplication()), "User exit Cargo Goods Receipt main menu");
        startActivity(intent);
    }
}
