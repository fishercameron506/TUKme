package com.example.cameron.tukme;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;


public class CurrentLocation extends Service implements LocationListener
{


    public CurrentLocation(Context context)
    {
        this.context = context;
        getLocation();

    }

    public Location getLocation ()
    {
        try
        {
         this.locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
         this.isGpsOn = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
         this.isNetworkEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGpsOn && !isNetworkEnabled)
            {

            }
            else
            {
                this.canReceiveLoc = true;

                if(isNetworkEnabled)
                {
                    this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                }

                if(this.locationManager != null)
                {
                    this.location = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if(this.location != null)
                    {
                        this.lat = this.location.getLatitude();
                        this.lng = this.location.getLongitude();
                    }
                }
            }

            if (isGpsOn)
            {
                if(location == null)
                {
                    this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if(this.locationManager != null)
                    {
                        this.location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(location != null)
                        {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return location;
    }

    public void stopGPS()
    {
        if(this.locationManager != null)
        {
            try
            {
                this.locationManager.removeUpdates(CurrentLocation.this);
            }catch(SecurityException e)
            {
                e.printStackTrace();
            }

        }
    }

    public double getLatitude()
    {
        if(location != null)
        {
         lat = location.getLatitude();
        }
        return  lat;
    }

    public double getLongitude()
    {
        if(this.location != null)
        {
            this.lng = this.location.getLongitude();
        }
        return  lng;
    }

    public boolean CanReceiveLoc()
    {
        return  this.canReceiveLoc;
    }

    public void showSettingsAlert()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Would you like to got to settings?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private Context context = null;
    private Location location;
    private boolean isGpsOn = false;
    private boolean canReceiveLoc = false;
    private boolean isNetworkEnabled = false;
    private double lat;
    private double lng;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager = null;

}
