package friendstalker.momstalker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import friendstalker.momstalker.Utility.User;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager lManager;
    private boolean isProviderActive = false;
    private User user = null;
    private String currLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        user = new User(1,"Testando...");
    }

    private void changeLocationCamera(){
        if(user.gps != null && currLocation != null){
            LatLng latLng = new LatLng(user.gps.x,user.gps.y);
            mMap.addMarker(new MarkerOptions().position(latLng).title(currLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));
        }
    }

    public void debuggPosition(View view){
        if(user.gps == null)
            System.out.println("No position!");
        else
            System.out.println(user.gps);
    }

    private LocationListener getLocationListener(){
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                user.setCoords(lat,lon);
                //double zoom = location.getAltitude();
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> address = geocoder.getFromLocation(lat,lon,1);
                    currLocation = address.get(0).getLocality() + "," + address.get(0).getCountryName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

//        lManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if(lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, getLocationListener());
//        }
    }
}
