package home.project.am.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import home.project.am.dto.LoginRequest;
import home.project.am.dto.LoginResponse;
import home.project.am.dto.PagedResponseDTO;
import home.project.am.component.JwtUtil;
import home.project.am.model.user.Employee;
import home.project.am.service.EmployeeService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	private final EmployeeService service;
	private final JwtUtil jwtUtil;
	
	public EmployeeController(EmployeeService service, JwtUtil jwtUtil) {
		this.service = service;
		this.jwtUtil = jwtUtil;
	}
	
	@GetMapping
    public PagedResponseDTO<Employee> getEmployeesByPage(@RequestParam(defaultValue = "0") int page,
    												 @RequestParam(defaultValue = "6") int size) {
		try {
	        return service.getEmployeesByPage(page, size);
	    } catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
	    }
    }
	
	@PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginContainer) {
        String userName = loginContainer.getUserName();
        String password = loginContainer.getPassword();
        
        return service.getEmployeeByUserName(userName)
                .filter(employee -> employee.getUser().getPassword().equals(hashPass(password)) && !employee.getUser().getIsBlocked()) // Provera lozinke
                .map(employee -> {
					
					LoginResponse response = new LoginResponse(employee.getRole(), jwtUtil.generateToken(userName, employee.getRole()));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)); // Neuspešna autentifikacija
    }
	
	@DeleteMapping("/{userName}")
	public ResponseEntity<String> deleteEmployee(@PathVariable String userName) {
	    try {
	        service.deleteEmployee(userName);
	        return ResponseEntity.ok("Employee deleted successfully.");
	    } catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + userName);
	    }
	}

	
	@PutMapping("/{userName}")
	public ResponseEntity<String> updateEmployee(@RequestBody Employee employee, @PathVariable String userName){
		try {
			if(employee.getUser().getPassword() != null && employee.getUser().getPassword() != "")
				employee.getUser().setPassword(hashPass(employee.getUser().getPassword()));
	        
			if (userName.equals(employee.getUserName())) {
	        	service.updateEmployee(employee);
		        return ResponseEntity.ok("Employee updated successfully.");
	        } else {
	        	service.deleteEmployee(userName);
	        	service.addEmployee(employee);
	        	return ResponseEntity.ok("Employee updated successfully.");
	        }     
	    } catch (ResponseStatusException e) {
	        throw e; // Prosljeđuje status klijentima
	    } catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
	    }
	}
	
	@PutMapping("/block/{userName}")
	public ResponseEntity<String> blockEmployee(@PathVariable String userName){
		try {
			service.setBlockEmployeeStatus(userName, true);
			return ResponseEntity.ok("Employee blocked successfully.");
		} catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
	    }
	}
	
	@PutMapping("/unblock/{userName}")
	public ResponseEntity<String> unblockEmployee(@PathVariable String userName){
		try {
			service.setBlockEmployeeStatus(userName, false);
			return ResponseEntity.ok("Employee unblocked successfully.");
		} catch(RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity<String> addEmployee(@RequestBody Employee employee){
		try {
			employee.getUser().setPassword(hashPass(employee.getUser().getPassword()));
			service.addEmployee(employee);
			return ResponseEntity.status(HttpStatus.CREATED).body("Employee unblocked successfully.");
		} catch(RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	private static String hashPass(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Alghoritm not founded: ", e);
        }
    }
}
