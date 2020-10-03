package com.chandalala.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    /*
    * Async task is meant for short operations
    * */

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);

    }

    public void startAysncTask(View view) {
        ExampleAsyncTask asyncTask = new ExampleAsyncTask(this);
        asyncTask.execute(10);
    }

    /*
    * Describe three generic types
    *
    * The first one is the type of parameter we want to paste to our async task and perform operations on it
    * the second type is for the progress units you want to publish, can be void if you dont want to update progress
    * the third type is the type of result you expect to get back could be json, bitmap, image etc returned
    * from doInBackground method
    *
    * */

    private static class ExampleAsyncTask extends AsyncTask<Integer, Integer, String>{

        private WeakReference<MainActivity> activityWeakReference; // To prevent memory leaks

        ExampleAsyncTask(MainActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = activityWeakReference.get();// Creates a strong reference only in the scope of this method
            if (activity == null || activity.isFinishing()){
                return;
            }

            activity.progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0] ; i++) {
                publishProgress((i * 100) / integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();// Creates a strong reference only in the scope of this method
            if (activity == null || activity.isFinishing()){
                return;
            }
            activity.progressBar.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MainActivity activity = activityWeakReference.get();// Creates a strong reference only in the scope of this method
            if (activity == null || activity.isFinishing()){
                return;
            }

            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
        }


    }
}
