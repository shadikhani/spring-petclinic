package org.springframework.samples.petclinic.vet;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.MainLayout;

@Route(value ="Veterinarians", layout = MainLayout.class)
@SpringComponent
public class VetView extends VerticalLayout {

    private final VetService service;

    @Autowired
    public VetView(VetService service) {
        this.service = service;
        Grid<Vet> grid = new Grid<>();
        grid.setItems(service.fetchAll());
        grid.addColumn(Vet::getFirstName).setHeader("Name");
        grid.addColumn(Vet::getSpecialties).setHeader("Specialties");

        add(grid);
    }
}
