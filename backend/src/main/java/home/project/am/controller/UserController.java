package home.project.am.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import home.project.am.model.user.User;
import home.project.am.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService service;
	
	public UserController(UserService service) {
		this.service = service;
	}
	
	@GetMapping
	public List<User> getAllUsers(){
		return service.getAllUsers();
	}
	
	@GetMapping("/{userName}")
	public ResponseEntity<User> getUserById(@PathVariable String userName){
		return service.getUserByUserName(userName)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
