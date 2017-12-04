package com.sunkoiwish.waphotorecon.MyServices;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Xaiyeon on 12/5/2017.
 *
 *
 * This is basically related to compe571 for processing, threads, tasks, and scheduling.
 *
 */

public class BatchAnalyzeService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public BatchAnalyzeService() {
        super("Image_Worker_Thread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        ArrayList<String> myList = (ArrayList<String>) intent.getSerializableExtra("mylist");
        // Now we can put our code here
        synchronized (this){

            // Put our Image Analyze and Face API call and stuff here...



        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this, "Batch Image Analyze Service Started...", Toast.LENGTH_LONG);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Batch Image Analyze Service Has Finished...", Toast.LENGTH_LONG);
    }
}
