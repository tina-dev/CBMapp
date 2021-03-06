package kg.apps.CBMapp.service.impl;

import kg.apps.CBMapp.model.Contact;
import kg.apps.CBMapp.model.User;
import kg.apps.CBMapp.repository.ContactRepository;
import kg.apps.CBMapp.repository.UserRepository;
import kg.apps.CBMapp.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Contact> selectUserContacts() {

        User user = getCurrentUser(userRepository);

        List<Contact> contacts= contactRepository.findAllByUser(user);

        return contacts;
    }

    @Override
    public void addContact(Contact newContact) {
        User user =getCurrentUser(userRepository);
        newContact.setUser(user);
        contactRepository.save(newContact);
    }

    @Override
    public void deleteContactById(long contactId) {
        contactRepository.deleteById(contactId);
    }

    @Override
    public Contact getContactById(Long id) throws Exception {
        if (id.equals(null) || id<0)
            throw new Exception("MyError: Contact for edit cannot found, please insert Long id variable to getContactById contactService...");



        Optional<Contact> contact = contactRepository.findById(id);

        if (!contact.isPresent())
            throw new Exception("MyError: Contact for edit cannot found, please insert Long id variable to getContactById contactService...");

        return contact.get();
    }

    static User getCurrentUser(UserRepository userRepository){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> userOptional = userRepository.findByUsername(authentication.getName());
        return userOptional.get();
    }
}
