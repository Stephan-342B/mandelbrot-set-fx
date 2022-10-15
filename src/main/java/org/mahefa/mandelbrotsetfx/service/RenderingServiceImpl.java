package org.mahefa.mandelbrotsetfx.service;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Service;

@Service
public class RenderingServiceImpl implements RenderingService {

    private final static int MAX_ITERATION = 80;

    private int width, height;

    // Boundaries
    private double[] real = { -2d, 1d };
    private double[] image = { -1d, 1d };

    @Override
    public void render(Canvas canvas) {
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();

        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                // Convert pixel to real-imaginary plane
                final int value = calculatePoint(
                        real[0] + (1.0 * x / width) * (real[1] - real[0]),
                        image[0] + (1.0 * y / height) * (image[1] - image[0])
                );
                pixelWriter.setColor(x, y, colorMapHsb(value));
            }
        }
    }

    /**
     * Iterate f(z) = z^2 + c | z(n + 1) = zn^2 + c | z(0) = 0
     * Where C is a complex number of the form a + bi (i is the imaginary number)
     * @param x
     * @param y
     * @return
     */
    private int calculatePoint(double x, double y) {
        double cx = x;
        double cy = y;

        int i = 0;

        for(; i < MAX_ITERATION; i++) {
            double nx = (x * x) - (y * y) + cx; // Real part
            double ny = (2 * x * y) + cy; // Imaginary part
            x = nx;
            y = ny;

            // Threshold: |Z|^2 > 4
            if((x * x) + (y * y) > 4) break;
        }

//        return (int) (i + 1 - (Math.log(Math.abs(x)) / Math.log(2)));
        return i;
    }

    private Color colorMapHsb(int i) {
        final int hue = (360 * i / MAX_ITERATION);
        final int saturation = 1;
        final int brightness = (i < MAX_ITERATION) ? 1 : 0;

        return Color.hsb(hue, saturation, brightness);
    }

    private Color colorMap(int value) {
        int i = value % 16;

        if(value < 255 && value > 0) {
            switch (i) {
                case 0:
                    return Color.rgb(66, 30, 15);
                case 1:
                    return Color.rgb(25, 7, 26);
                case 2:
                    return Color.rgb(9, 1, 47);
                case 3:
                    return Color.rgb(4, 4, 73);
                case 4:
                    return Color.rgb(0, 7, 100);
                case 5:
                    return Color.rgb(12, 44, 138);
                case 6:
                    return Color.rgb(24, 82, 177);
                case 7:
                    return Color.rgb(57, 125, 209);
                case 8:
                    return Color.rgb(134, 181, 229);
                case 9:
                    return Color.rgb(211, 236, 248);
                case 10:
                    return Color.rgb(241, 233, 191);
                case 11:
                    return Color.rgb(248, 201, 95);
                case 12:
                    return Color.rgb(255, 170, 0);
                case 13:
                    return Color.rgb(204, 128, 0);
                case 14:
                    return Color.rgb(153, 87, 0);
                case 15:
                    return Color.rgb(106, 52, 3);
                default:
                    return Color.rgb(0, 0, 0);
            }
        } else {
            return Color.rgb(0, 0, 0);
        }
    }
}
