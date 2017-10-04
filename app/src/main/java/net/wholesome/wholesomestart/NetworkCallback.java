package net.wholesome.wholesomestart;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

public abstract class NetworkCallback implements Callback {
    @Override
    public void onFailure(Call call, IOException e) {
        GeneralHelpers.Log("Failed to make request to " + call.request().url() + ": " + e.getMessage());
        // Reset alarm to try again in several minutes
    }
}
