package friendstalker.momstalker;

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
import android.widget.TextView;

import friendstalker.momstalker.EventMenu.CreateEventActivity;
import friendstalker.momstalker.EventMenu.DeleteEventActivity;
import friendstalker.momstalker.EventMenu.FriendEventsActivity;
import friendstalker.momstalker.EventMenu.ViewMyEventsActivity;
import friendstalker.momstalker.FriendMenu.CreateRequestActivity;
import friendstalker.momstalker.FriendMenu.DeleteFriendActivity;
import friendstalker.momstalker.FriendMenu.FriendRequestActivity;
import friendstalker.momstalker.FriendMenu.ViewFriendsActivity;

public class EventsActivity extends AppCompatActivity {
    private Button viewMine = null;
    private Button viewEvents = null;
    private Button add = null;
    private Button remove = null;
    private TextView errorMsg = null;
    private Resources res = null;
    private String err = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //must have in order to make http requests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        viewMine = (Button) findViewById(R.id.event1);
        viewEvents = (Button) findViewById(R.id.event2);
        add = (Button) findViewById(R.id.event3);
        remove = (Button) findViewById(R.id.event4);
        errorMsg = (TextView) findViewById(R.id.errorMsgEvents);

        res = getResources();
        err = res.getString(R.string.error_invalid_action);

        createViewMineListener(this);
        createViewEventsListener(this);
        createAddListener(this);
        createRemoveListener(this);
    }

    private void createViewMineListener(final EventsActivity eventsActivity){
        viewMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUser.myEvents = Connection.getEvents(AndroidUser.user.id);
                if(AndroidUser.myEvents != null && AndroidUser.myEvents.size() > 0){
                    Intent intent = new Intent(eventsActivity,ViewMyEventsActivity.class);
                    startActivity(intent);
                }
                else{
                    errorMsg.setText(err);
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void createViewEventsListener(final EventsActivity eventsActivity){
        viewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUser.events = Connection.getFriendsEvents(AndroidUser.user.id);
                if(AndroidUser.events != null && AndroidUser.events.size() > 0){
                    Intent intent = new Intent(eventsActivity,FriendEventsActivity.class);
                    startActivity(intent);
                }
                else{
                    errorMsg.setText(err);
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void createAddListener(final EventsActivity eventsActivity){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(eventsActivity,CreateEventActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createRemoveListener(final EventsActivity eventsActivity){
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(eventsActivity,DeleteEventActivity.class);
                startActivity(intent);
            }
        });
    }

}
