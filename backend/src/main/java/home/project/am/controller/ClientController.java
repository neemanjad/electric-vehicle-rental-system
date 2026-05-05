package home.project.am.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import home.project.am.dto.PagedResponseDTO;
import home.project.am.model.user.Client;
import home.project.am.service.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
	private final ClientService service;
	
	public ClientController(ClientService service) {
		this.service = service;
	}
	
	@GetMapping
    public PagedResponseDTO<Client> getClientsByPage(@RequestParam(defaultValue = "0") int page,
    												 @RequestParam(defaultValue = "6") int size) {
		try {
	        return service.getClientsByPage(page, size);
	    } catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
	    }
    }
	
	@PutMapping("/block/{userName}")
	public ResponseEntity<String> blockClient(@PathVariable String userName) {
	    try {
	        service.setBlockClientStatus(userName, true);
	        return ResponseEntity.ok("Client blocked successfully.");
	    } catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
	    }
	}

	@PutMapping("/unblock/{userName}")
	public ResponseEntity<String> unblockClient(@PathVariable String userName) {
	    try {
	        service.setBlockClientStatus(userName, false);
	        return ResponseEntity.ok("Client unblocked successfully.");
	    } catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
	    }
	}

}
