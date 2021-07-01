package moti.contacts.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.provider.ContactsContract.CommonDataKinds.Phone;

import androidx.lifecycle.MutableLiveData;

/**
 * This class handles the contacts list and get the contacts from the phone.
 */
public class ContactsRepository {

    // region constants

    // The contacts projection to read only this fields and make the query faster.
    private static final String[] PROJECTION = new String[] {
            Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            Phone.NUMBER
    };

    // The limit of contacts number that we wont to request each time.
    private static final int CONTACTS_NUMBER_REQUEST_LIMIT = 1000;

    // The limit of contacts number that we wont to request each time.
    private static final String CONTACTS_SORT_ORDER = Phone.DISPLAY_NAME + " ASC";

    // SQL statement that used to retrieve records from specific number of query records.
    private static final String LIMIT = "LIMIT";

    // SQL statement that used to the number of rows to skip before starting to return rows.
    private static final String OFFSET = "OFFSET";

    private static final String SPACE = " ";

    // The query to get the phone contacts.
    private static final String MY_CONTACTS_REQUEST_QUERY =
            CONTACTS_SORT_ORDER + SPACE + LIMIT + SPACE + CONTACTS_NUMBER_REQUEST_LIMIT + SPACE + OFFSET + SPACE;

    // endregion

    // region Data Members

    // The Contacts list.
    private List<Contact> _contactsList = new ArrayList<>();

    // The contact live data list.
    private MutableLiveData<List<Contact>> _contactsLiveData =  new MutableLiveData<>();

    // endregion

    // region Constructor

    public ContactsRepository() {

    }

    // endregion

    // region Properties

    public MutableLiveData<List<Contact>> getContactsLiveData() {
        return _contactsLiveData;
    }

    public void setContactsLiveData(MutableLiveData<List<Contact>> contactsLiveData) {
        this._contactsLiveData = contactsLiveData;
    }

    // endregion

    // region Public Methods

    /**
     * Receive a contacts list and add it to our contact list.
     *
     * @param contacts The contacts list to add.
     */
    public void addContactsList(List<Contact> contacts){

        // Add the new contacts list to our contact list.
        _contactsList.addAll(contacts);

        // Update the contacts live list.
        fetchLiveList();
    }

    /**
     * Receive a contact and add it to our contact list.
     *
     * @param contact The contact to add.
     */
    public void addContact(Contact contact){

        // Add the new contact to our contact list.
        _contactsList.add(contact);

        // Update the contacts live list.
        fetchLiveList();
    }

    /**
     * Update the contacts live list with our contacts list.
     */
    public void fetchLiveList(){

        // Update the contacts live list with our contacts list.
        _contactsLiveData.postValue(_contactsList);
    }

    /**
     * This function start to read parts of the contacts from the phone
     * on another thread in order to not interrupt the ui from appear and show the contacts list.
     * and update the list for each time we receiving a contacts list.
     *
     * @param context Context to get the contacts from the phone.
     */
    public void getPhoneContacts(Context context){

        // Run on another thread in order to not interrupt the ui.
        new Thread(() -> {

            int offset = 0;

            // Get the first contacts list part from the phone.
            List<Contact> currentLimit = readPhoneContacts(context, offset);

            // Check that the list not empty.
            while(!currentLimit.isEmpty()){

                // Add the new phone contacts list part to our contacts list.
                addContactsList(currentLimit);

                // Update the offset with new value to start read from.
                offset += CONTACTS_NUMBER_REQUEST_LIMIT;

                // Read the next contacts list part from the phone
                currentLimit = readPhoneContacts(context, offset);
            }
        }).start();
    }

    // endregion

    // region Private Methods

    /**
     * This function read the contacts from the phone.
     *
     * @param context Context to get the contacts from the phone.
     * @param offset Integer, From which contact row number to start to read.
     *
     * @return The phone contacts list.
     */
    private List<Contact> readPhoneContacts(Context context, int offset) {

        // The phone contacts list.
        List<Contact> phoneContacts = new ArrayList<>();

        // The contacts id hash set to check if the contact already found.
        HashSet<String> contactIdAlreadyFound = new HashSet<>();

        // Get the phone contacts sorted by name to cursor.
        Cursor contactsCursor =
                context.getContentResolver().query(Phone.CONTENT_URI , PROJECTION, null, null,  MY_CONTACTS_REQUEST_QUERY + offset);

        // Run for each contact.
        while (contactsCursor.moveToNext()) {

            // Get the contact id.
            String contactId =
                    contactsCursor.getString(contactsCursor.getColumnIndex(Phone.CONTACT_ID));

            // Add the contact id and check that is not already exists.
            if (contactIdAlreadyFound.add(contactId)) {

                // Get the contact name and phone number.
                String displayName =
                        contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String displayNumber =
                        contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                // Fix the phone number that will contain just digits and remove israel phone area code.
                displayNumber =
                        displayNumber.replaceAll("[ +\\-]","").replaceFirst("972","0");

                // Add the contact to the list.
                phoneContacts.add(new Contact(contactId, displayName, displayNumber));
            }
        }

        contactsCursor.close();

        return phoneContacts;
    }

    // endregion
}