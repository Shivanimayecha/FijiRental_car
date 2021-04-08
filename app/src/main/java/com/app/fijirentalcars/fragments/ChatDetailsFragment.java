package com.app.fijirentalcars.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Adapter.ChatDetailAdpter;
import com.app.fijirentalcars.R;

public class ChatDetailsFragment extends Fragment {

    public ChatDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_deatil_layout, container, false);
        Log.e("ChatDetailsFragment", "ChatDetailsFragment");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        ChatDetailAdpter adapter = new ChatDetailAdpter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}
