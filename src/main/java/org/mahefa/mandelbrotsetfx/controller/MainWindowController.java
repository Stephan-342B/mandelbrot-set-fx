package org.mahefa.mandelbrotsetfx.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import org.mahefa.mandelbrotsetfx.service.RenderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainWindowController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Label iteration, zoom;

    @Autowired
    private RenderingService renderingService;

    private Region region;

    @FXML
    private void initialize() {
        // Set label
        iteration.setText("Iteration: " + renderingService.getCurrentIteration());
        zoom.setText("Zoom: 0");

        // Draw Mandelbrot Set
        renderingService.init(canvas);
        renderingService.render();

        // Event
        canvas.addEventFilter(MouseEvent.ANY, mouseEvent -> {
            if(mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED && region == null) {
                region = new Region();
                region.setVisible(true);
                region.setTranslateX(mouseEvent.getX());
                region.setTranslateY(mouseEvent.getY());
                region.getStyleClass().add("selection");
                anchorPane.getChildren().add(region);
            }

            if(mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED && (region != null && region.isVisible())) {
                region.setMinWidth((mouseEvent.getX() - region.getTranslateX()));
                region.setMinHeight((mouseEvent.getY() - region.getTranslateY()));
            }

            if(mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED && (region != null && region.isVisible())) {
                // Save the current state
//                canvas.getGraphicsContext2D().save();

                // Zoom on the specified position
                renderingService.zoom(region);

                // Remove selection
                anchorPane.getChildren().remove(region);
                region = null;

                // Update label
                iteration.setText("Iteration: " + renderingService.getCurrentIteration());
            }
        });
    }
}
