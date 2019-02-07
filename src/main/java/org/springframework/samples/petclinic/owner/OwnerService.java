package org.springframework.samples.petclinic.owner;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Collection<Owner> findByLastName(String lastName) {

        return ownerRepository.findByLastName(lastName.toLowerCase());
    }

    public void save(Owner owner) {
        ownerRepository.save(owner);
    }

    public Owner findById(Integer id) {
        return ownerRepository.findById(id);
    }
}
