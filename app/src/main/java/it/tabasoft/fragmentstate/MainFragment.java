package it.tabasoft.fragmentstate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by valfer on 25/11/16. Tabasoft Srls
 */

public class MainFragment extends Fragment implements SecondFragmentDelegate {


    /**
     * Interface
     */
    TextView statoTextView;

    public static MainFragment newInstance() {

        return new MainFragment();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(MainActivity.TAG, "MainFragment onActivityCreated");
        Toast.makeText(getActivity(), "MainFragment onActivityCreated", Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        Button gotoSecondFragment = (Button)view.findViewById(R.id.gotoSecondFragment);
        gotoSecondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity activity = (MainActivity)getActivity();
                SecondFragment secondFragment = SecondFragment.newInstance("This is the Title (passed from main fragment)", MainFragment.this);
                activity.gotoFragment(secondFragment);
            }
        });

        statoTextView = (TextView)view.findViewById(R.id.statoTextView);
        statoTextView.setText(MyData.sharedInstance().getStr());

        return view;
    }

    @Override
    public void SecondFragmentDelegate_buttonPressed(Context context, Boolean newState) {

        String debugStr = "MainFragment delegate buttonPressed: " + newState;
        Log.d(MainActivity.TAG, debugStr);
        Toast.makeText(context, debugStr, Toast.LENGTH_LONG).show();

        MyData.sharedInstance().setStr(newState ? "ON" : "OFF");
    }
}
