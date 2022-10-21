package org.mahefa.mandelbrotsetfx.common;

import javafx.scene.paint.Color;

public class Utilities {

    private static final double redInMax = 255 * 255;
    private static final double blueInMax = Math.sqrt(255);
    private static final int alpha = 0xFF << 24;

    public static double map(double value, double inMin, double inMax, double outMin, double outMax) {
        return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static Color colorHSB(int i, double limit) {
        final double hue = (360 * i / limit);
        final double saturation = 1;
        final double brightness = (i < limit) ? 1 : 0;

        return Color.hsb(hue, saturation, brightness);
    }

    public static Color colorMap(int value) {
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

    public static Color colorMap(float hueFrom, float hueTo, int n, int i, double limit) {
        double hueRange = hueTo - hueFrom;
        double stepHue = hueRange / n;
        double hue = hueFrom + i * stepHue;
        final double saturation = 1;
        final double brightness = (i < limit) ? 1 : 0;

        return Color.hsb(hue, saturation, brightness);
    }

    public static int getArgbColor(int iteration, int limit) {
        double bright = Utilities.map(iteration, 0, limit, 0, 255);

        if(iteration == limit) {
            bright = 0d;
        }

        // Color the current pixel located at the position x and y
        int red = (int) Utilities.map(bright * bright, 0, redInMax, 0, 255);
        int green = (int) bright;
        int blue = (int) Utilities.map(Math.sqrt(bright), 0, blueInMax, 0, 255);

        return alpha | red << 16 | green << 8 | blue;
    }
}
