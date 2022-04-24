package com.availaboard.UI;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Used to create a Label that is editable on a double click
 *
 */
public class EditableLabel extends VerticalLayout {

	private String content;
	private Span label = new Span(content);
	TextField textField = new TextField();

	public EditableLabel(String content) {
		this.content = content;
		textField.setValue(content);
		textField.setVisible(false);

		label.getElement().addEventListener("dblclick", e -> {
			label.setVisible(false);
			textField.setVisible(true);
			textField.focus();
		});

		textField.addValueChangeListener(e -> {
			textField.setVisible(false);
			label.setVisible(true);
			this.content = textField.getValue();
			if (this.content.isEmpty()) {
				remove(label, textField);
			} else {
				label.setText(this.content);
			}
		});

		textField.addBlurListener(e -> {
			textField.setVisible(false);
			label.setVisible(true);
		});

		add(label, textField);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
