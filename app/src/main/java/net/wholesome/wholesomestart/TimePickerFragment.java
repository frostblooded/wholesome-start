package net.wholesome.wholesomestart;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;

public class TimePickerFragment extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int savedTimeHour = GeneralHelpers.getTimeHour(getActivity());
        int savedTimeMinute = GeneralHelpers.getTimeMinute(getActivity());

        return new TimePickerDialog(getActivity(), this, savedTimeHour, savedTimeMinute, false);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int timeHour, int timeMinute) {
        GeneralHelpers.saveTime(getActivity(), timeHour, timeMinute);
        GeneralHelpers.Log("Saving time " + timeHour + ":" + timeMinute);
        GeneralHelpers.setAlarm(getActivity(), timeHour, timeMinute);
    }
}
