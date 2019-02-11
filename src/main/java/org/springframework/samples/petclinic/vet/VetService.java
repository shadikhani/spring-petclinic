package org.springframework.samples.petclinic.vet;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class VetService {

    private final VetRepository vets;

    public VetService(VetRepository clinicService){
        this.vets = clinicService;
    }

    public Collection<Vet> fetchAll(){
        return vets.findAll();
    }
}
