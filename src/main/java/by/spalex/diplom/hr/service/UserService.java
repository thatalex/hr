package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.Role;
import by.spalex.diplom.hr.model.User;

import java.util.List;

public interface UserService {
    User findOneByEmail(String email);

    /**
     * Save user instance. Before saving set role equaled to {@link by.spalex.diplom.hr.model.Role#PRETENDER} and encode password
     */
    User savePretender(User user);

    /**
     * get users list by arguments
     * @param userName part of user's name. May be null
     * @param userEmail part of user's email. May be null.
     * @param userRole user's role. May be null
     * @return list of users.
     */
    List<User> getUsers(String userName, String userEmail, Role userRole);

    /**
     * Save user. If password is not empty - it encoded
     */
    User save(User user);

    void delete(Long id);

    User findOne(Long id);
}
