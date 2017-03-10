package csulb.hw2;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;

public class ViewPhoto extends AppCompatActivity {
    TextView show_caption;
    String path;
    ImageView showimage;
    Bitmap bitmap;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        showimage = (ImageView) findViewById(R.id.imageView2);
        show_caption = (TextView) findViewById(R.id.viewcaption);

        Bundle values = getIntent().getExtras();
        //Intent intent = getIntent();
        //image = (Bitmap) intent.getParcelableExtra("path");
       // byte[] byteArray = values.getByteArray("image");

        String Caption = values.getString("caption");
        System.out.println(Caption);
       // path = values.getString("image");
//        System.out.println(path);

        //image = (Bitmap) values.getParcelable("path");
        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        show_caption.setText(Caption);
      //  File f= new File(path);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//        try {
//            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
//        }
//     catch (FileNotFoundException e) {
//        e.printStackTrace();
//    }

     //   bitmap  = getIntent().getExtras().getParcelable("image");


//
//        try {
//            image = (Bitmap) intent.getParcelableExtra("path");
//            showimage.setImageBitmap(image);
//        }
//        catch (Exception e)
//        {
//            System.out.println("Inside try");
//            e.printStackTrace();
//        }

      //  InputStream is = (InputStream) new URL(image).getContent();
        //InputStream is = new URL( image ).openStream();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        path = db.getImagePath(Caption);
//       // path1 = (URI)db.getImagePath(Caption);
////
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = 8;
//        try {
//
//            bitmap = BitmapFactory.decodeFile(path, options);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
       // bitmap=BitmapFactory.decodeStream(is);

            showimage.setImageBitmap(decodeFile(path));
       // showimage.setImageBitmap(bitmap);

       // showimage.setImageBitmap(bmp);


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

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 200;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
