package com.bearapp.calllistener;

import android.content.Intent;

/**
 * Created by Henry.Ren on 7/7/16.
 */
public interface PreferenceContainer {

    void startActivityForResultForPreference(Intent intent, int requestCode);

    IPreference addPreference(IPreference preference);

    int generateNextRequestCode();
}
