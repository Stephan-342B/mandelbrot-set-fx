package org.mahefa.mandelbrotsetfx.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import org.mahefa.mandelbrotsetfx.service.RenderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainWindowController {

    @Autowired
    private RenderingService renderingService;

    @FXML
    AnchorPane anchorPane;

    @FXML
    Canvas canvas;

    @FXML
    Label iteration, zoom;

    @FXML
    private void initialize() {
        zoom.setText("Zoom: 0");
        renderingService.render(canvas, iteration);

        // Event
        canvas.addEventHandler(ScrollEvent.SCROLL, event -> {
            canvas.setScaleX(canvas.getScaleX() + event.getDeltaX());
            canvas.setScaleY(canvas.getScaleY() + event.getDeltaY());
        });
    }

    private Slider prepareSlider() {
        Slider slider = new Slider();
        slider.setMax(1500);
        slider.setMin(10);
        slider.setPrefWidth(300d);

        return slider;
    }
}
