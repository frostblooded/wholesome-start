package net.wholesome.wholesomestart.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import net.wholesome.wholesomestart.GeneralHelpers;

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
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        GeneralHelpers.saveTime(getActivity(), i, i1);
        GeneralHelpers.Log("Saving time " + i + ":" + i1);
    }
}
