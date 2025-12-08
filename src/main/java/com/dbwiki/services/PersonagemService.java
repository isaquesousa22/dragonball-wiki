package com.dbwiki.services;

import com.dbwiki.models.Personagem;
import com.dbwiki.repositories.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonagemService {

    private final PersonagemRepository personagemRepository;

    @Autowired
    public PersonagemService(PersonagemRepository personagemRepository) {
        this.personagemRepository = personagemRepository;
    }

   
    public List<Personagem> getAllPersonagens() {
        return personagemRepository.findAll();
    }

    
    public Optional<Personagem> getPersonagemById(Long id) {
        return personagemRepository.findById(id);
    }

 
    public Personagem addPersonagem(Personagem personagem) {
        return personagemRepository.save(personagem);
    }

  
    public Personagem updatePersonagem(Long id, Personagem updated) throws Exception {
        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new Exception("Personagem não encontrado"));

        personagem.setNome(updated.getNome());
        personagem.setRaca(updated.getRaca());
        personagem.setDescricao(updated.getDescricao());
        personagem.setImagemUrl(updated.getImagemUrl());

        return personagemRepository.save(personagem);
    }

  
    public void deletePersonagem(Long id) {
        personagemRepository.deleteById(id);
    }
}
