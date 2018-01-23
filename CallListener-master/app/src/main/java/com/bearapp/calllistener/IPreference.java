package com.bearapp.calllistener;

import android.content.Intent;

/**
 * Created by Henry.Ren on 7/7/16.
 */
public interface IPreference {
    void setPreferenceContainer(PreferenceContainer preferenceContainer);
    boolean onActivityResult(int requestCode, int resultCode, Intent data);
}
