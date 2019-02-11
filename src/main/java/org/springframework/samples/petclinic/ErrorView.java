package org.springframework.samples.petclinic;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

/**
 * View shown when trying to navigate to a view that does not exist using
 */
@Route(value ="error", layout = MainLayout.class)
@ParentLayout(MainLayout.class)
public class ErrorView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    private Span explanation;

    public ErrorView() {
        Image image = new Image("frontend/img/pets.png","Pets");
        H2 header = new H2("Something happened...");
        add(image,header);

        explanation = new Span();
        add(explanation);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        explanation.setText("Could not navigate to '"
            + event.getLocation().getPath() + "'.");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
