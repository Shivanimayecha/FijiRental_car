package com.app.fijirentalcars.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.fijirentalcars.R;
import com.app.fijirentalcars.listners.DialogButtonListner;

import java.util.List;

public class CustomButtonDialog extends DialogFragment {

    Context context;
    List<String> ItemsList;
    DialogButtonListner dialogItemListner;
    String dialogHeadingText,dialogMessageText,dialogButtonText;

    public CustomButtonDialog(Context context, String heading, String Message,String Button,DialogButtonListner listner) {
        this.context=context;
        this.dialogHeadingText=heading;
        this.dialogMessageText=Message;
        this.dialogButtonText=Button;
        dialogItemListner=listner;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return new AlertDialog.Builder(context)
//
//                .create();
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_with_button_view,container,false);
        TextView dialogHeading,dialogMessage,dialogBtn;

        dialogHeading=view.findViewById(R.id.dialog_heading);
        dialogMessage=view.findViewById(R.id.dialog_message);
        dialogBtn=view.findViewById(R.id.dialog_btn);

        dialogHeading.setText(dialogHeadingText);
        dialogMessage.setText(dialogMessageText);
        dialogBtn.setText(dialogButtonText);

        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogItemListner.onButtonClicked();
            }
        });







        return view;
    }
}