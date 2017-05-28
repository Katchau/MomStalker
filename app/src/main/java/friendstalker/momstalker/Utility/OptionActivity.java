package friendstalker.momstalker.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import friendstalker.momstalker.Connection;
import friendstalker.momstalker.LogIn;
import friendstalker.momstalker.MainActivity;
import friendstalker.momstalker.NotificationClient;
import friendstalker.momstalker.R;

public class OptionActivity extends AppCompatActivity {
    private Button submit = null;
    private EditText host = null;
    private EditText port = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        submit = (Button) findViewById(R.id.changeIp);
        host = (EditText) findViewById(R.id.hostName);
        port = (EditText) findViewById(R.id.port);

        createButtonListener(this);
    }

    private void changeIp(String hostName, String portS){
        if(hostName != null && hostName != "" && !hostName.replace(" ","").equals("")){
            Connection.host = hostName;
            NotificationClient.host = hostName;
        }
        if(portS != null && portS != "" && !portS.replace(" ","").equals("")){
            Connection.port = Integer.parseInt(portS);
            NotificationClient.port = Integer.parseInt(portS) + 31;//tem de ser diferente
        }
        NotificationClient.rebuildUri();
        Connection.rebuildIp();
    }

    private void createButtonListener(final OptionActivity option){
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String hostName = host.getText().toString();
                String portS = port.getText().toString();
                changeIp(hostName,portS);
                Intent intent = new Intent(option, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
