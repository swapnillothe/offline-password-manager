package com.example.offlinepasswordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

class CredentialAdapter extends ArrayAdapter<Credential> {

    @NonNull
    private final Context context;
    private final View popupView;

    public CredentialAdapter(@NonNull Context context, int resource, @NonNull List<Credential> items, View popupView) {
        super(context, resource, items);
        this.context = context;
        this.popupView = popupView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Credential credential = getItem(position);

        setView(view,formatLabel(position),R.id.label);

        view.setOnClickListener(v -> {

            final PopupWindow popupWindow = getPopupWindow(view, popupView);

            String username = EncryptedSharedPref.get(context, credential.label + "username");
            String password = EncryptedSharedPref.get(context, credential.label + "password");

            setView(popupView, "Label : " + credential.label, R.id.credential_label);
            setView(popupView, "Username : " + username, R.id.credential_username);
            setView(popupView, "Password : " + password, R.id.credential_password);

            FloatingActionButton back = popupView.findViewById(R.id.back);

            back.setOnClickListener(v1 -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", password);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,"PASSWORD HAS BEEN COPIED!",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });
        });
        return view;
    }

    private String formatLabel(int position) {
        return new StringBuilder().append(position + 1).append(". ").append(getItem(position).label).toString();
    }

    private void setView(View popupView, String labelName, int id) {
        TextView label = popupView.findViewById(id);
        label.setText(labelName);
    }

    private PopupWindow getPopupWindow(View fab, View popupView) {
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(fab, Gravity.CENTER, 0, 0);
        return popupWindow;
    }
}
