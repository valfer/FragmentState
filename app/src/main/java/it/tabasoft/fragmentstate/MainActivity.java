package it.tabasoft.fragmentstate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    static final String TAG = "FRAGMENT-STATE-APP";

    private Fragment curFragment;   // fragment currently visible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity onCreate");


        final FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            /**
             * With this code we show the last fragment after app resumes from background
             */
            curFragment = fm.getFragment(savedInstanceState, "curFragment");
        } else {
            /**
             * First time app launches
             */
            curFragment = MainFragment.newInstance();
        }

        /**
         * Questa serve per sapere (su back press) quale è il nuovo cur fragment
         */
        fm.addOnBackStackChangedListener(this);

        String fragmentTag = curFragment.getClass().toString();
        fm.beginTransaction()
                .replace(R.id.container, curFragment, fragmentTag)
                .addToBackStack(fragmentTag)
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

    public void gotoFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        String fragmentTag = fragment.getClass().toString();
        ft.replace(R.id.container, fragment, fragmentTag).
                addToBackStack(fragmentTag).
                commit();
    }

    public Fragment getFragmentByTag(String tag) {

        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void onBackStackChanged() {

        FragmentManager fm = getSupportFragmentManager();
        int stackCount = fm.getBackStackEntryCount();
        if (stackCount > 0) {
            FragmentManager.BackStackEntry bse = fm.getBackStackEntryAt(stackCount - 1);
            String fragmentTag = bse.getName();
            if (fragmentTag != null)
                curFragment = fm.findFragmentByTag(fragmentTag);
        }
    }
}
