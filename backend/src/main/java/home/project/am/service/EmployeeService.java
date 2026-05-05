package home.project.am.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import home.project.am.dto.PagedResponseDTO;
import home.project.am.model.user.Employee;
import home.project.am.model.user.User;
import home.project.am.repository.EmployeeRepository;
import home.project.am.repository.UserRepository;
import home.project.am.securityutil.SecurityUtil;
import jakarta.transaction.Transactional;

@Service
public class EmployeeService {
	private final EmployeeRepository repository;
	private final UserRepository uRepository;

	public EmployeeService(EmployeeRepository repository, UserRepository uRepository) {
		this.repository = repository;
		this.uRepository = uRepository;
	}
	
	public List<Employee> getAllEmployees(){
		return repository.findAll();
	}
	
	public Optional<Employee> getEmployeeByUserName(String userName) {
		if(!SecurityUtil.isSafeCredential(userName))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
		return repository.findById(userName);
	}
	
	public PagedResponseDTO<Employee> getEmployeesByPage(int page, int size) {
	    Page<Employee> employeePage = repository.findAll(PageRequest.of(page - 1, size));
	   
	    List<Employee> content = employeePage.getContent();
	    if (content.isEmpty()) {
	        throw new RuntimeException("No clients found for the given page!");
	    }

	    return new PagedResponseDTO<>(
	        content,
	        employeePage.getTotalPages(),
	        employeePage.getTotalElements(),
	        employeePage.getNumber()
	    );
	}
	
	@Transactional
	public void setBlockEmployeeStatus(String userName, boolean blockFlag) {
		if(!SecurityUtil.isSafeCredential(userName))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
	    Employee employee = repository.findById(userName).orElseThrow(() -> new RuntimeException("Employee not found"));
	    employee.getUser().setIsBlocked(blockFlag);
	    
	    repository.save(employee);
	}
	
	@Transactional
	public void addEmployee(Employee employee) {
	    User user = employee.getUser();
	    
	    if (user != null) {
	    	if(!SecurityUtil.isSafeCredential(user.getFirstName()) ||!SecurityUtil.isSafeCredential(user.getLastName())
	    			|| !SecurityUtil.isSafeCredential(user.getUserName()))
		        throw new IllegalArgumentException("Neispravan korisnički naziv!");
	    	
	        uRepository.save(user);
	        uRepository.flush();

	        user = uRepository.findById(user.getUserName()).orElseThrow();
	        employee.setUser(user);
	        employee.setUserName(user.getUserName());
	    }

	    repository.saveAndFlush(employee); 
	}
	
	@Transactional
	public void updateEmployee(Employee employee) {
		if (employee.getRole() == null || employee.getRole().isEmpty()) {
		    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role cannot be empty.");
		}
		if (employee.getUser() == null) {
		    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User details must be provided.");
		}
		
		if(!SecurityUtil.isSafeCredential(employee.getUser().getFirstName()) ||!SecurityUtil.isSafeCredential(employee.getUser().getLastName())
    			|| !SecurityUtil.isSafeCredential(employee.getUser().getUserName()))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
		Employee existingEmployee = repository.findById(employee.getUserName())
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

		existingEmployee.setRole(employee.getRole());
	    existingEmployee.setUser(employee.getUser());
	    repository.save(existingEmployee);
	}
	
	@Transactional
	public void deleteEmployee(String userName) {
		if(!SecurityUtil.isSafeCredential(userName))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
	    Employee existingEmployee = repository.findById(userName)
	            .orElseThrow(() -> new RuntimeException("Employee not found: " + userName));
	    
	    repository.delete(existingEmployee);
	}
}
