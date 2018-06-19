package com.mmadej.mosaic;

import java.awt.image.BufferedImage;
import java.awt.Color;

public interface IMosaicImage {

    public Color getColorAt(int x, int y) throws Exception;

    public int getGridWidth() throws Exception;

    public int getGridHeight() throws Exception;

    public void setGridSize(int width, int height) throws Exception;

    public void useColorPalette(Color... colors) throws Exception;

    public BufferedImage generateImage() throws Exception;

    public void saveImageToFile(String path) throws Exception;
}