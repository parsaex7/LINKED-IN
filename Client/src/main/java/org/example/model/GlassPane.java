package org.example.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.BoxBlur;

public class GlassPane extends StackPane {

    public GlassPane() {
        Rectangle glass = new Rectangle();
        glass.setFill(Color.color(0, 0, 0, 0.3));

        // Adjust the size of the glass pane
        glass.widthProperty().bind(this.widthProperty());
        glass.heightProperty().bind(this.heightProperty());

        // Add a blur effect
        BoxBlur blur = new BoxBlur();
        blur.setWidth(10);
        blur.setHeight(10);
        blur.setIterations(3);

        this.setPickOnBounds(false);
        glass.setMouseTransparent(true);
        this.getChildren().add(glass);
        this.setEffect(blur);
    }
}