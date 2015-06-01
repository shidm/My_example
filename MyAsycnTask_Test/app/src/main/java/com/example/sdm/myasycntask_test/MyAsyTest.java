package com.example.sdm.myasycntask_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.transform.Result;

public class MyAsyTest extends ActionBarActivity {

    private static String url = "http://img.my.csdn.net/uploads/201504/12/1428806103_9476.png";
    private ProgressBar mProgressBar;
    private ImageView imageView;
    private MyAsyncTask myAsyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myasytest);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.iv_image);
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myAsyncTask !=null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            myAsyncTask.cancel(true);
        }
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            for (int i = 0;i<100 ; i++){
                if (isCancelled()){
                    break;
                }
                publishProgress(i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String Url = params[0];
            Bitmap bitmap = null;
            URLConnection con;
            InputStream is;
            try {
                con = new URL(Url).openConnection();
                is = con.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                is.close();
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mProgressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (isCancelled()){
                return ;
            }
            mProgressBar.setProgress(values[0]);
        }
    }
}
