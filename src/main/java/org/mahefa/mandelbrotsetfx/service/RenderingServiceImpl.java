package org.mahefa.mandelbrotsetfx.service;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.Region;
import org.mahefa.mandelbrotsetfx.common.Boundary;
import org.mahefa.mandelbrotsetfx.common.Utilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

/**
 * Fix color
 * Zoom on selected region
 * Improve performance (pixelWriter, matrix)
 */
@Service
public class RenderingServiceImpl implements RenderingService {

    @Value("${max.iteration}")
    private int maxIteration;

    @Value("${zoom.value}")
    private int zoom;

    private Canvas canvas;
    private int width, height;

    // Image boundaries on real and imaginary axis
    private Boundary real = new Boundary(-2.0, 1.0);
    private Boundary image = new Boundary(-1.2, 0.0);

    private Double centerX, centerY;
    private double scaleX, scaleY, shift, scaleMultiplier = 1.0;

    @Override
    public void init(Canvas canvas) {
        this.canvas = canvas;
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();

//        selectionX = new Boundary(0, width);
//        selectionY = new Boundary(0, height);

        image.setMax((image.getMin() + (real.getMax() - real.getMin())) * height / width);

        centerX = (real.getMin() + real.getMax()) / 2;
        centerY = (image.getMin() + image.getMax()) / 2;
    }

    @Override
    public void render() {
        scaleX = (real.getMax() - real.getMin()) * scaleMultiplier;
        scaleY = (image.getMax() - image.getMin()) * scaleMultiplier;
        shift = (real.getMin() + real.getMax()) / 2;

        // Clear canvas
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Pixel writer
        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
        int[] pixels = new int[width * height];

        // For each pixel: convert it to real-imaginary plane
        IntStream.iterate(0, x -> x + 1).limit(width).parallel().forEach(x -> {
            IntStream.iterate(0, y -> y + 1).limit(height).parallel().forEach(y -> {
                // Mandelbrot Set
                final double cx = centerX + ((1.0 * x / width + shift) * scaleX);
                final double cy = centerY + ((1.0 * y / height + shift) * scaleY);
                final int value = calculatePoint(cx, cy);

                // Set color to the current pixel
                pixels[(x % width) + (y * width)] = Utilities.getArgbColor(value, maxIteration);
            });
        });

        pixelWriter.setPixels(0, 0, width, height, format, pixels, 0, width);
    }

    @Override
    public void zoom(Region region) {
        // Get center position
        final double centerX = region.getTranslateX() + region.getWidth() / 2;
        final double centerY = region.getTranslateY() + region.getHeight() / 2;

        // Get selection range
//        double regionX = region.getTranslateX();
//        double regionY = region.getTranslateY();
//        double regionWidth = region.getWidth();
//        double regionHeight = region.getHeight();
//
//        selectionX = new Boundary(regionX, regionX + regionWidth);
//        selectionY = new Boundary(regionY, regionY + regionHeight);

        // Set the current mouse position to the boundaries
        this.centerX = Utilities.map(centerX,0d, width, real.getMin(), real.getMax());
        this.centerY = Utilities.map(centerY,0d, height, image.getMin(), image.getMax());

        this.maxIteration *= 8.5;
        this.scaleMultiplier *= 0.005;

        render();
    }

    @Override
    public int getCurrentIteration() {
        return maxIteration;
    }

    /**
     * Iterate f(z) = z^2 + c | z(n + 1) = zn^2 + c | z(0) = 0
     * Where C is a complex number of the form a + bi ('i' is the imaginary number)
     *
     * @param x
     * @param y
     * @return
     */
    private int calculatePoint(double x, double y) {
        double cx = x;
        double cy = y;

        int i = 0;

        for(; i < maxIteration; i++) {
            // Formulae of the complex number: a^2 - ib^2 + 2ab
            final double xSquare = x * x;
            final double ySquare = y * y;
            final double nx = (xSquare - ySquare) + cx; // Real part
            final double ny = (2d * x * y) + cy; // Imaginary part
            x = nx;
            y = ny;

            /**
             * Threshold: |Z|^2 > 4 or |z| > 2
             * With |z| = sqrt(x^2 + y^2)
             */
            if(xSquare + ySquare > 4)
                break;
        }

        return i;
    }
}
