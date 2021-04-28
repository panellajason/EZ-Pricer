package codingsharks.ezpricer.random;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import codingsharks.ezpricer.R;

public class NotificationController {
    String CHANNEL_ID;
    String CHANNEL_NAME;
    String textTitle;
    String textContent;
    int managerID;

    NotificationManager manager;
    Context context;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("items");
    private DocumentReference userItemRef = db.document("user_settings/" + mAuth.getUid());

    public NotificationController(){};

    public NotificationController(String id, String channelName, String textTitle, String textContent, int managerID, NotificationManager manager, Context context) {
        this.CHANNEL_ID = id;
        this.CHANNEL_NAME = channelName;
        this.textTitle = textTitle;
        this.textContent = textContent;
        this.managerID = managerID;
        this.manager = manager;
        this.context = context;
    };

    public NotificationController setID(String id) {
        this.CHANNEL_ID = id;
        return this;
    }

    public NotificationController setName(String channelName) {
        this.CHANNEL_NAME = channelName;
        return this;
    }

    public NotificationController setTextTitle(String textTitle) {
        this.textTitle = textTitle;
        return this;
    }

    public NotificationController setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }

    public NotificationController setManagerID(int managerID) {
        this.managerID = managerID;
        return this;
    }

    public NotificationController setManager(NotificationManager manager) {
        this.manager = manager;
        return this;
    }

    public NotificationController setContext(Context context) {
        this.context = context;
        return this;
    }

    public void createNotification() {
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

    public void priceDropNotification(){
        getUserSettings(new MyCallBackBool() {
            @Override
            public void onCallBack(boolean textNotification, boolean appNotification, boolean emailNotification) {
                // If the user enables application notification
                if (appNotification) {
                    getItemList(itemList -> {
                        PriceDropController priceController;
                        Log.i("After GetItemList()", itemList.toString());
                        if (!itemList.isEmpty()) {
                            // For testing purposes, we will get a random item from the list to
                            // change the prices for notification testing by using Random().
                            Random rand = new Random();
                            int randomItemIndex = rand.nextInt(itemList.size());

                            // Get random item name
                            String itemName = itemList.get(randomItemIndex).get("name").toString();
                            // Get ranomd item price
                            double itemPrice = (double) itemList.get(randomItemIndex).get("price");

                            priceController = new PriceDropController(itemName, itemPrice);

                            textTitle = priceController.getPriceDropTitle();
                            textContent = priceController.getPriceDropContent();
                            createNotification();

                            //For testing purposes we will also send email notification from here.
                            EmailNotification emailNotification1 = new EmailNotification(
                                    context,
                                    priceController.getPriceDropSubject(),
                                    priceController.getPriceDropEmailMessage());
                            emailNotification1.sendEmail();
                        } else {
                            Toast.makeText(
                                    context,
                                    "Your watchlist is empty!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(
                            context,
                            "Application Notification is disabled!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @FunctionalInterface
    public interface MyCallBack {
        void onCallBack(ArrayList<Map<String, Object>> itemList);
    }

    @FunctionalInterface
    public interface MyCallBackBool {
        void onCallBack(boolean textNotification, boolean appNotification, boolean emailNotification);
    }

    private void getItemList(MyCallBack myCallBack) {
        final Query query = itemRef.whereEqualTo("userId", mAuth.getUid());
        ArrayList<Map<String, Object>> itemList = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> itemDetails = new HashMap<>();
                        itemDetails.put("name", document.getString("item_name"));
                        itemDetails.put("price", document.getDouble("item_price"));
                        itemList.add(itemList.size(), itemDetails);
                        Log.i("Item List", itemList.toString());
                    }
                    Log.i("Query", itemList.toString());
                    myCallBack.onCallBack(itemList);
                }
            }
        });
    }

    private void getUserSettings(MyCallBackBool myCallBack) {
        userItemRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    int textSwitch = Integer.parseInt(documentSnapshot.getString("text_notification"));
                    int appSwitch = Integer.parseInt(documentSnapshot.getString("app_notification"));
                    int emailSwitch = Integer.parseInt(documentSnapshot.getString("email_notification"));

                    myCallBack.onCallBack((textSwitch == 1), (appSwitch == 1), (emailSwitch == 1));
                }
            }
        });
    }
}
