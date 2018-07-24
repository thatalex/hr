package by.spalex.diplom.hr.repository;

import by.spalex.diplom.hr.model.Role;
import by.spalex.diplom.hr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByEmail(String email);

    List<User> findAllByRole(Role role);
}
