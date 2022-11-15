package pl.lodz.p.edu.rest.managers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pl.lodz.p.edu.rest.model.UniqueId;
import pl.lodz.p.edu.rest.model.users.Client;
import pl.lodz.p.edu.rest.model.users.User;
import pl.lodz.p.edu.rest.repository.impl.UserRepository;

import java.util.List;
import java.util.UUID;

@Transactional
@RequestScoped
public class ClientManager {

    @Inject
    private UserRepository userRepository;

    protected ClientManager() {}

    public void registerClient(Client client) {
        userRepository.add(client);
    }

    public void unregisterClient(UniqueId entityId) {
        userRepository.remove(entityId);
    }

    public Client getByClientId(String id) {
        return userRepository.getClientByIdName(id);
    }

    public User getUserByUuid(UniqueId entityId) {
        return userRepository.get(entityId);
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    public void updateClient(Client client) {
        userRepository.update(client);
    }
}
