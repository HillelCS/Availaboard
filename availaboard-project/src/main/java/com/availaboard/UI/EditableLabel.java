package com.availaboard.UI;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Used to create a Label that is editable on a double click
 *
 */
public class EditableLabel {



	public EditableLabel(HorizontalLayout layout) {
		String content;
		Span label = new Span(content);
		TextField textField = new TextField();
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
			content = textField.getValue();
			if (content.isEmpty()) {
				layout.remove(label, textField);
			} else {
				label.setText(content);
			}
		});

		textField.addBlurListener(e -> {
			textField.setVisible(false);
			label.setVisible(true);
		});

		layout.add(label, textField);
	}
}
