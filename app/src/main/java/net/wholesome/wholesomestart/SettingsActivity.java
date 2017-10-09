package net.wholesome.wholesomestart;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(!GeneralHelpers.getHasBeenOpened(this)) {
            GeneralHelpers.Log("Alarm isn't started yet. Staring it now.");
            AlarmCreator.startAlarm(this);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }
}
