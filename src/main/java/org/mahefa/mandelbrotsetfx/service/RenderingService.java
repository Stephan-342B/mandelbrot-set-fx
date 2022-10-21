package org.mahefa.mandelbrotsetfx.service;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

public interface RenderingService {

    void init(Canvas canvas);

    void render();

    void zoom(Region region);

    int getCurrentIteration();

}
