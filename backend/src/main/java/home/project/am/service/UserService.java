package home.project.am.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import home.project.am.model.user.User;
import home.project.am.repository.UserRepository;
import home.project.am.securityutil.SecurityUtil;


@Service
public class UserService {
	private final UserRepository repository;
	
	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	public List<User> getAllUsers(){
	    return repository.findAll();
	}
	
	public Optional<User> getUserByUserName(String userName){
		if(!SecurityUtil.isSafeCredential(userName))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
		return repository.findById(userName);
	}
}
