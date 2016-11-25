package it.tabasoft.fragmentstate;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


/**
 * 1) secondo fragment e quando l'app resume torniamo sullo stesso fragment mostrato precedentemente
 * 2) se secondFragment ha un dato di input, questo deve essere settato come Arg
 * 3) se invece ha uno stato va fatto in savedInstance e onCreate (buttonPressed)
 * 4) il main fragment si passa come delegate e viene avvisato al press del button
 * 4) Prova anche col kill dell'app (de developer Options) e vedi che funziona tutto
 * 5) però su kill attento a oggetti esterni al fragment, tipo singleton (e punta ad articolo:
 * https://medium.com/inloop/android-process-kill-and-the-big-implications-for-your-app-1ecbed4921cb#.n6zckfjfm
 *
 */
public class MainActivity extends AppCompatActivity {

    static final String TAG = "FRAGMENT-STATE-APP";

    private Fragment curFragment;   // fragment currently visible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity onCreate");


        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            /**
             * With this code we show the last fragment after app resumes from background
             */
            curFragment = fm.getFragment(savedInstanceState, "curFragment");
        } else {
            /**
             * First time app launches
             */
            MainFragment mainFragment = MainFragment.newInstance();
            curFragment = mainFragment;
        }

        String fragmentTag = curFragment.getClass().toString();
        fm.beginTransaction()
                .replace(R.id.container, curFragment, fragmentTag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "MainActivity onSaveInstanceState");

        FragmentManager fm = getSupportFragmentManager();
        /**
         * Save the fragment currently visible for restoring state after resume
         */
        fm.putFragment(outState, "curFragment", curFragment);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "MainActivity onDestroy");
    }

    public void gotoSecondFragment(SecondFragmentDelegate delegate) {

        SecondFragment secondFragment = SecondFragment.newInstance("This is the Title (passed from main fragment)", delegate);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        String secondFragmentTag = secondFragment.getClass().toString();
        ft.replace(R.id.container, secondFragment, secondFragmentTag).
                addToBackStack(null).
                commit();
        curFragment = secondFragment;
    }

    public Fragment getFragmentByTag(String tag) {

        return getSupportFragmentManager().findFragmentByTag(tag);
    }

}
