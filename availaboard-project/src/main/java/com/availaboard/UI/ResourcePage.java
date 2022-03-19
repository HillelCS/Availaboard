package com.availaboard.UI;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
@Route("availaboard/resource/:resourceID?")
public class ResourcePage extends Div implements BeforeEnterObserver {
	private int userID;
	private Resource res;
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		userID = Integer.valueOf(event.getRouteParameters().get("resourceID").stream().filter(x -> x.length() == 1)
				.findFirst().map(Object::toString).orElse(""));
	}
	public ResourcePage() {
		//System.out.println(db.username);
	}
}