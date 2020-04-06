package com.sp.locupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent startServiceIntent = new Intent(context, MyService.class);
//        context.startService(startServiceIntent);
        String action = intent.getAction();
//        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//            Log.d(TAG, "onReceive: ");
//            Log.d(TAG, "onReceive: ");
//            Log.d(TAG, "onReceive: ");
//            Log.d(TAG, "onReceive: ");
//            Log.d(TAG, "onReceive: ");
//            Log.d(TAG, "onReceive: ");
//            Log.d(TAG, "onReceive: ");
//            Log.d(TAG, "onReceive: ");
//
//        }
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, GoogleService.class);
            context.startService(serviceIntent);
            Log.d(TAG, "onReceive: boot");


        } else if (action.equals(Intent.ACTION_REBOOT)) {


            Intent serviceIntent = new Intent(context, GoogleService.class);
            context.startService(serviceIntent);
            Log.d(TAG, "onReceive: boot   2");

        }




    }
}
