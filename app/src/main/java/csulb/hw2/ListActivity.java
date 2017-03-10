package csulb.hw2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageButton floatbutton;
    ListView  listView1;
    List<String> pic_captions;
    ArrayAdapter<String> dataAdapter;
    String selectedFromList = null;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions (this,
                        new String[]{WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }










        listView1 = (ListView) findViewById(R.id.listView);

        floatbutton = (ImageButton) findViewById(R.id.imageButton);



//        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//        db.DeleteNote();

        try {
            loadListData();
        }
        catch(Exception e) {
            e.printStackTrace();
        }


        floatbutton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                goToSecondActivity();

            }

        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parentAdapter, View view,
                                    int position, long id) {

                selectedFromList = listView1.getItemAtPosition(position).toString();
                goToShowActivity();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.uninstall:
                uninstall();
                return true;

            case R.id.delete:
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                db.DeleteNote();
                pic_captions.clear();
                pic_captions.addAll(db.getAllNotes()); // reload the items from database
                dataAdapter.notifyDataSetChanged();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void loadListData() {
// database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        // List  elements

        pic_captions = db.getAllNotes();
// Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, pic_captions);
// Drop down layout style - list view with radio button
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// attaching data adapter to spinner
        listView1.setAdapter(dataAdapter);
    }

    private void goToSecondActivity() {

        Intent intent = new Intent(this, AddPhoto.class);
        this.startActivity(intent);
    }

    private void goToShowActivity() {

        Bundle values = getIntent().getExtras();
//        String path = values.getString("path");
        Intent intent = new Intent(this, ViewPhoto.class);
        //intent.putExtra("caption",listView1.getSelectedItem().toString());
        intent.putExtra("caption",selectedFromList);
      //  intent.putExtra("image",path);

        this.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        pic_captions.clear();
        pic_captions.addAll(db.getAllNotes()); // reload the items from database
        dataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        Intent intent = new Intent(this, ViewPhoto.class);
        intent.putExtra("caption",listView1.getSelectedItem().toString());
        this.startActivity(intent);
// On selecting a spinner item
//        String label = parent.getItemAtPosition(position).toString();
//// Showing selected spinner item
//        Toast.makeText(parent.getContext(), "You selected: " + label,
//                Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void uninstall()
    {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        startActivity(intent);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
