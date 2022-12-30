/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.javafx.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.parameter.BooleanParameter;
import org.dayflower.parameter.DoubleParameter;
import org.dayflower.parameter.FileParameter;
import org.dayflower.parameter.FloatParameter;
import org.dayflower.parameter.IntParameter;
import org.dayflower.parameter.Parameter;
import org.dayflower.parameter.StringParameter;
import org.dayflower.utility.ParameterArguments;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

final class ParameterDialog extends Dialog<Boolean> {
	public ParameterDialog(final Stage stage, final List<Parameter> parameters) {
		Objects.requireNonNull(stage, "stage == null");
		
		ParameterArguments.requireNonNullList(parameters, "parameters");
		
		final List<Node> nodes = doCreateNodes(parameters);
		
		final
		DialogPane dialogPane = getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setContent(doCreateGridPane(parameters, nodes));
		
		final
		Button button = Button.class.cast(dialogPane.lookupButton(ButtonType.OK));
		button.setOnAction(event -> {
			for(int i = 0; i < parameters.size(); i++) {
				doUpdateParameter(parameters.get(i), nodes.get(i));
			}
		});
		
		initOwner(stage);
		
		setResultConverter(buttonType -> Boolean.valueOf(buttonType.getButtonData() == ButtonType.OK.getButtonData()));
		setTitle("Parameter Dialog");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static CheckBox doCreateCheckBox(final BooleanParameter booleanParameter) {
		final
		CheckBox checkBox = new CheckBox();
		checkBox.setSelected(booleanParameter.getValue());
		
		return checkBox;
	}
	
	private static GridPane doCreateGridPane(final List<Parameter> parameters, final List<Node> nodes) {
		final
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10.0D);
		gridPane.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		gridPane.setVgap(10.0D);
		
		for(int i = 0, j = 0; i < parameters.size(); i++) {
			final Parameter parameter = parameters.get(i);
			
			final Node node = nodes.get(i);
			
			if(parameter != null && node != null) {
				gridPane.add(new Text(parameter.getName()), 0, j);
				gridPane.add(node, 1, j);
				
				j++;
			}
		}
		
		return gridPane;
	}
	
	private static List<Node> doCreateNodes(final List<Parameter> parameters) {
		return parameters.stream().map(parameter -> doCreateNode(parameter)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	private static Node doCreateNode(final Parameter parameter) {
		if(parameter instanceof BooleanParameter) {
			return doCreateCheckBox(BooleanParameter.class.cast(parameter));
		} else if(parameter instanceof DoubleParameter) {
			return doCreateTextField(DoubleParameter.class.cast(parameter));
		} else if(parameter instanceof FileParameter) {
			return null;
		} else if(parameter instanceof FloatParameter) {
			return doCreateTextField(FloatParameter.class.cast(parameter));
		} else if(parameter instanceof IntParameter) {
			return doCreateTextField(IntParameter.class.cast(parameter));
		} else if(parameter instanceof StringParameter) {
			return doCreateTextField(StringParameter.class.cast(parameter));
		} else {
			return null;
		}
	}
	
	private static TextField doCreateTextField(final StringParameter stringParameter) {
		final
		TextField textField = new TextField();
		textField.setText(stringParameter.getValue());
		
		return textField;
	}
	
	private static TextField doCreateTextField(final DoubleParameter doubleParameter) {
		final TextFormatter<Change> textFormatter = new TextFormatter<>(change -> {
			try {
				Double.parseDouble(change.getControlNewText());
				
				return change;
			} catch(@SuppressWarnings("unused") final NumberFormatException e) {
				return null;
			}
		});
		
		final
		TextField textField = new TextField();
		textField.setTextFormatter(textFormatter);
		textField.setText(Double.toString(doubleParameter.getValue()));
		
		return textField;
	}
	
	private static TextField doCreateTextField(final FloatParameter floatParameter) {
		final TextFormatter<Change> textFormatter = new TextFormatter<>(change -> {
			try {
				Float.parseFloat(change.getControlNewText());
				
				return change;
			} catch(@SuppressWarnings("unused") final NumberFormatException e) {
				return null;
			}
		});
		
		final
		TextField textField = new TextField();
		textField.setTextFormatter(textFormatter);
		textField.setText(Float.toString(floatParameter.getValue()));
		
		return textField;
	}
	
	private static TextField doCreateTextField(final IntParameter intParameter) {
		final TextFormatter<Change> textFormatter = new TextFormatter<>(change -> {
			try {
				Integer.parseInt(change.getControlNewText());
				
				return change;
			} catch(@SuppressWarnings("unused") final NumberFormatException e) {
				return null;
			}
		});
		
		final
		TextField textField = new TextField();
		textField.setTextFormatter(textFormatter);
		textField.setText(Integer.toString(intParameter.getValue()));
		
		return textField;
	}
	
	private static void doUpdateBooleanParameter(final BooleanParameter booleanParameter, final CheckBox checkBox) {
		booleanParameter.setValue(checkBox.isSelected());
	}
	
	private static void doUpdateDoubleParameter(final DoubleParameter doubleParameter, final TextField textField) {
		try {
			doubleParameter.setValue(Double.parseDouble(textField.getText()));
		} catch(@SuppressWarnings("unused") final NumberFormatException e) {
//			Do nothing for now.
		}
	}
	
	private static void doUpdateFloatParameter(final FloatParameter floatParameter, final TextField textField) {
		try {
			floatParameter.setValue(Float.parseFloat(textField.getText()));
		} catch(@SuppressWarnings("unused") final NumberFormatException e) {
//			Do nothing for now.
		}
	}
	
	private static void doUpdateIntParameter(final IntParameter intParameter, final TextField textField) {
		try {
			intParameter.setValue(Integer.parseInt(textField.getText()));
		} catch(@SuppressWarnings("unused") final NumberFormatException e) {
//			Do nothing for now.
		}
	}
	
	private static void doUpdateParameter(final Parameter parameter, final Node node) {
		if(parameter instanceof BooleanParameter && node instanceof CheckBox) {
			doUpdateBooleanParameter(BooleanParameter.class.cast(parameter), CheckBox.class.cast(node));
		} else if(parameter instanceof DoubleParameter && node instanceof TextField) {
			doUpdateDoubleParameter(DoubleParameter.class.cast(parameter), TextField.class.cast(node));
		} else if(parameter instanceof FloatParameter && node instanceof TextField) {
			doUpdateFloatParameter(FloatParameter.class.cast(parameter), TextField.class.cast(node));
		} else if(parameter instanceof IntParameter && node instanceof TextField) {
			doUpdateIntParameter(IntParameter.class.cast(parameter), TextField.class.cast(node));
		} else if(parameter instanceof StringParameter && node instanceof TextField) {
			doUpdateStringParameter(StringParameter.class.cast(parameter), TextField.class.cast(node));
		}
	}
	
	private static void doUpdateStringParameter(final StringParameter stringParameter, final TextField textField) {
		stringParameter.setValue(textField.getText());
	}
}