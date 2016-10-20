package de.halters.andmatic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class ActionActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        for (int i = 1; i < 9; i++) {
            int id = getResources().getIdentifier("buttonV"+i, "id", getPackageName());
            if (id > 0) {
                Button button = (Button) findViewById(id);
                button.setOnClickListener(btnListener);
                if (button != null) {
                    String text = preferences.getString("btn" + i + "Name", "");

                    if (text != null && text.length() > 0) {
                        button.setText(text);
                    } else {
                        button.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action, menu);
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

            Intent i = new Intent(this, AppPreferencesActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            XMLRPCCallback listener = new XMLRPCCallback() {
                public void onResponse(long id, Object result) {
                    System.out.println("onResponse");
                }
                public void onError(long id, XMLRPCException error) {
                    System.out.println("onError: " + error.getMessage());
                }
                public void onServerError(long id, XMLRPCServerException error) {
                    System.out.println("On error: " + error.getMessage());
                }
            };

            try {
                XMLRPCClient client = new XMLRPCClient(new URL(preferences.getString("ccuAddress", "")));

                String btnName = ((Button)findViewById(v.getId())).getText().toString();

                int id = v.getId();
                String tag = (String) ((Button) v).getTag();

                Object[] params = null;

                String btnAdress = preferences.getString(tag + "Adress", "");
                String btnPressType = preferences.getString(tag + "PressType", "");

                params = new Object[]{btnAdress, btnPressType, Boolean.TRUE};


                Toast.makeText(ActionActivity.this, "Anfrage senden: " + btnName, Toast.LENGTH_SHORT).show();
                client.callAsync(listener, "setValue", params);
            } catch (MalformedURLException ex){

            }
        }
    };

}
