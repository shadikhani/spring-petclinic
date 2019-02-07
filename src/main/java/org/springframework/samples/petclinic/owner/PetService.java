package org.springframework.samples.petclinic.owner;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public void save(Pet pet) {
        petRepository.save(pet);
    }

    public Pet findById(Integer id) {
        return petRepository.findById(id);
    }

    public List<PetType> fetchPetTypes() {

        List<PetType> petTypeList = petRepository.findPetTypes();
        return petTypeList;
    }
}
