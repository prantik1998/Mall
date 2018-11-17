package com.autonise.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DemoObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.activity_main, container, false);

        Bundle args = getArguments();
        TextView change_text = rootView.findViewById(R.id.textView);
        change_text.setText(getResources().getString(R.string.first_welcome));
        change_text.setTextAppearance(20);
        Log.d("HI", "Hello5");
        return rootView;
    }
}
