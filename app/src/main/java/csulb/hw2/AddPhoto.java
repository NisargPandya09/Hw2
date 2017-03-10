package csulb.hw2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddPhoto extends AppCompatActivity {
//    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    ImageView ImageView;
    EditText editText;
    Bitmap photo;

    File finalFile;
    File photoFile;

    Uri tempUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        Button button = (Button) findViewById(R.id.button);
        Button save = (Button) findViewById(R.id.save);
        ImageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_REQUEST);


            }

        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                String caption = editText.getText().toString();
             //   String path = photoFile.toString();
                String path = finalFile.toString();
                ListActivity list = new ListActivity();
                if (caption.trim().length() > 0) {
// database handler
                    DatabaseHandler db = new DatabaseHandler(
                            getApplicationContext());
// inserting new label into database
                    db.insertNote(caption,path);
// making input filed text to blank
                    editText.setText("");
// Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    goToListActivity(finalFile.toString());

                    // loading spinner with newly added data
                    //list.loadListData();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter caption",
                            Toast.LENGTH_SHORT).show();
                }
                finish();
            }

        });
    }

    private void goToListActivity(String s) {


        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("path",s);
       // finish();
        startActivity(intent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent takePictureIntent = new Intent(this, ViewPhoto.class);


        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ImageView.setImageBitmap(photo);

//            selectedImageURI = data.getData();
//            photoFile = new File(getRealPathFromURI(getApplicationContext(),selectedImageURI));
//            System.out.println(photoFile);



//            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                tempUri = getImageUri(getApplicationContext(), photo);
//         //   tempUri = getApplicationContext().getFilesDir().getPath();
////
//////
////            // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = new File(getRealPathFromURI(tempUri));
//

        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    @Override
    public void onSaveInstanceState(Bundle bundle) {


        bundle.putString("caption",editText.getText().toString());
//        BitmapDrawable drawable = (BitmapDrawable) ImageView.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        bundle.putParcelable("image", bitmap);
//        super.onSaveInstanceState(bundle);
        bundle.putParcelable("image", photo);
        super.onSaveInstanceState(bundle);
//        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Bitmap bitmap;
        editText.setText(savedInstanceState.getString("caption"));
        try {
            bitmap = savedInstanceState.getParcelable("image");
            ImageView.setImageBitmap(bitmap);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
//        ImageView.setImageURI();

    }


//    private String getRealPathFromURI(Context context,Uri contentURI) {
//        String result;
//      //  String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }


    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
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

}

