package edu.monash.kmhc.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import edu.monash.kmhc.MainActivity;
import edu.monash.kmhc.R;
import edu.monash.kmhc.viewModel.SharedViewModel;

/**
 * This fragment is used to allow users to configure the frequency to update the server.
 * TODO: Selection box to allow users to select frequency (1...60) seconds
 */
public class SettingsFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private Spinner mySpinner;
    private Toolbar toolbar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //set up tool bar
        toolbar = root.findViewById(R.id.home_toolbar);
        setUpToolBar();

        //set up spinner
        mySpinner = root.findViewById(R.id.settings_polling_frequency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(),R.array.polling_frequency,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(spinnerListener);

        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setting up the viewmodel
        sharedViewModel =
                ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        mySpinner.setSelection(((ArrayAdapter) mySpinner.getAdapter()).getPosition(sharedViewModel.getSelectedFrequency().getValue()));

    }

    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            sharedViewModel.updateCurrentSelected(selected);
            Context context = parent.getContext();
            String message = "Server will now update in every " + selected;
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void setUpToolBar(){
        toolbar.setTitle(R.string.title_settings);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

    }

}
