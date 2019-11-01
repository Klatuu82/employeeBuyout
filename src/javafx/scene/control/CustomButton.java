package javafx.scene.control;

import javafx.scene.layout.AnchorPane;

public class CustomButton extends Button {

    public CustomButton(String text, double topAnchor, double leftAnchor) {
        AnchorPane.setTopAnchor(this, topAnchor);
        AnchorPane.setLeftAnchor(this, leftAnchor);
        this.setMinWidth(140.0);
        this.setMaxWidth(140.0);
        if (text != null) {
            this.setText(text);
        }
        this.setId(text);
    }
}
