package org.springframework.samples.petclinic.owner;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.MainLayout;
import org.springframework.samples.petclinic.visit.VisitView;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitService;

import java.util.Collections;
import java.util.List;

@Route(value = "OwnerInformationView", layout = MainLayout.class)
public class OwnerInformationView extends VerticalLayout implements HasUrlParameter<Integer> {

    private Div header;
    private TextField firstName;
    private TextField lastName;
    private TextField name;
    private TextField address;
    private TextField city;
    private TextField telephone;
    private TextField petName;
    private DatePicker petBirthDate;
    private TextField petType;
    private BeanValidationBinder<Owner> binder;
    private Button save;
    private Button editOwnerButton;
    private final OwnerService ownerService;
    private Button addPetButton;
    //private Owner owner;
    private final VisitService visitService;
    private BeanValidationBinder<Visit> visitBinder;
    private VerticalLayout petsLayout;


    @Autowired
    public OwnerInformationView(OwnerService ownerService, VisitService visitService) {
        this.ownerService = ownerService;
        this.visitService = visitService;


        name = new TextField("Name");
        name.setReadOnly(true);
        address = new TextField("Address");
        address.setReadOnly(true);
        city = new TextField("City");
        city.setReadOnly(true);
        telephone = new TextField("Telephone");
        telephone.setReadOnly(true);
        editOwnerButton = new Button("Edit Owner");
//        editOwnerButton.addClickListener(event -> {
//            Integer ownerId = owner.getId();
//            getUI().get().navigate(OwnerFormView.class, ownerId);
//        });

        addPetButton = new Button("Add New Pet");
//        addPetButton.addClickListener(event -> {
//            Integer ownerId = owner.getId();
//            getUI().get().navigate("AddPet", QueryParameters.simple(Collections.singletonMap("owner-id", ownerId.toString())));
//        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(editOwnerButton, addPetButton);

        binder = new BeanValidationBinder<>(Owner.class);
        binder.bindInstanceFields(this);
        binder.bind(name, owner -> owner.getFirstName() + " " + owner.getLastName(), null);

        petsLayout = new VerticalLayout();

        add(name, address, city, telephone, horizontalLayout, petsLayout);

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer ownerId) {
//        sentOwnerId = ownerId;

        Owner owner = ownerService.findById(ownerId);
        binder.readBean(owner);

        editOwnerButton.addClickListener(event -> {
            getUI().get().navigate(OwnerFormView.class, ownerId);
        });

        addPetButton.addClickListener(event -> {
            getUI().get().navigate("AddPet", QueryParameters.simple(Collections.singletonMap("owner-id", ownerId.toString())));
        });

        List<Pet> petList = owner.getPets();

        for (Pet pet : petList) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();

            FormLayout layoutWithFormItems = new FormLayout();

            petName = new TextField();
            petName.setValue(pet.getName());
            layoutWithFormItems.addFormItem(petName, "Name");
            petName.setReadOnly(true);

            petBirthDate = new DatePicker();
            petBirthDate.setValue(pet.getBirthDate());
            layoutWithFormItems.addFormItem(petBirthDate, "Birth Date");
            petBirthDate.setReadOnly(true);

            petType = new TextField();
            petType.setValue(pet.getType().getName());
            layoutWithFormItems.addFormItem(petType, "Type");
            petType.setReadOnly(true);

            VerticalLayout gridVerticalLayout = new VerticalLayout();

            Grid<Visit> grid = new Grid<>();
            grid.setHeightByRows(true);

            Grid.Column<Visit> dateColumn = grid.addColumn(Visit::getDate).setHeader("Visit Date");
            Grid.Column<Visit> descriptionColumn = grid.addColumn(Visit::getDescription).setHeader("Description");

            grid.setItems(pet.getVisits());
            Button editPetbutton = new Button("Edit Pet", event -> {
                Integer petId = pet.getId();
                getUI().get().navigate(PetView.class, petId);
            });

            Button addVisitButton = new Button("Add Visit", event -> {
                Integer petId = pet.getId();
                getUI().get().navigate(VisitView.class, petId);
            });

            HorizontalLayout visitButtonsHorizontalLayout = new HorizontalLayout();
            visitButtonsHorizontalLayout.add(editPetbutton, addVisitButton);

            gridVerticalLayout.add(grid, visitButtonsHorizontalLayout);

            horizontalLayout.add(layoutWithFormItems, gridVerticalLayout);

            petsLayout.add(horizontalLayout);

        }
    }
}



