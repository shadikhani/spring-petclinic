package org.springframework.samples.petclinic;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLayout;
import org.springframework.samples.petclinic.owner.OwnerListView;
import org.springframework.samples.petclinic.vet.VetView;

import java.util.*;

public class MainLayout extends VerticalLayout implements RouterLayout {

    private Div centerContent;


    public MainLayout() {

        setSizeFull();
        Tab tab1 = new Tab("Home");
        Tab tab2 = new Tab("Veterinarians");
        Tab tab3 = new Tab("Find Owners");

        Tabs tabs = new Tabs(tab1, tab2, tab3);

        tabs.addSelectedChangeListener(event -> {

            if (tabs.getSelectedTab().getLabel().equals("Home")) {
                getUI().get().navigate(HomeView.class);
            } else if (tabs.getSelectedTab().getLabel().equals("Veterinarians")) {
                getUI().get().navigate(VetView.class);
            } else if (tabs.getSelectedTab().getLabel().equals("Find Owners")) {
                getUI().get().navigate(OwnerListView.class);
            }
        });

        add(tabs);

        centerContent = new Div();
        centerContent.setSizeFull();
        add(centerContent);

        Image image = new Image(
            "frontend/img/Vaadin.png", "Logo");

        add(image);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        if (content != null) {
            centerContent.removeAll();
            centerContent.getElement().appendChild(Objects.requireNonNull(content.getElement()));
        }
    }
}
