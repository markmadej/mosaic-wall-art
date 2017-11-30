from PIL import Image
import sys

def main():
    loadImage("testImage.jpg")

def loadImage(filename):
    return Image.open(filename)

def getHeightAndWidthOfImage(image):
    return image.size

def getImagePixels(image):
    return im.load()

def getGridTuplesFromImageAndBoxDimensions(imageDimensions, boxDimensions):
    tuples = []
    currX = 0
    currY = 0

    pass

def getProposedNextBox(boxDimensions, lastBox):
    return (lastBox[0] + boxDim)

if __name__ == '__main__':
    main()
