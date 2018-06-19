package com.mmadej.mosaic;

import lombok.extern.slf4j.Slf4j;

import java.awt.Color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@ShellComponent
public class CLI {

    @Autowired
    public CLI() {
    }

    @ShellMethod("Convert image to mosaic.")
    public String convert(int width, int height) throws Exception {
      String pathToImage = "/Users/mmadej/workspace/mosaic-wall-art/src/main/resources/example.jpg";
      log.debug("In convert, path is {}, width {}, height {}.", pathToImage, width, height);
      MosaicImage image = new MosaicImage.Builder()
        .withPath(pathToImage)
        .withGridWidth(width)
        .withGridHeight(height)
        .build();
      /*image.useColorPalette(
        Color.YELLOW,
        Color.BLACK,
        Color.RED,
        Color.PINK,
        Color.GREEN,
        Color.BLUE,
        Color.WHITE
      );*/
      image.generateImage();
      image.saveImageToFile("");
      return "Complete!";
    }
}
