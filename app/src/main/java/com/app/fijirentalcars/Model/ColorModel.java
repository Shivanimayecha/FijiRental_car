package com.app.fijirentalcars.Model;

public class ColorModel {
    int ColorCode;
    String ColorName;
    boolean selected=false;

    public int getColorCode() {
        return ColorCode;
    }

    public void setColorCode(int colorCode) {
        ColorCode = colorCode;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
