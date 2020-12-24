package com.lxkj.shortvideo.utils;

import android.graphics.Color;

public class ColorUtil {
    /**
     * Parse the color string, and return the corresponding color-int. If the
     * string cannot be parsed, return 0. Supported formats are: <br />
     * #RRGGBB #AARRGGBB <br />
     * 'red', 'blue', 'green', 'black', 'white', 'gray', 'cyan', 'magenta',
     * 'yellow', 'lightgray', 'darkgray', 'grey', 'lightgrey', 'darkgrey',
     * 'aqua', 'fuschia', 'lime', 'maroon', 'navy', 'olive', 'purple', 'silver',
     * 'teal'.
     */
    public static int genColor(final String color) {
        if (!StringUtil.isEmpty(color)) {
            try {
                return Color.parseColor(color);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static int genColor(final String color, final String defColor) {
        final int c = genColor(color);
        return (c >= 0) ? c : genColor(defColor);
    }

    public static int genColor(final String color, final int defColor) {
        final int c = genColor(color);
        return (c >= 0) ? c : defColor;
    }

}