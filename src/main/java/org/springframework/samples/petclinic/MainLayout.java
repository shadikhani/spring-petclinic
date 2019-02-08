package org.springframework.samples.petclinic;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import org.springframework.samples.petclinic.owner.OwnerListView;
import org.springframework.samples.petclinic.vet.VetView;

import java.util.Objects;


public class MainLayout extends VerticalLayout implements RouterLayout {

    private Div centerContent;



    public MainLayout() {

        setSizeFull();
        Tabs tabs = new Tabs();

        Tab home = new Tab();
        RouterLink routerLink = new RouterLink("Home", HomeView.class);
        home.add(routerLink);

        Tab vet = new Tab();
        RouterLink vetRouterLink = new RouterLink("Veterinarians", VetView.class);
        vet.add(vetRouterLink);

        Tab owner = new Tab();
        RouterLink ownerRouterLink = new RouterLink("Find Owners", OwnerListView.class);
        owner.add(ownerRouterLink);

        tabs.add(home, owner,vet);
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
