package edu.monash.kmhc.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.monash.kmhc.MainActivity;
import edu.monash.kmhc.R;

/**
 * Login Fragment is the first screen that appears when health practitioner opens the app.
 * It prompts the health practitioner to enter their ID before using the app.
 */
public class LoginFragment extends Fragment {

    private EditText practitionerID;

    /**
     * This method performs all graphical initialization,
     * assign all view variables and set up the tool bar.
     * @return The Login UI view that is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_login_fragment, container, false);

        practitionerID = root.findViewById(R.id.login_et_pracID);
        practitionerID.setOnKeyListener(pracIDListener);
        Button signInButton = root.findViewById(R.id.login_btn_login);
        signInButton.setOnClickListener(signInButtonListener);
        return root;
    }

    /**
     * This is a OnKeyListener that looks for Enter Key Presses.
     * Pressing the Enter Key is equivalent to clicking the sign in button.
     * It triggers the app to display the next screen
     */
    private EditText.OnKeyListener pracIDListener = (v, keyCode, event) -> {
        boolean handled = false;
        if (keyCode == KeyEvent.KEYCODE_ENTER ) {
            goToNextFragment();
            handled = true;
        }
        return handled;
    };

    /**
     * This is a SignInButtonListener.
     * clicking the sign in button triggers the app to display the next screen
     */
    private View.OnClickListener signInButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (practitionerID.getText().toString().equals("")){
                Context context = getContext();
                Toast toast = Toast.makeText(context, R.string.invalid_username, Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                goToNextFragment();
            }

        }
    };

    /**
     * A method that calls the MainActivity to switch the screen to SelectPatientFragment
     * SelectPatientFragment displays the list of patients that is under the Health Practitioner
     */
    private void goToNextFragment(){
        MainActivity main = (MainActivity) getActivity();
        main.newSelectPatientFragment(MainActivity.select_patients_fragment,practitionerID.getText().toString());
    }
}
