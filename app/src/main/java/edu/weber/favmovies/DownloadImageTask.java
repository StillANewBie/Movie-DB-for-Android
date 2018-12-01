package edu.weber.favmovies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... url) {

        Bitmap logo = null;

        try {
            InputStream is = new URL(url[0]).openStream();

            logo = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            Log.d("URL", "Bad URL");
        } catch (IOException e) {
            Log.d("IO", "IO Error");
        }

        return logo;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
