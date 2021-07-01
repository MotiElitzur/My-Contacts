package moti.contacts.viewModel;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import moti.contacts.model.Contact;
import moti.contacts.model.ContactsRepository;
import moti.contacts.view.ContactDetailsActivity;

/**
 *  The contacts view model that hold the contacts list and operations when it's change .
 */
public class ContactsViewModel extends ViewModel {

    // region Data Members

    // The main context.
    private Context _context;

    // The contacts list.
    private MutableLiveData<List<Contact>> _contactsList;

    // The contact repository.
    private ContactsRepository _contactsRepository;

    // endregion

    // region Constructor

    public ContactsViewModel(Context context){

        _context = context;
        _contactsRepository = new ContactsRepository();

        // Get the contacts live data list.
        _contactsList = _contactsRepository.getContactsLiveData();
    }

    // endregion

    // region Properties

    public MutableLiveData<List<Contact>> getContactsList() {
        return _contactsList;
    }

    public void setContactsList(MutableLiveData<List<Contact>> contactsList) {
        this._contactsList = contactsList;
    }

    // endregion

    // region Public Methods

    /**
     * Start to read the phone contacts.
     */
    public void startReadPhoneContact(){

        // Call the contacts repository to start to read the phone contacts.
        _contactsRepository.getPhoneContacts(_context);
    }

    /**
     * This function called when a contact pressed and open an activity with contact details.
     *
     * @param contact The contact that pressed.
     */
    public void onContactPressed(Contact contact){

        // Pass the contact to contact details activity.
        Intent intent = new Intent(_context, ContactDetailsActivity.class);
        intent.putExtra(Contact.class.getSimpleName(), contact);

        // start contact details activity.
        _context.startActivity(intent);
    }

    // endregion
}