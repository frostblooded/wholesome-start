package net.wholesome.wholesomestart;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationCreator.createNewNotification(this);
        AlarmCreator.startAlarm(this, false);
        setContentView(R.layout.activity_settings);
        setUpNameInput();
    }

    private void setUpNameInput() {
        EditText nameInput = (EditText)findViewById(R.id.nameInput);
        String name = GeneralHelpers.getName(this);
        nameInput.setText(name);

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                GeneralHelpers.saveName(getApplicationContext(), charSequence.toString());
                GeneralHelpers.Log("Saving name: " + charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    public void showTimePickerDialog(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }
}
