package edu.monash.kmhc.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import edu.monash.kmhc.R;
import edu.monash.kmhc.viewModel.SharedViewModel;

/**
 * This fragment is used to allow users to configure the frequency to update the server.
 */
public class SettingsFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private Spinner mySpinner;
    private Toolbar toolbar;


    /**
     * This method performs all graphical initialization,
     * assign all view variables and set up the tool bar.
     * @return The Settings Screen's UI view that is created.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //set up tool bar
        toolbar = root.findViewById(R.id.settings_toolbar);
        setUpToolBar();

        //set up spinner (drop down list)
        mySpinner = root.findViewById(R.id.settings_polling_frequency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(),R.array.polling_frequency,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(spinnerListener);

        return root;
    }

    /**
     * This method links the fragment will the shared view model class.
     * This allow the settings fragment sync and display the current frequency selected.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setting up the viewmodel
        sharedViewModel =
                ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);

        // set the drop down list to display current frequency
        mySpinner.setSelection(((ArrayAdapter) mySpinner.getAdapter()).getPosition(sharedViewModel.getSelectedFrequency().getValue()));

    }

    /**
     * This is a OnItemSelectedListener that gets triggered when the user selects a new frequency
     *
     * When the user changes the frequency , it passes the new frequency the the view model
     * The app will then start polling at the new selected frequency.
     */
    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            sharedViewModel.updateCurrentSelected(selected);
            Context context = parent.getContext();
            String message = "Server will now update in every " + selected + " seconds";
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * This method is sets up the tool bar title in the Settings fragment.
     */
    private void setUpToolBar(){
        toolbar.setTitle(R.string.title_settings);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
    }

}
