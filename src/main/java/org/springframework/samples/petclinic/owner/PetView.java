package org.springframework.samples.petclinic.owner;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.MainLayout;

import java.util.List;


@Route(value = "AddPet", layout = MainLayout.class)

public class PetView extends VerticalLayout implements HasUrlParameter<Integer> {

    private Div header;
    private TextField owner;
    private TextField name;
    private DatePicker birthDate;
    private ComboBox<PetType> type;
    private BeanValidationBinder<Pet> binder;
    private Button save;
    private Pet pet;
    private final PetService petService;
    private final OwnerService ownerService;

    @Autowired
    public PetView(PetService petService, OwnerService ownerService) {
        this.petService = petService;
        this.ownerService = ownerService;

        header = new Div();
        header.setText("New Pet");
        binder = new BeanValidationBinder<>(Pet.class);
        owner = new TextField("Owner");
        binder.forField(owner).bind(pet -> pet.getOwner().getFirstName() + " " + pet.getOwner().getLastName(), null);
        name = new TextField("Name");
        birthDate = new DatePicker("Birth Date");
        birthDate.setPlaceholder("MM/DD/YYYY");
        type = new ComboBox<>("Type");
        type.setItems(petService.fetchPetTypes());

        binder.bindInstanceFields(this);

        save = new Button();

        save.addClickListener(event -> {
            try {
                binder.writeBean(pet);
                petService.save(pet);

                Integer ownerId = pet.getOwner().getId();
                getUI().get().navigate(OwnerInformationView.class, ownerId);
            } catch (ValidationException e) {
                notifyValidationErrors(e.getValidationErrors());
            } catch (RuntimeException re) {
                Notification.show(re.getMessage());
            }

        });

        add(header, owner, name, birthDate, type, save);
    }

    private void notifyValidationErrors(List<ValidationResult> validationErrors) {
        UnorderedList errors = new UnorderedList();
        validationErrors.forEach(validationResult -> errors.add(new ListItem(validationResult.getErrorMessage())));
        Notification notification = new Notification(errors);
        notification.open();
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Integer petId) {
        if (petId != null) {
            save.setText("Update");
            pet = petService.findById(petId);
        } else {
            pet = new Pet();
            save.setText("Add Pet");
            Integer ownerId = Integer.valueOf(beforeEvent.getLocation().getQueryParameters().getParameters().get("owner-id").get(0));
            Owner owner = ownerService.findById(ownerId);
            pet.setOwner(owner);
        }

        binder.readBean(pet);
    }
}
