package xyz.topapproid.www.kitchen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String my_intent=intent.getAction();
        String target=Intent.ACTION_BOOT_COMPLETED;

        if (my_intent != null && my_intent.equals(target)) {
            context.startService(new Intent(context, BackgroundService.class));

        }

    }
}