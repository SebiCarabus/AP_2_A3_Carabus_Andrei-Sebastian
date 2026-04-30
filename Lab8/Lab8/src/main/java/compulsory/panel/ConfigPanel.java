package compulsory.panel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class ConfigPanel extends HBox {
    private Spinner<Integer> dimensionSpinner;
    private Button drawButton;

    public ConfigPanel(){
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
        Label dimensionLabel=new Label("Dimension: ");
        this.dimensionSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory= new SpinnerValueFactory.IntegerSpinnerValueFactory(2,10,5);
        this.dimensionSpinner.setValueFactory(valueFactory);
        this.dimensionSpinner.setEditable(true);
        this.drawButton= new Button("Draw");
        this.getChildren().addAll(dimensionLabel,dimensionSpinner,drawButton);
    }

    public int getDimension() {
        return this.dimensionSpinner.getValue();
    }
}
