package home.project.am.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import home.project.am.model.user.User;

public interface UserRepository extends JpaRepository<User, String> {

}
