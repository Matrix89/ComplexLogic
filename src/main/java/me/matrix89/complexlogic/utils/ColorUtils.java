package me.matrix89.complexlogic.utils;

public enum ColorUtils {
    WHITE(0, 16383998),
    ORANGE(1, 16351261),
    MAGENTA(2, 13061821),
    LIGHT_BLUE(3, 3847130),
    YELLOW(4, 16701501),
    LIME(5, 8439583),
    PINK(6, 15961002),
    GRAY(7, 4673362),
    SILVER(8, 10329495),
    CYAN(9, 1481884),
    PURPLE(10, 8991416),
    BLUE(11, 3949738),
    BROWN(12, 8606770),
    GREEN(13, 6192150),
    RED(14, 11546150),
    BLACK(15, 1908001);
    int intColor;
    int index;
    private float[] rgbColor = new float[3];
    private float[] hsvColor;
    private float[] argbColor = new float[4];

    ColorUtils(int index, int color) {
        this.index = index;
        intColor = color;
        int b = color & 0xFF;
        int g = (color >> 8) & 0xFF;
        int r = (color >> 16) & 0xFF;
        rgbColor[0] = r;
        rgbColor[1] = g;
        rgbColor[2] = b;
        hsvColor = rgb2hsv(rgbColor);
        argbColor[0] = 1;
        argbColor[1] = r;
        argbColor[2] = g;
        argbColor[3] = b;
    }

    public float[] getHsvColor() {
        return new float[]{hsvColor[0], hsvColor[1], hsvColor[2]};
    }

    public float[] getRgbColor() {
        return new float[]{rgbColor[0], rgbColor[1], rgbColor[2]};
    }

    public float[] getColor(float value) {
        float[] hsv = getHsvColor();
        hsv[2] = value;
        float[] color = hsv2rgb(hsv);
        return new float[]{1, color[0], color[1], color[2]};
    }

    public static float[] rgb2hsv(float[] in) {
        float[] out = new float[3];
        float min, max, delta;

        min = in[0] < in[1] ? in[0] : in[1];
        min = min < in[2] ? min : in[2];

        max = in[0] > in[1] ? in[0] : in[1];
        max = max > in[2] ? max : in[2];

        out[2] = max;
        delta = max - min;
        if (delta < 0.00001) {
            out[1] = 0;
            out[0] = 0;
            return out;
        }
        if (max > 0.0) {
            out[1] = (delta / max);
        } else {

            out[1] = 0.0f;
            out[0] = Float.NaN;
            return out;
        }
        if (in[0] >= max)
            out[0] = (in[1] - in[2]) / delta;
        else if (in[1] >= max)
            out[0] = 2.0f + (in[2] - in[0]) / delta;
        else
            out[0] = 4.0f + (in[0] - in[1]) / delta;

        out[0] *= 60.0;

        if (out[0] < 0.0)
            out[0] += 360.0;

        return out;
    }

    public static float[] hsv2rgb(float[] in) {
        float hh, p, q, t, ff;
        int i;
        float[] out = new float[4];

        if (in[1] <= 0.0) {
            out[0] = in[2];
            out[1] = in[2];
            out[2] = in[2];
            return out;
        }
        hh = in[0];
        if (hh >= 360.0) hh = 0.0f;
        hh /= 60.0;
        i = (int) hh;
        ff = hh - i;
        p = in[2] * (1.0f - in[1]);
        q = in[2] * (1.0f - (in[1] * ff));
        t = in[2] * (1.0f - (in[1] * (1.0f - ff)));

        switch (i) {
            case 0:
                out[0] = in[2];
                out[1] = t;
                out[2] = p;
                break;
            case 1:
                out[0] = q;
                out[1] = in[2];
                out[2] = p;
                break;
            case 2:
                out[0] = p;
                out[1] = in[2];
                out[2] = t;
                break;

            case 3:
                out[0] = p;
                out[1] = q;
                out[2] = in[2];
                break;
            case 4:
                out[0] = t;
                out[1] = p;
                out[2] = in[2];
                break;
            case 5:
            default:
                out[0] = in[2];
                out[1] = p;
                out[2] = q;
                break;
        }
        return out;
    }

}
