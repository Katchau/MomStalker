package friendstalker.momstalker.FriendMenu;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import friendstalker.momstalker.FriendActivity;
import friendstalker.momstalker.R;

public class CreateRequestActivity extends AppCompatActivity {
    private EditText username = null;
    private Button add = null;
    private TextView errorMsg = null;
    private Resources res = null;
    private String err = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.addFriendText);
        add = (Button) findViewById(R.id.addFriendB);
        errorMsg = (TextView) findViewById(R.id.errorMsgAddF);

        res = getResources();
        err = res.getString(R.string.error_invalid_username);

        createButtonListener(this);
    }

    private void createButtonListener(final CreateRequestActivity requestActivity){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();

                if(1 == 2){
                    Intent intent = new Intent(requestActivity,ViewFriendsActivity.class);
                    startActivity(intent);
                }
                else{
                    errorMsg.setText(err);
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
