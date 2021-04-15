package com.giovanidasilva.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.giovanidasilva.cursomc.domain.Cliente;
import com.giovanidasilva.cursomc.dto.ClienteDTO;
import com.giovanidasilva.cursomc.repositories.ClienteRepository;
import com.giovanidasilva.cursomc.services.exceptions.DataIntegratyException;
import com.giovanidasilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		//return obj.orElse(null);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				 "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	
	public Cliente insert(Cliente obj) {		
		obj.setId(null);		
		return repo.save(obj);
	}
	
	public Cliente update(Cliente obj) {		
		
		Cliente newObj =  find(obj.getId());	
		updateData(newObj, obj);
		
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		
		try {
			repo.deleteById(id);	
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegratyException("Não é possível excluir porque há entidades relacionadas");
		}		
	}
	
	
	public Page<Cliente> findPage(Integer page, Integer linesPage, String orderBy, String direction){		
		PageRequest pageRequest = PageRequest.of(page,  linesPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
