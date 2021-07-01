package moti.contacts.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import moti.contacts.R;
import moti.contacts.databinding.ContactDetailsBinding;
import moti.contacts.model.Contact;

/**
 * The contact details activity that contains the contact details layout.
 */
public class ContactDetailsActivity extends AppCompatActivity {

    // region Life Cycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the contact details binding.
        ContactDetailsBinding activityBinding =
                DataBindingUtil.setContentView(this, R.layout.contact_details);

        // Get the contact.
        Contact contact = (Contact) getIntent().getSerializableExtra(Contact.class.getSimpleName());

        // Set the contact to contact details binding.
        activityBinding.setContact(contact);
    }

    // endregion
}