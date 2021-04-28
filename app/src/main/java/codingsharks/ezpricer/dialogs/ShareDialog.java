package codingsharks.ezpricer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import codingsharks.ezpricer.R;
import codingsharks.ezpricer.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class ShareDialog extends AppCompatDialogFragment {
    private EditText toET;
    private EditText subjectET;
    private EditText messageET;
    private ShareDialogListener listener;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("items");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_share, null);

        ArrayList<String> watchlist = new ArrayList<>();

        final Query query1 = itemRef.whereEqualTo("userId", mAuth.getUid());
        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    int i = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String name = document.getString("item_name");
                        String price = document.getDouble("item_price") + "";
                        String url = document.getString("productUrl");

                        String combined = "ITEM "+ i + ":\nName: " + name + "\nPrice: $" + price + "\nUrl:" + url + "\n\n";
                        i++;

                        messageET.setText(messageET.getText() + combined);

                    }
                } else {
                    Log.d("SHARE", "Error getting documents: ", task.getException());
                }
            }
        });

        builder.setView(view)
                .setTitle("Share Watchlist")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String to = toET.getText().toString().trim();
                        String subject = subjectET.getText().toString().trim();
                        String message = messageET.getText().toString().trim();

                        listener.shareWatchlist(to, subject, message);
                    }
                });
        toET = view.findViewById(R.id.shareToET);
        subjectET = view.findViewById(R.id.shareSubjectET);
        messageET = view.findViewById(R.id.shareMessageET);

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ShareDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ShareDialogListener");
        }
    }
    public interface ShareDialogListener {
        void shareWatchlist(String to, String subject, String message);
    }

}


