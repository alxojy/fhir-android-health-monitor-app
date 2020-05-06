package edu.monash.kmhc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.sql.SQLOutput;
import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.adapter.HomeAdapter;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.viewModel.SharedViewModel2;

/**
 * This fragment is used to display the main home screen upon login.
 * All patients monitored by the health practitioner will be displayed in this screen.
 * TODO: Select, add, remove patient functionality.
 */
public class HomeFragment extends Fragment implements HomeAdapter.OnPatientClickListener , NavigationView.OnNavigationItemSelectedListener{
    private HomeFragment thisFrag ;
    private SharedViewModel2 sharedViewModel;
    //private TextView textView;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;

    // for navigation drawer
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageButton drawerToggle;

    Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel2.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //set tool bar
        toolbar = root.findViewById(R.id.home_toolbar);
//        drawer = root.findViewById(R.id.home_drawer_layout);
//        navigationView = root.findViewById(R.id.home_nav_view);
//        drawerToggle = root.findViewById(R.id.home_menu_option);
        setUpToolBar();




        //textView = root.findViewById(R.id.text_home);


        recyclerView = root.findViewById(R.id.home_recycler_view);

        sharedViewModel.getAllPatientObservations().observe(getViewLifecycleOwner(), patientUpdatedObserver);

        thisFrag = this;

        return root;
    }

        Observer<HashMap<String, PatientModel>> patientUpdatedObserver = new Observer<HashMap<String, PatientModel>>() {
            @Override
            public void onChanged(HashMap<String, PatientModel> patientObservationHashMap) {
                //System.out.println("another");
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                homeAdapter = new HomeAdapter(patientObservationHashMap,thisFrag);
                recyclerView.setAdapter(homeAdapter);
            }
        };

    @Override
    public void onPatientClick(int position, PatientModel patient) {
        // TODO: move fragment call
        PatientInfoFragment patientInfo= new PatientInfoFragment(patient);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, patientInfo, "patientInfoFragment")
                .addToBackStack(null)
                .commit();
        // navigate to new fragment
        // pass in the patient

    }

    public void setUpToolBar(){
        toolbar.setTitle("Home Page");
        toolbar.inflateMenu(R.menu.home_menu);

        Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO : implement update server click
                // TODO : move call to activity
                if (item.getItemId()== R.id.menu_patient_edit){
                    Fragment select_patient_frag = getActivity().getSupportFragmentManager().findFragmentByTag("select_patient");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,select_patient_frag,"select_patient")
                            .commit();
                }

                else if (item.getItemId() ==R.id.menu_update_server){
                    System.out.println("update server click");
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }

    //swipe to delete
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // check back youtube video coding with Mitch
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.END);
        return false;
    }
}


