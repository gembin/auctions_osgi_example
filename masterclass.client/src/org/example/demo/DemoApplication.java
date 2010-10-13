package org.example.demo;

import java.util.Date;

import aQute.bnd.annotation.component.Component;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@Component(factory = "com.vaadin.Application/demo")
public class DemoApplication extends Application {

	@Override
	public void init() {
		Window window = new Window("Demo Application");
		setMainWindow(window);

		final Button button = new Button("Hello world");
		window.addComponent(button);

		button.addListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				button.setCaption("It's " + new Date());
			}

		});
	}

}
