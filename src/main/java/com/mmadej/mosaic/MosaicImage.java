package com.mmadej.mosaic;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Point;

import java.io.IOException;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jline.terminal.impl.jna.freebsd.CLibrary.winsize;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;

@Slf4j
public class MosaicImage {

    @Getter
    private BufferedImage sourceImage;

    @Getter
    private int sourceHeight;  // Height in pixels of the source image.

    @Getter
    private int sourceWidth;   // Width in pixels of the source image.

    @Getter
    private BufferedImage resultImage;

    @Getter
    private int gridWidth;  // Number of grid locations wide.

    @Getter
    private int gridHeight;  // Number of grid locations tall.

    private int gridPixelWidth;  // Width in pixels of each individual grid box.
    private int gridPixelHeight; // Height in pixels of each individual grid box.

    private Set<Color> colorPalette;

    private MosaicImage(final String pathToFile, int width, int height) throws Exception {
        loadImage(pathToFile);
        setGridSize(width, height);
    }

    /**
     * recalcGrid should be called whenever the height/width or grid height/width have changed.
     * This will calculate the height and width of each grid cell.
     */
    private void recalcGrid() {
        gridPixelHeight = sourceHeight / gridHeight;
        gridPixelWidth = sourceWidth / gridWidth;
        log.debug("Source {}x{}, grid {}x{}, box width={}, box height={}", 
            sourceWidth, 
            sourceHeight, 
            gridWidth, 
            gridHeight,
            gridPixelWidth,
            gridPixelHeight);
    }

    public Color getColorAt(int x, int y) throws Exception {
        throw new Exception("Not implemented.");
    }

    public void setGridSize(int width, int height) throws Exception {
        this.gridWidth = width;
        this.gridHeight = height;
        recalcGrid();
    }

    public void useColorPalette(Color... colors) throws Exception {
        colorPalette = new HashSet<Color>(Arrays.asList(colors));
    }

    public void generateImage() throws Exception {
        int countdown = gridWidth * gridHeight;
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                Color c = getAverageColorForGridLocation(x, y);
                Color gridColor = getClosestPaletteColor(c);
                setColorForGrid(gridColor, x, y);
                countdown = countdown - 1;
                log.warn("{} operations remaining...", countdown);
            }
        }
    }

    private Color getAverageColorForGridLocation(int x, int y) {
        Set<Point> points = getPointsForGridLocation(x, y);
        int r = 0;
        int g = 0;
        int b = 0;
        int pointCount = 0;
        for (Point p : points) {
            Color pointColor = new Color(sourceImage.getRGB((int) p.getX(), (int) p.getY()), true);
            r += pointColor.getRed();
            g += pointColor.getGreen();
            b += pointColor.getBlue();
            pointCount++;
        }
        return new Color(r / pointCount, g / pointCount, b / pointCount);
    }

    private Set<Point> getPointsForGridLocation(int x, int y) {
        //0, 0 would be 0, 0 through gridPixelWidth / gridPixelHeight
        //2, 3 woudl be 2xGPH/3xGPH through 3xGPH/4xGPH
        Set<Point> points = new HashSet<>();
        for (int xCoord = x * gridPixelWidth; xCoord < (x+1) * gridPixelWidth; xCoord++) {
            for (int yCoord = y * gridPixelHeight; yCoord < (y+1) * gridPixelHeight; yCoord++) {
                Point p = new Point(xCoord, yCoord);
                points.add(p);
            }
        }
        return points;
    }

    /**
     * setColorForGrid sets the given color in each pixel that falls within 
     * the grid for the x/y coordinates in the result image.
     */
    private void setColorForGrid(Color gridColor, int x, int y) {
        Set<Point> points = getPointsForGridLocation(x, y);
        for (Point p : points) {
            resultImage.setRGB((int) p.getX(), (int) p.getY(), gridColor.getRGB());
        }
    }

    private int colorDiff(Color a, Color b) {
        return Math.abs(a.getRed() - b.getRed()) +
                    Math.abs(a.getBlue() - b.getBlue()) +
                    Math.abs(a.getGreen() - b.getGreen());
    }

    private Color getClosestPaletteColor(Color c) {
        if (colorPalette == null) {
            return c;
        }
        Color returnColor = null;
        int totalDifference = 0;
        for (Color potentialColor : colorPalette) {
            if (returnColor == null) {
                totalDifference = colorDiff(c, potentialColor);
                returnColor = potentialColor;
            } else {
                int newColorDiff = colorDiff(c, potentialColor);
                if (newColorDiff < totalDifference) {
                    totalDifference = newColorDiff;
                    returnColor = potentialColor;
                }
            }
        }
        return returnColor;
    }

    public void saveImageToFile(String path) throws Exception {
        log.warn("The given path is ignored  Using output.png in this directory.");
        File outputfile = new File("output.png");
        ImageIO.write(resultImage, "png", outputfile);
        log.info("Wrote output file to {}.", outputfile.getAbsolutePath());
    }

    private void loadImage(final String filename) throws Exception {
        File f = new File(filename);
        sourceImage = ImageIO.read(f);
        sourceHeight = sourceImage.getHeight();
        sourceWidth = sourceImage.getWidth();

        resultImage = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_INT_RGB);
    }

    @NoArgsConstructor
    public static class Builder {

        private String path;
        private int width;
        private int height;

        public Builder withPath(final String pathToFile) {
            this.path = pathToFile;
            return this;
        }

        public Builder withGridWidth(final int gridWidth) {
            this.width = gridWidth;
            return this;
        }

        public Builder withGridHeight(final int gridHeight) {
            this.height = gridHeight;
            return this;
        }

        public MosaicImage build() throws Exception {
            return new MosaicImage(path, width, height);
        }
    }
}