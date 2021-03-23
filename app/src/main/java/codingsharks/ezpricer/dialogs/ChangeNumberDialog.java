package codingsharks.ezpricer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import codingsharks.ezpricer.R;

public class ChangeNumberDialog extends AppCompatDialogFragment {
    private EditText numberET;
    private EditText number2ET;
    private PhoneDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_changenumber, null);
        builder.setView(view)
                .setTitle("Change phone number")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String number = numberET.getText().toString().trim();
                        String number2 = number2ET.getText().toString().trim();
                        listener.applyChange(number, number2);
                    }
                });
        numberET = view.findViewById(R.id.numberET);
        number2ET = view.findViewById(R.id.number2ET);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PhoneDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface PhoneDialogListener {
        void applyChange(String number, String number2);
    }
}

