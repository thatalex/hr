package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.Role;
import by.spalex.diplom.hr.model.User;
import by.spalex.diplom.hr.repository.UserRepository;
import by.spalex.diplom.hr.tools.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PretenderService pretenderService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PretenderService pretenderService) {
        this.userRepository = userRepository;
        this.pretenderService = pretenderService;
    }

    @Override
    public User findOneByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public User savePretender(User user) {
        user.setPassword(Util.encode(user.getPassword()));
        user.setRole(Role.PRETENDER);
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers(String userName, String userEmail, Role userRole) {
        List<User> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (Util.isEmpty(userName) || user.getName().toLowerCase().contains(userName.toLowerCase())) {
                if (Util.isEmpty(userEmail) || user.getEmail().toLowerCase().contains(userEmail.toLowerCase())) {
                    if (userRole == null || user.getRole().equals(userRole)) {
                        users.add(user);
                    }
                }
            }

        }
        return users;
    }

    @Override
    public User save(User user) {
        if (!Util.isEmpty(user.getPassword())) {
            user.setPassword(Util.encode(user.getPassword()));
        } else {
            User tmp = userRepository.findOne(user.getId());
            if (tmp != null) {
                user.setPassword(tmp.getPassword());
            }
        }

        User result = userRepository.save(user);
        if (result.getRole().equals(Role.PRETENDER)) {
            pretenderService.getPretender(result);
        }
        return result;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        pretenderService.deleteByUserId(id);
        userRepository.delete(id);
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findOne(id);
    }
}
