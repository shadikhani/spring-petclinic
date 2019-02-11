package org.springframework.samples.petclinic.owner;

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
import com.vaadin.flow.component.button.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.MainLayout;

import java.util.List;


@Route(value = "owner-form", layout = MainLayout.class)
public class OwnerFormView extends VerticalLayout implements HasUrlParameter<Integer> {

    private Div header;
    private TextField firstName;
    private TextField lastName;
    private TextField address;
    private TextField city;
    private TextField telephone;
    private BeanValidationBinder<Owner> binder;
    private Button save;
    private Owner owner;
    private final OwnerService ownerService;
    private Owner findOwner;


    public OwnerFormView(@Autowired OwnerService ownerService) {
        this.ownerService = ownerService;

        header = new Div();
        header.setText("Owner");

        firstName = new TextField("First name");
        lastName = new TextField("Last name");
        address = new TextField("Address");
        city = new TextField("City");
        telephone = new TextField("Telephone");

        binder = new BeanValidationBinder<>(Owner.class);
        binder.bindInstanceFields(this);
        save = new Button("Save");


        add(header, firstName, lastName, address, city, telephone, save);
    }

    private void notifyValidationErrors(List<ValidationResult> validationErrors) {
        UnorderedList errors = new UnorderedList();
        validationErrors.forEach(validationResult -> errors.add(new ListItem(validationResult.getErrorMessage())));
        Notification notification = new Notification(errors);
        notification.setDuration(5000);
        notification.open();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Integer id) {

        if (id == null) {
            owner = new Owner();
            save.setText("Save");
            save.addClickListener(event -> {
                try {
                    binder.writeBean(owner);
                    ownerService.save(owner);
                    Integer ownerId = owner.getId();
                    getUI().get().navigate(OwnerInformationView.class, ownerId);
                    enableFields(true);
                } catch (ValidationException e) {
                    notifyValidationErrors(e.getValidationErrors());
                }
            });
            return;
        }

        findOwner = ownerService.findById(id);
        binder.readBean(findOwner);

        save.setText("Update Owner");

        save.addClickListener(event -> {
            
            try {
                binder.writeBean(owner);
                ownerService.save(owner);
                getUI().get().navigate(OwnerInformationView.class, id);
            } catch (ValidationException e) {
                notifyValidationErrors(e.getValidationErrors());
            }
        });
    }

    private void enableFields(boolean isEnabled) {
        firstName.setReadOnly(isEnabled);
        lastName.setReadOnly(isEnabled);
        address.setReadOnly(isEnabled);
        city.setReadOnly(isEnabled);
        telephone.setReadOnly(isEnabled);
    }
}
