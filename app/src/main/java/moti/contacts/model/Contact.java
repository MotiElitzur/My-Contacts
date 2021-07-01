package moti.contacts.model;

import java.io.Serializable;

/**
 * This class contains the contact details and properties.
 */
public class Contact implements Serializable {

    // region Data Members

    // The contact id.
    private String _id;

    // The contact name.
    private String _name;

    // The contact phone number.
    private String _phoneNumber;

    // endregion

    // region Constructor

    public Contact(String id, String name, String phoneNumber) {

        this._id = id;
        this._name = name;
        this._phoneNumber = phoneNumber;
    }

    // endregion

    // region Properties

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    // endregion
}