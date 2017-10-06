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
        AlarmCreator.startAlarm(this, false);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onStart() {
        NotificationCreator.createNewNotification(this);
        super.onStart();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }
}
