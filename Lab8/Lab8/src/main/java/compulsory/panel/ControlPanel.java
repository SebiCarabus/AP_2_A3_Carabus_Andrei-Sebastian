package compulsory.panel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class ControlPanel extends HBox {
    private Button exitButton;
    private Button resetButton;
    private Button createButton;

    public ControlPanel(){
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
        createButton=new Button("Create");
        resetButton=new Button("Reset");
        exitButton=new Button("Exit");
        exitButton.setOnMouseClicked(event -> {
            System.exit(0);
        });
        this.getChildren().addAll(createButton,resetButton,exitButton);
    }
}
