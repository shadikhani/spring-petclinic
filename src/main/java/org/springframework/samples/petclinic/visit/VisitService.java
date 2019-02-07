package org.springframework.samples.petclinic.visit;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public void save(Visit visit) {
        visitRepository.save(visit);
    }

    public List<Visit> findByPetId(Integer petId) {
        return visitRepository.findByPetId(petId);
    }
}
