package home.project.am.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import home.project.am.model.user.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	Page<Employee> findAll(Pageable pageable);
}
