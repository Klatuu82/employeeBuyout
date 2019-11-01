package javafx.scene.control;

import javafx.scene.layout.AnchorPane;

public class CustomTextField extends TextField {

    public CustomTextField(String name, double topAnchor, double leftAnchor, double minWidth, double maxWidth) {
        AnchorPane.setTopAnchor(this, topAnchor);
        AnchorPane.setLeftAnchor(this, leftAnchor);
        if (minWidth != 0) {
            this.setMaxWidth(minWidth);
        }
        if (maxWidth != 0) {
            this.setMinWidth(maxWidth);
        }
        this.setId(name);
    }
}
