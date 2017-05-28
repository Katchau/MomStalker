package friendstalker.momstalker.EventMenu;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import friendstalker.momstalker.AndroidUser;
import friendstalker.momstalker.Connection;
import friendstalker.momstalker.LogIn;
import friendstalker.momstalker.MainActivity;
import friendstalker.momstalker.R;
import friendstalker.momstalker.Utility.Event;

public class CreateEventActivity extends AppCompatActivity {
    private Button submit = null;
    private EditText eventName  = null;
    private EditText latitude = null;
    private EditText longitude = null;
    private TextView errorMsg = null;
    private Resources res = null;
    private String err1 = null;
    private String err2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        submit = (Button) findViewById(R.id.createEvent);
        eventName = (EditText) findViewById(R.id.createEventName);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);
        errorMsg = (TextView) findViewById(R.id.errorMsgCreateEvent);

        initializeErrorMsgs();
        createButtonListener(this);
    }

    private void initializeErrorMsgs(){
        res = getResources();
        err1 = res.getString(R.string.error_missing_field);
        err2 = res.getString(R.string.error_fail_create_event);
    }

    private boolean setErrorMsg(String msg, String error){
        if(msg == null || msg.equals("") || msg.replace(" ", "").equals("") || msg.equals("-1")){
            errorMsg.setText(error);
            errorMsg.setVisibility(View.VISIBLE);
            return true;
        }
        else return false;
    }

    private void createButtonListener(final CreateEventActivity event){
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = eventName.getText().toString();
                String latS = latitude.getText().toString();
                String lonS = longitude.getText().toString();
                if(!setErrorMsg(name, err1) && !setErrorMsg(latS,err1) && !setErrorMsg(lonS,err1)){
                    double lat = Double.parseDouble(latS);
                    double lon = Double.parseDouble(lonS);
                    boolean validation = Connection.createEvent(name,AndroidUser.user.id,lat,lon);
                    if(validation){
                        AndroidUser.myEvents.add(new Event(-1,-1,name,lat,lon));
                        Intent intent = new Intent(event, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        errorMsg.setText(err2);
                        errorMsg.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
