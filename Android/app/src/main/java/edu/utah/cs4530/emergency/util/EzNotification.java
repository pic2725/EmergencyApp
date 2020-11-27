package edu.utah.cs4530.emergency.util;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class EzNotification {
    public static final int IMPORTANCE_DEFAULT = 3;
    public static final int IMPORTANCE_HIGH = 4;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_MAX = 5;
    public static final int IMPORTANCE_MIN = 1;
    public static final int IMPORTANCE_NONE = 0;
    private Context mContext;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notification;
    private boolean mOngoing = false;
    private boolean mWakeUp = false;
    private boolean mNotiToUser = false;
    private boolean prioritySetted = false;
    private boolean iconSetted = false;
    private boolean visiblitySetted = false;
    private int mWakeupInterval = 0;
    private int mId = 0;

    public EzNotification(@NonNull Context context, @NonNull String ChannelID) {
        this.mContext = context;
        this.notificationManager = (NotificationManager)this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        this.notification = new NotificationCompat.Builder(this.mContext, ChannelID);
    }

    @RequiresApi(26)
    public static void createChannel(Context mContext, String channelID, String channelName, int importance) {
        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
        notificationManager.createNotificationChannel(channel);
    }

    public void setTitle(@NonNull String title) {
        this.notification.setContentTitle(title);
        this.notification.setStyle((new NotificationCompat.BigTextStyle()).bigText(title));
    }

    public void setMessage(@NonNull String message) {
        this.notification.setContentText(message);
        this.notification.setStyle((new NotificationCompat.BigTextStyle()).bigText(message));
    }

    public void setLargeMessage(@NonNull String message) {
        this.notification.setStyle((new NotificationCompat.BigTextStyle()).bigText(message));
    }

    public void setSubMessage(@NonNull String message) {
        this.notification.setSubText(message);
    }

    public void setTicker(@NonNull String ticker) {
        this.notification.setTicker(ticker);
    }

    public void setOngoing(boolean ongoing) {
        this.notification.setOngoing(ongoing);
        this.mOngoing = ongoing;
    }

    public void setIntent(@NonNull Class cls) {
        Intent intent = new Intent(this.mContext, cls);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        this.setIntent(intent);
    }

    public void setIntent(@NonNull Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this.mContext, 0, intent, 0);
        this.notification.setContentIntent(pendingIntent);
    }

    public void setIntent(@NonNull PendingIntent pendingIntent) {
        this.notification.setContentIntent(pendingIntent);
    }

    public void setShowWhen(boolean bool) {
        this.notification.setShowWhen(bool);
    }

    public void setVectorIcon(@DrawableRes int id) {
        this.notification.setLargeIcon(getBitmapFromVectorDrawable(this.mContext, id));
        this.iconSetted = true;
    }

    public void setIcon(@DrawableRes int id) {
        this.notification.setSmallIcon(id);
        this.iconSetted = true;
    }

    public void setVisibility(int visibility) {
        if (Build.VERSION.SDK_INT > 20) {
            this.notification.setVisibility(visibility);
            this.visiblitySetted = true;
        }

    }

    public void setPriority(int priority) {
        this.notification.setPriority(priority);
        this.prioritySetted = true;
    }

    public void setWakeUp(int interval) {
        this.mWakeUp = true;
        this.mWakeupInterval = interval;
    }

    public void setWakeUp() {
        this.setWakeUp(5000);
    }

    public void setNotiToUser(boolean notiToUser) {
        this.mNotiToUser = notiToUser;
    }

    public void show() {
        this.show((new Random()).nextInt());
    }

    public void show(int id) {
        try {
            if (!this.prioritySetted) {
                this.notification.setPriority(2);
            }

            if (!this.iconSetted) {
//                this.notification.setSmallIcon(drawable.ic_stat_credit);
            }

            if (this.mNotiToUser) {
                this.notification.setVibrate(new long[]{1L, 1L, 1L});
                this.notification.setSound(RingtoneManager.getDefaultUri(2));
                this.notification.setDefaults(1);
                this.notification.setDefaults(-1);
            }

            if (Build.VERSION.SDK_INT > 20 && !this.visiblitySetted) {
                this.notification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            }

            this.notification.setAutoCancel(true);

            Notification notify = this.notification.build();
            if (this.mNotiToUser) {
                notify.flags |= 1;
            }

            if (this.mOngoing) {
                notify.flags |= 34;
            }

            this.mId = id;
            if (this.mWakeUp) {
                PowerManager pm = (PowerManager)this.mContext.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakelock = pm.newWakeLock(268435482, "TCO_HCE:NOTIFICATION");
                wakelock.acquire(this.mWakeupInterval);
            }

            this.notificationManager.notify(id, notify);
        } catch (Exception var5) {
        }

    }

    public void dismiss() {
        this.notificationManager.cancel(this.mId);
    }

    public static void dismiss(Context context, int id) {
        try {
            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(id);
        } catch (Exception var3) {
        }

    }

    private static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        try {
            VectorDrawableCompat drawable = VectorDrawableCompat.create(context.getResources(), drawableId, null);
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception var5) {
            throw var5;
        }
    }
}
