package org.mahefa.mandelbrotsetfx.service;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

public interface RenderingService {

    void render(Canvas canvas, Label label);

}
