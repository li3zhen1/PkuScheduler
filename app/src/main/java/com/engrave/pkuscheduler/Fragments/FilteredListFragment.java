package com.engrave.pkuscheduler.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.engrave.pkuscheduler.Components.FilteredToDoItemRecyclerViewAdapter;
import com.engrave.pkuscheduler.R;
import com.engrave.pkuscheduler.ViewModels.ToDoItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import static com.engrave.pkuscheduler.Utils.UI.LengthConveter.DpToPx;


public class FilteredListFragment extends BottomSheetDialogFragment {

    private List<ToDoItem> mToDoList;
    private Context mContext;
    private View view;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(this.getContext());
    }

    public FilteredListFragment(List<ToDoItem> _tds){
        mToDoList=_tds;
    }


    @Override
    public void onStart() {
        Log.e("TAG", "onStart: ");
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = DpToPx(120*mToDoList.size()+12,getContext());
            bottomSheet.setLayoutParams(layoutParams);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        Log.e("TAG", "onCreateView: ");
        view = inflater.inflate(R.layout.fragment_filtered_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.filtered_todo_item_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.e("se", JSON.toJSONString(mToDoList));
        FilteredToDoItemRecyclerViewAdapter adapter = new FilteredToDoItemRecyclerViewAdapter(mToDoList,view);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, List<ToDoItem> _tds) {
        mToDoList=_tds;
        return onCreateView(inflater,container,savedInstanceState);
    }
}
