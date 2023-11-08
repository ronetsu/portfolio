package fi.tuni.prog3.calc;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Calculator extends Application {

    @Override
    public void start(Stage stage) {

        Label fOLabel = new Label("First operand:");
        TextField fOField = new TextField();

        Label sOLabel = new Label("Second operand:");
        TextField sOField = new TextField();

        Button addButton = new Button();
        addButton.setText("Add");

        Button subButton = new Button();
        subButton.setText("Subtract");

        Button multiButton = new Button();
        multiButton.setText("Multiply");

        Button divideButton = new Button();
        divideButton.setText("Divide");

        Label resultLabel = new Label("Result:");
        TextField result = new TextField();
        result.setPrefWidth(100);
        result.setEditable(false);

        var grid = new GridPane();
        grid.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        grid.setVgap(10); //vertical gap in pixels
        grid.setPadding(new Insets(10, 10, 10, 10));

        // node, columnIndex, rowIndex, columnSpan, rowSpan:
        grid.add(fOLabel, 0, 0, 2, 1);
        grid.add(fOField, 2, 0, 2, 1);
        grid.add(sOLabel, 0, 1, 2, 1);
        grid.add(sOField, 2, 1, 2, 1);

        grid.add(addButton, 0, 2);
        grid.add(subButton, 1, 2);
        grid.add(multiButton, 2, 2);
        grid.add(divideButton, 3, 2);

        grid.add(resultLabel, 0, 3);
        grid.add(result, 2, 3, 2, 1);

        Scene scene = new Scene(grid, 300, 300);
        stage.setTitle("Calculator");
        stage.setScene(scene);

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent addEvent) {
                var a = Double.parseDouble(fOField.getText());
                var b = Double.parseDouble(sOField.getText());
                var r = a + b;
                result.setText(String.format("%.2f", r));
            }
        });

        subButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent subEvent) {
                var a = Double.parseDouble(fOField.getText());
                var b = Double.parseDouble(sOField.getText());
                var r = a - b;
                result.setText(String.format("%.2f", r));
            }
        });

        multiButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent multiEvent) {
                var a = Double.parseDouble(fOField.getText());
                var b = Double.parseDouble(sOField.getText());
                var r = a * b;
                result.setText(String.format("%.2f", r));
            }
        });

        divideButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent divideEvent) {
                var a = Double.parseDouble(fOField.getText());
                var b = Double.parseDouble(sOField.getText());
                if(b != 0) {
                    var r = a / b;
                    result.setText(String.format("%.2f", r));
                }
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

