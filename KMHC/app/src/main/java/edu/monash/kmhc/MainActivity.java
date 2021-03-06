package edu.monash.kmhc;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.codesystems.AdministrativeGender;

import java.util.Date;
import java.util.HashMap;

import edu.monash.kmhc.model.PatientAddressModel;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.view.HomeFragment;
import edu.monash.kmhc.view.LoginFragment;
import edu.monash.kmhc.view.PatientInfoFragment;
import edu.monash.kmhc.view.SelectPatientsFragment;
import edu.monash.kmhc.view.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    BottomNavigationView navView;
    public static final String login_fragment = "login_fragment";
    public static final String home_fragment = "home_fragment";
    public static final String settings_fragment = "settings_fragment";
    public static final String patient_info__fragment = "patient_info_fragment";
    public static final String select_patients_fragment = "select_patients_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);

        //new app bar implementation here
        navView.setOnNavigationItemSelectedListener(navListener);

        fragmentManager = getSupportFragmentManager();

        //init home fragment
        launchNewFragment(new LoginFragment(),login_fragment);
}
    public void findFragment(String tag){
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null){
            createNewFragment(tag);
        }
        else{
            launchFragment(fragment,tag);
        }
    }


    public void newPatientInfoFragment(String tag, @Nullable PatientModel patient){
        Fragment fragment = new PatientInfoFragment(patient);
        launchNewFragment(fragment,tag);
    }
    public void newSelectPatientFragment(String tag,String pracID){
        Fragment prev_fragment = fragmentManager.findFragmentByTag(tag);
        if (prev_fragment != null ){
            fragmentManager.beginTransaction().remove(prev_fragment).commit();
            Log.d("main","prev frag destroyed");
        }

        Fragment fragment = new SelectPatientsFragment(pracID);
        launchNewFragment(fragment,tag);
    }
    private void createNewFragment(String tag){
        Fragment fragment = new Fragment ();
        navView.setVisibility(View.VISIBLE);
        switch(tag){
            case home_fragment:
                fragment = new HomeFragment();
                break;
            case settings_fragment:
                fragment = new SettingsFragment();
                break;
        }
        launchNewFragment(fragment,tag);
    }

    private void launchNewFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    private void launchFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag)
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {

                if (item.getItemId() == R.id.navigation_home) {
                    findFragment(home_fragment);

                } else if (item.getItemId() == R.id.navigation_settings) {
                    findFragment(settings_fragment);
                }
                return true;
            };
}