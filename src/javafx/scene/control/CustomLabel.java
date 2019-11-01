package javafx.scene.control;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class CustomLabel extends Label {

    public CustomLabel(String name, double topAnchor, double leftAnchor, double minWidth, double maxWidth, int textSize) {
        this.setFont(new Font("Tahoma", textSize));
        AnchorPane.setTopAnchor(this, topAnchor);
        AnchorPane.setLeftAnchor(this, leftAnchor);
        if (minWidth != 0) {
            this.setMinWidth(minWidth);
        }
        if (maxWidth != 0) {
            this.setMaxWidth(maxWidth);
        }
        if (name != null) {
            this.setText(name);
        }
        this.setId(name);
    }
}
