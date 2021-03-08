package codingsharks.ezpricer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationController {
    String CHANNEL_ID;
    String CHANNEL_NAME;
    String textTitle;
    String textContent;
    int managerID;

    NotificationManager manager;
    Context context;

    NotificationController(){};

    NotificationController(String id, String channelName, String textTitle, String textContent, int managerID, NotificationManager manager, Context context) {
        this.CHANNEL_ID = id;
        this.CHANNEL_NAME = channelName;
        this.textTitle = textTitle;
        this.textContent = textContent;
        this.managerID = managerID;
        this.manager = manager;
        this.context = context;
    };

    NotificationController setID(String id) {
        this.CHANNEL_ID = id;
        return this;
    }

    NotificationController setName(String channelName) {
        this.CHANNEL_NAME = channelName;
        return this;
    }

    NotificationController setTextTitle(String textTitle) {
        this.textTitle = textTitle;
        return this;
    }

    NotificationController setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }

    NotificationController setManagerID(int managerID) {
        this.managerID = managerID;
        return this;
    }

    NotificationController setManager(NotificationManager manager) {
        this.manager = manager;
        return this;
    }

    NotificationController setContext(Context context) {
        this.context = context;
        return this;
    }

    void createNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int notificationImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, notificationImportance);

            manager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.shark)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(managerID, builder.build());
    }
}
