package friendstalker.momstalker;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import friendstalker.momstalker.Utility.Event;
import friendstalker.momstalker.Utility.OptionActivity;
import friendstalker.momstalker.Utility.User;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{

    private GoogleMap mMap;
    String currLocation = null;
    private LocationManager lManager;
    private boolean isProviderActive = true;
    public static MainActivity na = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AndroidUser.user == null) return;
                isProviderActive = !isProviderActive;
                if(isProviderActive){
                    Snackbar.make(view, "Location is Activated!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    changeLocationCamera();
                }
                else{
                    Snackbar.make(view, "Location is Deactivated!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);

        //new AndroidUser();
        if(AndroidUser.user != null)
            changeDisplayUser();
        if(NotificationClient.c == null)
            new NotificationClient();
        if(na == null)na = this;
    }

    public static void notificationPop(Activity ac, String title, String text){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ac)
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setContentTitle(title)
                        .setContentText(text);
        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) ac.getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent options = new Intent(this, OptionActivity.class);
            startActivity(options);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeDisplayUser(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.sign).setVisible(false);
        navigationView.getMenu().findItem(R.id.friendmenu).setVisible(true);
        navigationView.getMenu().findItem(R.id.eventmenu).setVisible(true);
        navigationView.getMenu().findItem(R.id.log).setTitle("Log out from " + AndroidUser.user.name);
        changeLocationCamera();
        updateMapMarkers();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sign) {
            Intent sign = new Intent(this,Register.class);
            startActivity(sign);
        } else if (id == R.id.log) {
            if(AndroidUser.user == null){
                Intent log = new Intent(this,LogIn.class);
                startActivity(log);
            }
            else{
                NotificationClient.logOut(AndroidUser.user.name);
                AndroidUser.resetVars();
                Intent refresh = new Intent(this, MainActivity.class);
                startActivity(refresh);
                finish();
            }
        } else if (id == R.id.friendmenu) {
            Intent friend = new Intent(this,FriendActivity.class);
            startActivity(friend);
        } else if (id == R.id.eventmenu) {
            Intent event = new Intent(this,EventsActivity.class);
            startActivity(event);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeLocationCamera(){
        System.out.println("Changing camera...");
        if(AndroidUser.user.gps != null && currLocation != null){
            LatLng latLng = new LatLng(AndroidUser.user.gps.x,AndroidUser.user.gps.y);
            mMap.addMarker(new MarkerOptions().position(latLng).title("I'm here!!").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                    .showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));
        }
    }

    public void drawMarkers(){
        mMap.clear();
        if(AndroidUser.myEvents != null){
            for(Event e : AndroidUser.myEvents){
                if(e.gps == null) continue;
                LatLng lt = new LatLng(e.gps.x,e.gps.y);
                mMap.addMarker(new MarkerOptions().position(lt).title(e.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                        .showInfoWindow();
            }
        }
        if(AndroidUser.events != null){
            for(Event e : AndroidUser.events){
                if(e.gps == null) continue;
                LatLng lt = new LatLng(e.gps.x,e.gps.y);
                mMap.addMarker(new MarkerOptions().position(lt).title(e.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                        .showInfoWindow();
            }
        }
        if(AndroidUser.friends != null){
            for(User u : AndroidUser.friends){
                if(u.gps == null) continue;
                LatLng lt = new LatLng(u.gps.x,u.gps.y);
                mMap.addMarker(new MarkerOptions().position(lt).title(u.name)).showInfoWindow();
            }
        }
    }

    public void updateMapMarkers(){
            new Thread(new Runnable() {
            @Override
            public void run() {
                while(AndroidUser.user != null){
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        System.err.println("Something interrupted the timer");
                    }
                    System.out.println("lol");
                    User user = AndroidUser.user;
                    if(isProviderActive && user.gps != null)
                        Connection.updateCoords(user.id, user.gps.x, user.gps.y);
                    AndroidUser.events = Connection.getFriendsEvents(user.id);
                    AndroidUser.friends = Connection.getAmizades(user.id);
                    AndroidUser.myEvents = Connection.getEvents(user.id);
                    if(mMap != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawMarkers();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void debuggPosition(){
        if(AndroidUser.user.gps == null)
            System.out.println("No position!");
        else
            System.out.println(AndroidUser.user.gps);
    }

    private LocationListener getLocationListener(){
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(AndroidUser.user == null || !isProviderActive) return;
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                AndroidUser.user.setCoords(lat,lon);
                //double zoom = location.getAltitude();
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> address = geocoder.getFromLocation(lat,lon,1);
                    currLocation = address.get(0).getLocality() + "," + address.get(0).getCountryName();
                    debuggPosition();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                System.out.println("Rip :&");
                return;
            }
        }
        mMap.setMyLocationEnabled(true);
        System.out.println("Permission Granted");
        lManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(lManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && isProviderActive){
            System.out.println("Location Activated");
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, getLocationListener());
        }
    }

}
