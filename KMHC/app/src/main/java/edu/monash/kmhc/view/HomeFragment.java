package edu.monash.kmhc.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import edu.monash.kmhc.MainActivity;
import edu.monash.kmhc.R;
import edu.monash.kmhc.adapter.HomeAdapter;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationType;
import edu.monash.kmhc.viewModel.SharedViewModel;

/**
 * This fragment is used to display the main screen after the health practitioner
 * selected their patients.
 * All patients monitored by the health practitioner will be displayed in this screen.
 */
public class HomeFragment extends Fragment implements HomeAdapter.OnPatientClickListener {

    private HomeFragment thisFrag ;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private BarChart barChart ;
    private int x = 0;
    private int y = 0;

    /**
     * This method performs all graphical initialization,
     * assign all view variables and set up the tool bar.
     * @return The Main UI view that is created.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedViewModel sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //set tool bar
        toolbar = root.findViewById(R.id.home_toolbar);
        setUpToolBar();

        recyclerView = root.findViewById(R.id.home_recycler_view);

        barChart = (BarChart) root.findViewById(R.id.barchart);

        sharedViewModel.getAllPatientObservations().observe(getViewLifecycleOwner(), patientUpdatedObserver);
        thisFrag = this;
        return root;
    }

    /**
     * This observer observes for changes in selectedpatients' data
     * ( ie : deselect a selected patient / select new patient )
     * It updates the UI when there're changes to the data.
     */
    private Observer<HashMap<String, PatientModel>> patientUpdatedObserver = new Observer<HashMap<String, PatientModel>>() {
            @Override
            public void onChanged(HashMap<String, PatientModel> patientObservationHashMap) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new HomeAdapter(patientObservationHashMap,thisFrag,x,y));
                plotBarChart2(patientObservationHashMap);
            }
        };

    /**
     * This method is called when a patient's card-view is specifically clicked
     * It calls MainActivity to switch the screen to display PatientInfoFragment
     * - PatientInfoFragment displays a patient's detail info ( name , gender etc)
     *
     * @param position position of the patient
     * @param patient the patient to display
     */

    @Override
    public void onPatientClick(int position, PatientModel patient) {
        // navigate to new fragment - pass in the patient
        MainActivity main = (MainActivity) getActivity();
        Objects.requireNonNull(main).newPatientInfoFragment(MainActivity.patient_info__fragment,patient);
    }

    /**
     * This method is responsible to set up the tool bar in the HomeFragment
     * It performs the following
     *
     * 1. Set the title
     * 2. Inflate the menu , and assign OnMenuItemClickListener
     *   - When the Edit Button is click , the app should navigate back to SelectPatientFragment
     */
    private void setUpToolBar(){
        toolbar.setTitle("Home Page");
        toolbar.inflateMenu(R.menu.home_menu);

        Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
            if (item.getItemId() == R.id.menu_patient_edit) {
                MainActivity main = (MainActivity) getActivity();
                Objects.requireNonNull(main).findFragment(MainActivity.select_patients_fragment);
            } else if (item.getItemId() == R.id.menu_edit_x_y) {
                launchDialog();
            }
            return true;
        };

        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void launchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme);
        builder.setTitle("Enter the x,y value");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.prompt_x_y_values, (ViewGroup) getView(), false);
        final EditText xInput = (EditText) viewInflated.findViewById(R.id.x_input);
        final EditText yInput = (EditText) viewInflated.findViewById(R.id.y_input);
        builder.setView(viewInflated);

        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                x = Integer.parseInt(xInput.getText().toString());
                y = Integer.parseInt(yInput.getText().toString());
                System.out.println(x);
                System.out.println(y);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    private void plotBarChart(HashMap<String, PatientModel> patientObservationHashMap){
        ArrayList<String> patientName = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        patientObservationHashMap.forEach((patientID,patientModel) -> {
            if (patientModel.isObservationMonitored(ObservationType.CHOLESTEROL)){
                float patientCholVal = Float.parseFloat(patientModel.getObservationReading(ObservationType.CHOLESTEROL).getValue());
                patientName.add(patientModel.getName());
                System.out.println(patientName.get(i.intValue()) + Float.toString(patientCholVal));
                barEntries.add(new BarEntry(patientCholVal,i.intValue()));
                i.getAndIncrement();
            }
        });
        System.out.println(barEntries);
        BarDataSet dataSet= new BarDataSet(barEntries,"Patient Cholesterol Value");
        BarData data = new BarData(patientName,dataSet);
        barChart.setData(data);
        barChart.setDragEnabled(true); // on by default
        barChart.setVisibleXRangeMaximum(2); // sets the viewport to show 3 bars
        dataSet.setColor(R.color.colorPrimary);
    }

    private void plotBarChart2(HashMap<String, PatientModel> patientObservationHashMap){
        ArrayList<String> patientName = new ArrayList<>();
        List<IBarDataSet> dataBars = new ArrayList<IBarDataSet>();
        AtomicInteger i = new AtomicInteger();
        ArrayList<Integer> colours = new ArrayList<Integer>(
                Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.CYAN, Color.MAGENTA, Color.GRAY, Color.BLACK
                ));

        patientObservationHashMap.forEach((patientID,patientModel) -> {
            // reference from https://stackoverflow.com/questions/38872181/mpandroidchart-bar-chart-how-to-change-color-of-each-label
            boolean hasCholValue = patientModel.getObservationReading(ObservationType.CHOLESTEROL) != null;
            if (hasCholValue && patientModel.isObservationMonitored(ObservationType.CHOLESTEROL)){
                float patientCholVal = Float.parseFloat(patientModel.getObservationReading(ObservationType.CHOLESTEROL).getValue());
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                patientName.add(patientModel.getName());
                barEntries.add(new BarEntry(patientCholVal,0));
                BarDataSet dataset = new BarDataSet(barEntries,patientModel.getName());
                dataset.setColor(colours.get(i.intValue() % colours.size()));
                dataBars.add(dataset);
                i.getAndIncrement();
            }
        });
        //BarData data = new BarData(patientName,dataBars);
        BarData data = new BarData(Collections.singletonList("Total Cholesterol mg/dL"),dataBars);
        barChart.setData(data);
        //barChart.setDescription("Total Cholesterol mg/dL");
        barChart.setDragEnabled(true); // on by default
        barChart.setVisibleXRangeMinimum(patientName.size()%6);
        barChart.setVisibleXRangeMaximum(5);
        barChart.getLegend().setWordWrapEnabled(true);
    }

}


