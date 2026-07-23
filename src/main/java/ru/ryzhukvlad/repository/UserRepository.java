package ru.ryzhukvlad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ryzhukvlad.entity.User;
import ru.ryzhukvlad.entity.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByRoleInOrderById(Collection<UserRole> role);
    Optional<User> findByEmailIgnoreCase(String email);
}
