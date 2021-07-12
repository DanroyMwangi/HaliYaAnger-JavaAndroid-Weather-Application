package com.danroy.haliyaanger.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.danroy.haliyaanger.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FlagAPI extends AsyncTask<String,Void,Bitmap> {
    public String country;
    public ArrayList<View> views;

    public FlagAPI(String country){
        this.country = country.toLowerCase();

    }
    public Bitmap FlagData(){
        try {
            URL url = new URL("https://flagcdn.com/40x30/"+ this.country +".png");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            System.out.println(input.toString());
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        return FlagData();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        System.out.println("================================"+bitmap.toString());
    }
}
