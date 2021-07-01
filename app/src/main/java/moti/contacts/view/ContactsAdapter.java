package moti.contacts.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moti.contacts.BR;
import moti.contacts.R;
import moti.contacts.model.Contact;
import moti.contacts.viewModel.ContactsViewModel;

/**
 * This class contains the contacts recycler view.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    // region Data Members

    // The contacts view model.
    private ContactsViewModel _contactsViewModel;

    // The contacts list.
    private MutableLiveData<List<Contact>> _contactList;

    // endregion

    // region Constructor

    public ContactsAdapter(ContactsViewModel contactsViewModel) {

        _contactsViewModel = contactsViewModel;
        _contactList = contactsViewModel.getContactsList();
    }

    // endregion

    // region RecyclerView Adapter

    @Override
    public int getItemCount() {
        return _contactList == null || _contactList.getValue() == null ? 0 : _contactList.getValue().size();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(_contactList.getValue().get(position), _contactsViewModel);
    }

    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Get the contact view binding.
        ViewDataBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.contact_view, parent, false);

        return new ContactViewHolder(binding);
    }

    // endregion

    // region Contact View Holder

    /**
     * The contact view holder to bind the contact in contact view.
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        // region Data Members

        // The contact binding.
        private final ViewDataBinding binding;

        // endregion

        // region Constructor

        public ContactViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // endregion

        // region Public Methods

        /**
         * Receive a contact and bind it to contact view binding.
         *
         * @param contact The contact to bind.
         *
         * @param contactsViewModel ContactsViewModel, to do more action from contacts view binding.
         */
        public void bind(Contact contact, ContactsViewModel contactsViewModel) {

            // bind the contact.
            binding.setVariable(BR.contact, contact);
            binding.setVariable(BR.contactViewModel, contactsViewModel);

            binding.executePendingBindings();
        }

        // endregion
    }

    // endregion
}