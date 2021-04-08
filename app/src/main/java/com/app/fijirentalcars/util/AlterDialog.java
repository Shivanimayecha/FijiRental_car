package com.app.fijirentalcars.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.fijirentalcars.R;

public class AlterDialog
{
    public static void ShowValidation(String s, Context context, final String pos) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        @SuppressLint("InflateParams") View rv = inflater.inflate(R.layout.validation_dialog, null);
        dialogBuilder.setView(rv);
        final Dialog b = dialogBuilder.create();
        TextView tvMessage = rv.findViewById(R.id.tvMessage);
        TextView btnOK = rv.findViewById(R.id.btnOK);
        tvMessage.setText(s);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        dialogBuilder.setTitle("");
        dialogBuilder.setMessage("");
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        b.show();
    }
}
