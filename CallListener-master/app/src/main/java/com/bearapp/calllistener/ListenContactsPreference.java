package com.bearapp.calllistener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by Henry.Ren on 16/7/7.
 */
public class ListenContactsPreference extends Preference implements IPreference {

    private static final String TAG = "ListenContactsPreference";

    private int mRequestCode;
    private String mSummary;
    private String mCurrentValue;
    private PreferenceContainer mPreferenceContainer;


    public ListenContactsPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListenContactsPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListenContactsPreference(Context context) {
        this(context, null);
    }


    @Override
    protected void onClick() {
        if (mPreferenceContainer == null)
            return;
        // Launch the contact picker
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        mPreferenceContainer.startActivityForResultForPreference(pickContactIntent, mRequestCode);
    }

    @Override
    public void setPreferenceContainer(PreferenceContainer preferenceContainer) {
        mPreferenceContainer = preferenceContainer;
        mRequestCode = mPreferenceContainer.generateNextRequestCode();
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCode) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = App.getInstance().getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                onSaveListenContact(name + " " + number);

                cursor.close();
            }
        }
        return true;
    }


    public void onSaveListenContact(String contact) {
        if (callChangeListener(contact)) {
            persistString(contact);
        }
    }


    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValueObj) {
        String defaultValue = (String) defaultValueObj;
        if (restorePersistedValue) {
            return;
        }
        // If we are setting to the default value, we should persist it.
        if (!TextUtils.isEmpty(defaultValue)) {
            onSaveListenContact(defaultValue);
        }

    }
}
