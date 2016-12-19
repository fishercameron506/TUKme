package com.example.cameron.tukme;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cameron.tukme.Directions.DirectionSeekerListener;
import com.example.cameron.tukme.Directions.Distance;
import com.example.cameron.tukme.Directions.Duration;
import com.example.cameron.tukme.Directions.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapLocationActivity extends FragmentActivity implements DirectionSeekerListener
{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private CurrentLocation currentLocation;

    private List<Marker> startMarkers = new ArrayList<>();
    private List<Marker> endMarkers = new ArrayList<>();
    private List<Polyline> path = new ArrayList<>();
    private ProgressDialog dialog;
    private String jobLocation;
    private String myLocation;
    private String key = "";



    private static final String DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    //API key to use Google maps
    private static final String API_KEY = "AIzaSyAkai98UMJtDq6kERtALUnp0kENvA7Nu3k";


    private List<Polyline> polylinePaths = new ArrayList<>();

    private String auth_token_string = "";

    /**
     *
     * @return Gets the start markers
     */
    public List<Marker> getStartMarkers() {
        return startMarkers;
    }

    /**
     *
     * @param startMarkers Sets the start markers
     */
    public void setStartMarkers(List<Marker> startMarkers) {
        this.startMarkers = startMarkers;
    }

    /**
     *
     * @return Gets the end markers
     */
    public List<Marker> getEndMarkers() {
        return endMarkers;
    }

    /**
     *
     * @param endMarkers Sets the end markers
     *
     */
    public void setEndMarkers(List<Marker> endMarkers) {
        this.endMarkers = endMarkers;
    }

    /**
     *
     * @return Gets the path
     */
    public List<Polyline> getPath() {
        return path;
    }

    /**
     *
     * @param path Sets the path
     */
    public void setPath(List<Polyline> path) {
        this.path = path;
    }

    /**
     *
     * @return Get the progress dialog
     */
    public ProgressDialog getProgressDialog() {
        return this.dialog;
    }

    /**
     *
     * @param dialog Sets the progress dialog
     */
    public void setProgressDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    /**
     *
     * @return Gets the current location
     */
    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    /**
     *
     * @param currentLocation Sets the current location
     */
    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        this.currentLocation = new CurrentLocation(MapLocationActivity.this);

        this.key = getIntent().getStringExtra("key");


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MapLocationActivity.this);
        this.auth_token_string = settings.getString(this.key, "");
        this.jobLocation = getIntent().getStringExtra("jobLocation");


        //setUpMapIfNeeded();

        //get the current user position
        try
        {
            //System.out.println("HELLOOOOOOOOOO");
            Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> listAddress = geocoder.getFromLocation(this.currentLocation.getLatitude(), this.currentLocation.getLongitude(),1);
            //System.out.println("list Address 1: "+listAddress);
            //System.out.println("Current location lat: " + this.currentLocation.getLatitude());
            //System.out.println("Current location long: " + this.currentLocation.getLongitude());
            if(listAddress.size() > 0)
            {
                //System.out.println("Address list 2: " + listAddress);
                for(int i = 0; i< listAddress.get(0).getMaxAddressLineIndex(); i++)
                {
                    this.myLocation += listAddress.get(0).getAddressLine(i) +",";
                    //System.out.println("Address: " + this.myLocation);

                }
            }






        }catch(IOException e)
        {
            e.printStackTrace();
        }




        try
        {

            //call the run method to start the google maps
            run();
        }catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        //setUpMapIfNeeded();
    }









    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }


        }

    }


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }


        //LatLng myPosition = new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude());
        //LatLng destPosition = new LatLng(-26.026830, 27.997468);
        //mMap.addMarker(new MarkerOptions().position(destPosition).title(""));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition,16));

    }


    @Override
    public void directionStart() {
        this.dialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction", true);

        if (startMarkers != null) {
            for (Marker marker : startMarkers) {
                marker.remove();
            }
        }

        if (endMarkers != null) {
            for (Marker marker : endMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    /**
     * This method is used to set the start and end markers
     * and out put the polylines between the markers
     */
    public void directionSuccess(List<Route> routes) {
        this.dialog.dismiss();
        polylinePaths = new ArrayList<>();
        startMarkers = new ArrayList<>();
        endMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.getMyLocation(), 16));


            startMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_passanger))
                    .title(route.getMyAddress())
                    .position(route.getMyLocation())));
            endMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.tuk_tuk))
                    .title(route.getJobAddress())
                    .position(route.getJobLocation())));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.getPoints().size(); i++)
            {
                polylineOptions.add(route.getPoints().get(i));
            }


            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    /**
     * This method is used to run the google maps and get the directions data
     * @throws UnsupportedEncodingException
     */
    public void run() throws UnsupportedEncodingException {
        directionStart();
        getData(makeUrl());
    }

    /**
     * This method creates the URL to make the request to get the directions
     * @return Get the URL to send to the server
     * @throws UnsupportedEncodingException
     */
    private String makeUrl() throws UnsupportedEncodingException {

        //String urlOrigin  = URLEncoder.encode(myLocation, "UTF-8");
        String urlDestination = URLEncoder.encode(jobLocation, "UTF-8");

        String urlOrigin  = "Rossmore,+Johannesburg,+2092,+South+Africa";



        return DIRECTION_URL + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + API_KEY;
    }


    /**
     *
     * @param link Sets the link to get the directions from location A to B
     *             Data is received from the Server
     */
    public void getData(String link) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                link, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response == null)
                {

                    return;
                }



                try
                {
                    List<Route> routes = new ArrayList<Route>();
                    JSONObject jsonData = new JSONObject(response);
                    JSONArray jsonRoutes = jsonData.getJSONArray("routes");
                    for (int i = 0; i < jsonRoutes.length(); i++) {
                        JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
                        Route route = new Route();

                        JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
                        JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                        JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                        JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                        JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                        JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
                        JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

                        route.setDistance(new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value")));
                        route.setDuration(new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value")));
                        route.setJobAddress(jsonLeg.getString("end_address"));
                        route.setMyAddress(jsonLeg.getString("start_address"));
                        route.setMyLocation(new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng")));
                        route.setJobLocation(new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng")));
                        route.setPoints(decodePolyLine(overview_polylineJson.getString("points")));

                        routes.add(route);
                    }

                    directionSuccess(routes);
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof AuthFailureError) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MapLocationActivity.this);
                    builder.setTitle("Incorrect Credentials")
                            .setMessage("The Username or Password you have entered is incorrect.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                } else if (error instanceof NoConnectionError) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MapLocationActivity.this);
                    builder.setTitle("Connection Error")
                            .setMessage("Please ensure your device has Data or Wifi Connection.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                } else if (error instanceof ServerError) {
                    if (error.networkResponse.statusCode == 401) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MapLocationActivity.this);
                        builder.setTitle("Account already Exists")
                                .setMessage("Session has Expired. Please Login to Confirm yur Credentials.")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent logIn = new Intent(MapLocationActivity.this, MainActivity.class);
                                        startActivity(logIn);
                                    }
                                });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MapLocationActivity.this);
                        builder.setTitle("Server Error")
                                .setMessage("There seems to be an issue communicating with the server. Please try again later.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else if (error instanceof ParseError) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MapLocationActivity.this);
                    builder.setTitle("Parse Error")
                            .setMessage("There was an issue in communicating with the server. Please try again later.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                } else if (error instanceof NetworkError) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MapLocationActivity.this);
                    builder.setTitle("Network Error")
                            .setMessage("There is an issue with the Network. Please try again later.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                dialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");
                params.put("Authorization", "bearer " + auth_token_string);


                return params;
            }
        };


        // Adding request to request queue
        requestQueue.add(stringRequest);

    }

    /**
     * Google code to decode the polyline
     * @param poly Sets the polyline
     * @return gets the decoded polyline
     */
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }




}
