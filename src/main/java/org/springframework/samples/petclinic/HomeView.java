package org.springframework.samples.petclinic;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@RouteAlias(value = "", layout = MainLayout.class)
@Route(value = "Home", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    public HomeView() {

        Div div = new Div();
        div.setText("Welcome");
        Image image = new Image("frontend/img/pets.png", "Pets");
        add(div, image);
    }
}



