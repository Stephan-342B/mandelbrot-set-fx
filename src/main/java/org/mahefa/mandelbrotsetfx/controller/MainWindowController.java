package org.mahefa.mandelbrotsetfx.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import org.mahefa.mandelbrotsetfx.service.RenderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainWindowController {

    @Autowired
    private RenderingService renderingService;

    @FXML
    Canvas canvas;

    private double[] real = { -2d, 1d };
    private double[] image = { -1d, 1d };

    @FXML
    private void initialize() {
        renderingService.render(canvas);

        // Event
        canvas.addEventHandler(ScrollEvent.SCROLL, event -> {
            double cr = real[0] + (real[1] - real[0]) * event.getMultiplierX() / event.getDeltaX();
            double ci = image[0] + (image[1] - image[0]) * event.getMultiplierX() / event.getDeltaX();

            double tmpMinR = cr - (real[1] - real[0]) / 2 / 0.5;
            real[1] = cr + (real[1] - real[0]) / 2 / 0.5;
            real[0] = tmpMinR;

            double tmpMinI = ci - (image[1] - image[0]) / 2 / 0.5;
            image[1] = ci + (image[1] - image[0]) / 2 / 0.5;
            image[0] = tmpMinI;
        });
    }
}
