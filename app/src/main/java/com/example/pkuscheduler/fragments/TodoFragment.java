package com.example.pkuscheduler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;

import com.example.pkuscheduler.R;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.util.TypefaceUtil;

public class TodoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = View.inflate(getActivity(), R.layout.fragment_todo, null);

        return view;
    }
}
