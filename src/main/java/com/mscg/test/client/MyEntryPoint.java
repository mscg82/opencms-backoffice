package com.mscg.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyEntryPoint implements EntryPoint {
	private TextBox nameBox;
	private Label errorLabel;

	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("main");

		VerticalPanel verticalPanel = new VerticalPanel();
		rootPanel.add(verticalPanel, 0, 0);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);

		Label lblInsertYourName = new Label("Insert your name:");
		horizontalPanel.add(lblInsertYourName);

		nameBox = new TextBox();
		horizontalPanel.add(nameBox);

		Button sendButton = new Button("New button");
		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String name = nameBox.getText();
				if(name != null && name.length() > 0) {
					errorLabel.setVisible(false);
					Window.alert("Hi, " + nameBox.getText());
				}
				else {
					errorLabel.setText("Your name cannot be empty");
					errorLabel.setVisible(true);
				}
			}
		});
		sendButton.setText("Send");
		horizontalPanel.add(sendButton);

		errorLabel = new Label("There errors will be showed");
		errorLabel.setVisible(false);
		errorLabel.setStyleName("gwt-Label errorLabel");
		verticalPanel.add(errorLabel);

		Anchor testLink = new Anchor("New link", "Test_GWT.html");
		verticalPanel.add(testLink);
		verticalPanel.setCellHorizontalAlignment(testLink, HasHorizontalAlignment.ALIGN_CENTER);
	}
}
