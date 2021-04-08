package com.app.fijirentalcars.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Adapter.PerformanceAdapter;
import com.app.fijirentalcars.Adapter.PopUpAdapter;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

public class PerformanceFragment extends Fragment implements View.OnClickListener, PopUpAdapter.OnSelectListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    BottomSheetDialog dialog;
    LinearLayout filter;
    List itemList= Arrays.asList("1 Apr 2020 - 31 Mar 2021 (Current)","1 Jan 2020 - 31 Dec 2020");
    private String mParam2;
    public PerformanceFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerformanceFragment newInstance(String param1, String param2) {
        PerformanceFragment fragment = new PerformanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_performance, container, false);
        Log.e("PerformanceFragment","PerformanceFragment");
        filter=view.findViewById(R.id.ll_recycle);
        filter.setOnClickListener(this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewperformance);
        PerformanceAdapter adapter = new PerformanceAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ll_recycle){

            dialog = new BottomSheetDialog(getContext());
            View dialogView= LayoutInflater.from(getContext()).inflate(R.layout.performance_period,null);

            RecyclerView recyclerView=dialogView.findViewById(R.id.item_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            LinearLayout sublayout=dialogView.findViewById(R.id.subLayout);
            sublayout.setVisibility(View.GONE);
            TextView resetBtn,popupHeading;
            resetBtn=dialogView.findViewById(R.id.reset_btn);
            popupHeading=dialogView.findViewById(R.id.popup_heading);
            popupHeading.setText("Performance period");

            resetBtn.setVisibility(View.GONE);

            LinearLayout searchClose=dialogView.findViewById(R.id.search_close_btn);
            searchClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            PopUpAdapter popUpAdapter=new PopUpAdapter(getContext(),itemList,this, FijiRentalUtils.performanceUtil);
            recyclerView.setAdapter(popUpAdapter);
            dialog.setContentView(dialogView);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                    FrameLayout bottomSheet = (FrameLayout)
                            bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    layoutParams.height = height-(height/3);
                    bottomSheet.setLayoutParams(layoutParams);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onSelect(String val) {
        FijiRentalUtils.performanceUtil=val;
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}
