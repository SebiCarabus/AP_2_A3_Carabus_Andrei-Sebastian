package homework.panel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class ControlPanel extends HBox {
    private Button exitButton;
    private Button resetButton;
    private Button validateButton;
    private Button createButton;
    private Button saveButton;
    private Button loadButton;

    public ControlPanel(){
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
        createButton=new Button("Create");
        resetButton=new Button("Reset");
        validateButton=new Button("Validate");
        loadButton=new Button("Load");
        saveButton=new Button("Save");
        exitButton=new Button("Exit");
        exitButton.setOnMouseClicked(event -> {
            System.exit(0);
        });
        this.getChildren().addAll(createButton,resetButton,validateButton,loadButton,saveButton,exitButton);
    }
}
