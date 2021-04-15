package codingsharks.ezpricer.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import codingsharks.ezpricer.R;

public class ReEnterPasswordDialog extends AppCompatDialogFragment {
    private EditText password;
    private ConfirmDeleteAccountDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle svaedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_re_enter_password, null);
        builder.setView(view)
                .setTitle("Re-enter Password")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String passwordInput = password.getText().toString().trim();
                        listener.confirmDeleteAccount(passwordInput);
                    }
                })
                .setNegativeButton("Cancel", null);
        password = view.findViewById(R.id.delete_account_re_enter_password);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButon = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButon.setTextColor(Color.parseColor("#FFFF0000"));

        return dialog;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ReEnterPasswordDialog.ConfirmDeleteAccountDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ConfirmDeleteAccountDialogListener");
        }
    }

    public interface ConfirmDeleteAccountDialogListener {
        void confirmDeleteAccount(String password);
    }

}
