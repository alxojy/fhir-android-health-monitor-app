package edu.monash.kmhc;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.monash.kmhc.view.HomeFragment;
import edu.monash.kmhc.view.PatientInfoFragment;
import edu.monash.kmhc.view.SelectPatientsFragment;
import edu.monash.kmhc.view.SettingsFragment;

//public class MainActivity extends AppCompatActivity implements FragmentActionListener {
public class MainActivity extends AppCompatActivity{
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        //new app bar implementation here
        navView.setOnNavigationItemSelectedListener(navListener);



        //Original App bar implementation
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_settings)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);


        fragmentManager = getSupportFragmentManager();

        //init home fragment
        fragmentManager.beginTransaction().replace(R.id.fragment_container,new SelectPatientsFragment(),"select_patient").commit();
        //fragmentManager.beginTransaction().replace(R.id.fragment_container,new HomeFragment(),"home fragment").commit();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                System.out.println("Main Activity - current system fragment count : " +  fragmentManager.getBackStackEntryCount());

            }
        });


    }

   private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    String tag = "";
                    boolean found = false;

                    if (item.getItemId() == R.id.navigation_home){
                        tag = "home_fragment";
                        Fragment home_frag = fragmentManager.findFragmentByTag(tag);
                        if ( home_frag == null){
                            selectedFragment = new HomeFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment,tag)
                                    .addToBackStack(null)
                                    .commit();
                        }
                            else{
                                found = true;
                                selectedFragment = fragmentManager.findFragmentByTag(tag);
                            }
                    }
                    else if (item.getItemId() == R.id.navigation_settings){
                        tag = "settings_fragment";
                        Fragment settings_frag = fragmentManager.findFragmentByTag(tag);
                        if ( settings_frag == null){
                            selectedFragment = new SettingsFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment,tag)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else{
                            found = true;
                            selectedFragment = settings_frag;
                        }

                    }
                    if (found){
                    fragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment,tag)
                            .commit();
                    }

                    return true;
                }
            };



//    @Override
//    public void callBackMethod() {
//        // to be invoke by fragment to create another fragment
//    }
//
//    public void addPatientInfoFragment(){
//        fragmentTransaction = fragmentManager.beginTransaction();
//        PatientInfoFragment patientInfo= new PatientInfoFragment(patient);
//        patientInfo.setCall
//
//        fragmentTransaction.replace(R.id.nav_host_fragment, patientInfo, "patientInfoFragment")
//                .addToBackStack("patientInfoFragment")
//                .commit();
//    }
}
