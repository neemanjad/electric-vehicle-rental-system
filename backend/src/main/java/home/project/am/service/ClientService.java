package home.project.am.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import home.project.am.dto.PagedResponseDTO;
import home.project.am.model.user.Client;
import home.project.am.repository.ClientRepository;
import home.project.am.securityutil.SecurityUtil;
import jakarta.transaction.Transactional;

@Service
public class ClientService {
	private final ClientRepository repository;
	
	public ClientService(ClientRepository repository) {
		this.repository = repository;
	}
	
	public List<Client> getAllClients(){		
		return repository.findAll();
	}
	
	public PagedResponseDTO<Client> getClientsByPage(int page, int size) {
	    Page<Client> clientPage = repository.findAll(PageRequest.of(page - 1, size));
	    List<Client> content = clientPage.getContent();
	    
	    if (content.isEmpty()) {
	        throw new RuntimeException("No clients found for the given page!");
	    }

	    return new PagedResponseDTO<>(
	        content,
	        clientPage.getTotalPages(),
	        clientPage.getTotalElements(),
	        clientPage.getNumber()
	    );
	}
	
	@Transactional
	public void setBlockClientStatus(String userName, boolean blockFlag) {
		if(!SecurityUtil.isSafeCredential(userName))
			throw new IllegalStateException("illegalllllll");
		
	    Client client = repository.findById(userName).orElseThrow(() -> new RuntimeException("Client not found"));
	    client.getUser().setIsBlocked(blockFlag);
	    
	    repository.save(client);
	}
}
