package home.project.am.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import home.project.am.model.user.Client;

public interface ClientRepository extends JpaRepository<Client, String>{
	Page<Client> findAll(Pageable pageable);
}
