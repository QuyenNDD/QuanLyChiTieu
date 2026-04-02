package com.example.quanlychitieu.utils;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SimpleTextFragment extends Fragment {
    private static final String ARG_TEXT = "ARG_TEXT";

    public static SimpleTextFragment newInstance(String text) {
        SimpleTextFragment fragment = new SimpleTextFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEXT, text);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView textView = new TextView(requireContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(0xFFFFFFFF);
        textView.setTextSize(20);

        if (getArguments() != null) {
            textView.setText(getArguments().getString(ARG_TEXT, ""));
        }

        return textView;
    }
}
