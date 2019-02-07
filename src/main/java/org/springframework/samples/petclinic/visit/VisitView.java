package org.springframework.samples.petclinic.visit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.MainLayout;
import org.springframework.samples.petclinic.owner.*;

import java.time.LocalDate;
import java.util.List;


@Route(value = "visitView", layout = MainLayout.class)
public class VisitView extends VerticalLayout implements HasUrlParameter<Integer> {
    private final VisitService visitService;
    private final PetService petService;
    private TextField name;
    private DatePicker birthDate;
    private TextField type;
    private TextField owner;
    private DatePicker date;
    private TextField description;
    private Button addButton;
    private Pet pet = new Pet();
    private BeanValidationBinder<Visit> binder;
    private Integer ownerId;
    private Grid<Visit> visitGrid;

    @Autowired
    public VisitView(PetService petService, VisitService visitService) {
        this.petService = petService;
        this.visitService = visitService;

        name = new TextField("Name");
        name.setReadOnly(true);

        birthDate = new DatePicker("Birth Date");
        birthDate.setReadOnly(true);

        type = new TextField("Type");
        type.setReadOnly(true);

        owner = new TextField("Owner");
        owner.setReadOnly(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(name, birthDate, type, owner);

        //Visits

        FormLayout formLayout = new FormLayout();

        date = new DatePicker();
        LocalDate now = LocalDate.now();
        date.setValue(now);
        formLayout.addFormItem(date, "Date");

        description = new TextField();
        formLayout.addFormItem(description, "Description");

        binder = new BeanValidationBinder<>(Visit.class);
        binder.bindInstanceFields(this);

        addButton = new Button("Add Visit", event -> {
            try {
                Visit visit = new Visit();
                binder.writeBean(visit);
                visit.setPetId(pet.getId());
                visitService.save(visit);

                Integer ownerId = pet.getOwner().getId();
                getUI().get().navigate(OwnerInformationView.class, ownerId);
            } catch (ValidationException e) {
                notifyValidationErrors(e.getValidationErrors());
            }
        });

        visitGrid = new Grid<>();
        visitGrid.addColumn(Visit::getDate).setHeader("Date");
        visitGrid.addColumn(Visit::getDescription).setHeader("Description");

        add(horizontalLayout, formLayout, addButton, visitGrid);

    }

    private void notifyValidationErrors(List<ValidationResult> validationErrors) {
        UnorderedList errors = new UnorderedList();
        validationErrors.forEach(validationResult -> errors.add(new ListItem(validationResult.getErrorMessage())));
        Notification notification = new Notification(errors);
        notification.setDuration(5000);
        notification.open();
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer petId) {
        pet = petService.findById(petId);
        name.setValue(pet.getName());
        birthDate.setValue(pet.getBirthDate());
        type.setValue(pet.getType().getName());
        owner.setValue(pet.getOwner().getFirstName() + " " + pet.getOwner().getLastName());

        visitGrid.setItems(visitService.findByPetId(petId));

    }
}
