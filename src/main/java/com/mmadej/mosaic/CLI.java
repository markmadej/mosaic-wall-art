package com.mmadej.mosaic;

import lombok.extern.slf4j.Slf4j;
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
    public String convert(String pathToImage) {
      log.debug("In convert, path is {}.", pathToImage);
      return "Complete!";
    }
}
