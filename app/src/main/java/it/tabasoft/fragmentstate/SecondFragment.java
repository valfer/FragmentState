package it.tabasoft.fragmentstate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by valfer on 25/11/16. Tabasoft Srls
 */

public class SecondFragment extends Fragment {

    /**
     * Input
     */
    private String title;
    private SecondFragmentDelegate delegate;

    /**
     * State
     */
    private Boolean buttonPressed = false;

    /**
     * Interface
     */
    private TextView stateTextView;

    static <T extends Fragment & SecondFragmentDelegate> SecondFragment newInstance(String title, T delegate) {

        SecondFragment f = new SecondFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("delegate", delegate.getTag());
        f.setArguments(args);

        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // il delegate va preso qui perché in onCreate non siamo ancora nell'activity e non pocciamo chiamare getFragmentByTag
        Bundle args = getArguments();
        String delegateTag = args.getString("delegate");
        delegate = (SecondFragmentDelegate)((MainActivity)getActivity()).getFragmentByTag(delegateTag);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        /**
         * Questo va preso qui e non in onActivityCreated, perchè onActivityCreated non
         * viene chiamata se il fragment è resumato ma in backStack
         * (prova test con due destroy/resume consecutivi su fragment successivo)
         */
        title = args.getString("title");

        /**
         * State
         */
        if (savedInstanceState != null) {
            buttonPressed = savedInstanceState.getBoolean("buttonPressed");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("buttonPressed", buttonPressed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.second_fragment, container, false);

        TextView titleTextView = (TextView)view.findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        // State
        stateTextView = (TextView)view.findViewById(R.id.stateTextView);
        setState();
        Button stateButton = (Button)view.findViewById(R.id.stateButton);
        stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonPressed = !buttonPressed;
                setState();
                delegate.SecondFragmentDelegate_buttonPressed(getActivity(), buttonPressed);
            }
        });

        return view;
    }

    private void setState() {

        String newStateInfo;
        if (buttonPressed) {
            newStateInfo = "You pressed the Button";
        } else {
            newStateInfo = "The Button is up";
        }
        stateTextView.setText(newStateInfo);
    }

}
