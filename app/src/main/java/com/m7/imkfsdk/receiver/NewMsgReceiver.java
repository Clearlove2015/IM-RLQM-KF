package com.m7.imkfsdk.receiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.odbpo.fenggou.im_rlqm_kf.R;
import com.m7.imkfsdk.MobileApplication;
import com.m7.imkfsdk.chat.ChatActivity;
import com.moor.imkf.IMChatManager;

import java.util.List;

/**
 * 新消息接收器
 *
 */
public class NewMsgReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(IMChatManager.NEW_MSG_ACTION)) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            context.sendBroadcast(new Intent("com.m7.imkfsdk.msgreceiver"));
            //看应用是否在前台
            if(isAppForground(context)) {
//                context.sendBroadcast(new Intent("com.m7.imkfsdk.msgreceiver"));
            }else {
                Intent contentIntent = new Intent(MobileApplication.getInstance(), ChatActivity.class);
                contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                MobileApplication.getInstance(),
                                0,
                                contentIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                //新的通知
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MobileApplication.getInstance());
                Notification notification = builder.setTicker("您有新的消息")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.kf_ic_launcher)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(resultPendingIntent)
                        .setContentTitle("新消息")
                        .setContentText("您有新的消息")
                        .setAutoCancel(true)
                        .build();
                if(notificationManager != null && notification != null) {
                    notificationManager.notify(1, notification);
                }
            }
        }
    }

    /**
     * 判断聊天界面是否在前台
     * @param mContext
     * @return
     */
    public boolean isAppForground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getClassName().equals(ChatActivity.class.getName())) {
                return false;
            }
        }
        return true;
    }
}
