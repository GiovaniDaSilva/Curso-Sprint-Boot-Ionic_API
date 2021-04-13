package com.giovanidasilva.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.giovanidasilva.cursomc.domain.Categoria;
import com.giovanidasilva.cursomc.repositories.CategoriaRepository;
import com.giovanidasilva.cursomc.services.exceptions.DataIntegratyException;
import com.giovanidasilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		//return obj.orElse(null);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				 "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	
	public Categoria insert(Categoria obj) {		
		obj.setId(null);		
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {		
		find(obj.getId());				
		return repo.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		
		try {
			repo.deleteById(id);	
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegratyException("Não é possível excluir uma categoria que possui produtos.");
		}		
	}
	
	
	public Page<Categoria> findPage(Integer page, Integer linesPage, String orderBy, String direction){		
		PageRequest pageRequest = PageRequest.of(page,  linesPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public List<Categoria> findAll(){
		return repo.findAll();
	}
	
}
