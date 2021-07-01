package moti.contacts.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.util.List;

import moti.contacts.R;
import moti.contacts.databinding.ActivityMainBinding;
import moti.contacts.model.Contact;
import moti.contacts.viewModel.ContactsViewModel;

/**
 * The main app activity  with the phone contacts list.
 */
public class MainActivity extends AppCompatActivity {

    // region constants

    // The contacts permission code (the number does not really matter).
    private static final int CONTACTS_PERMISSION_CODE = 100;

    // endregion

    // region Data Members

    // The main activity binding.
    private ActivityMainBinding _mainBinding;

    // The contacts adapter.
    private ContactsAdapter _contactsAdapter;

    // endregion

    // region Life Cycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the main binding from main layout.
        _mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Check that the user has give the contacts permission.
        checkContactsPermission(Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION_CODE);
    }

    // endregion

    // region Private Methods

    /**
     * This function check the contacts permission.
     *
     * @param contactsPermission The contacts permission to check.
     * @param requestCode The contacts request permission code.
     */
    private void checkContactsPermission(String contactsPermission, int requestCode) {

        // Check if the contacts permission already exists.
        if (ContextCompat.checkSelfPermission(this, contactsPermission) == PackageManager.PERMISSION_GRANTED) {

            // Let the main screen to show up.
            initContacts();

        } else {

            // Requesting the permission
            ActivityCompat.requestPermissions(this, new String[] {contactsPermission}, requestCode);
        }
    }

    /**
     * Initialize the activity with the view model.
     */
    private void initContacts(){

        ContactsViewModel contactsViewModel = new ContactsViewModel(this);

        // Set the view model to the adapter.
        _contactsAdapter = new ContactsAdapter(contactsViewModel);

        // Add the contacts adapter to the main activity.
        _mainBinding.setContactsAdapter(_contactsAdapter);

        // Start listening when the contact live list updated.
        contactsViewModel.getContactsList().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {

                // Notify the contacts adapter that the contacts list updated.
                _contactsAdapter.notifyDataSetChanged();
            }
        });

        // Start to read the phone contacts.
        contactsViewModel.startReadPhoneContact();
    }

    // endregion

    // region Request Permission.

    /**
     * This function receive the user permission request result
     * and if the permission denied, alerts to the user.
     *
     * @param requestCode The Contacts permission request code.
     * @param permissions The requested permissions.
     * @param grantResults The requested permissions result.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        // Make sure it's our original READ_CONTACTS request
        if (requestCode == CONTACTS_PERMISSION_CODE) {

            // Check if the permission granted.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Let the main screen to show up.
                initContacts();
            } else {

                // Explain to the user why the permission is necessary.
                String userPermissionRequestInfo = getResources().getString(R.string.contacts_permission_required_info);

                // Check if the user phone is above api 29
                // (because from api 30 we can't request again the user permission). .
                if(android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.R){

                    userPermissionRequestInfo += "\n\n" + getResources().getString(R.string.permission_required_api_30_plus);
                }

                // Alert to the user that the permission is necessary.
                new AlertDialog.Builder(this).setTitle(R.string.permission_required_alert)
                        .setMessage(userPermissionRequestInfo).setPositiveButton(android.R.string.yes,null)
                        .setIcon(android.R.drawable.ic_dialog_alert).show();

                // Check that the user phone is below api 30
                // and we can request the user permission again.
                if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.R){

                    // Ask the user permission again.
                    checkContactsPermission(Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION_CODE);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // endregion
}