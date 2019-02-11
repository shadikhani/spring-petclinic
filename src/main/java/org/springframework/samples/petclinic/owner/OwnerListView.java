package org.springframework.samples.petclinic.owner;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.MainLayout;

import java.util.Collection;

@Route(value = "owner-list", layout = MainLayout.class)
public class OwnerListView extends VerticalLayout {
    private final OwnerService ownerService;
    private Button addButton;

    @Autowired
    public OwnerListView(OwnerService service) {

        this.ownerService = service;
        H3 header = new H3("Find Owners");


        TextField lastNameField = new TextField("Last Name");
        Grid<Owner> grid = new Grid<>();

        grid.addColumn(owner -> owner.getFirstName() + " " + owner.getLastName()).setHeader("Name");
        grid.addColumn(Owner::getAddress).setHeader("Address");
        grid.addColumn(Owner::getCity).setHeader("Header");
        grid.addColumn(Owner::getTelephone).setHeader("Telephone");
        grid.addColumn(Owner::getPets).setHeader("Pets");
        grid.addColumn(new NativeButtonRenderer<>("View", item -> {
            getUI().get().navigate(OwnerInformationView.class, item.getId());
        }));

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        Label message = new Label();

        Button filterButton = new Button("Find Owner");
        filterButton.addClickListener(event -> {
            Collection<Owner> list = service.findByLastName(lastNameField.getValue().toLowerCase());
            if (list.isEmpty()) {
                message.setText("has not been found");
            } else {
                message.setText(null);
            }
            grid.setItems(list);
            grid.getDataProvider().refreshAll();
        });

        addButton = new Button("Add Owner");
        addButton.addClickListener(event -> getUI().get().navigate(OwnerFormView.class));

        add(header, lastNameField, message, filterButton, grid, addButton);
    }
}
