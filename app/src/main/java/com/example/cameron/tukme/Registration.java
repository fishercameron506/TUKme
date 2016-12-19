package com.example.cameron.tukme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Registration extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    private void onButtonClick(View v)
    {
        if(v.getId() == R.id.btnDriverReg)
        {
            Intent driverRegIntent = new Intent(this, DriverReg.class);
            startActivity(driverRegIntent);
        }

        if(v.getId() == R.id.btnPassengerReg)
        {
            Intent passengerRegIntent = new Intent(this, PassengerReg.class);
            startActivity(passengerRegIntent);
        }
    }

}
