package com.app.fijirentalcars.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Adapter.GenericAdapter;
import com.app.fijirentalcars.AddressActivity;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.listners.DialogItemListner;

import java.util.List;

public class CustomDialog extends DialogFragment {

    Context context;
    List<Object> ItemsList;
    DialogItemListner dialogItemListner;

    public CustomDialog(Context context, List<Object> itemList, DialogItemListner listner) {
        this.context=context;
        this.ItemsList=itemList;
        dialogItemListner=listner;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return new AlertDialog.Builder(context)
//
//                .create();
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_view,container,false);

        RecyclerView recyclerView=view.findViewById(R.id.item_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(new GenericAdapter(context, ItemsList, dialogItemListner) {
            @Override
            public void selectProduct(int position) {

            }
        });
        recyclerView.getAdapter().notifyDataSetChanged();



        return view;
    }
}
