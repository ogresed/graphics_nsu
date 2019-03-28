package ru.nsu.fit.g16207.melnikov.controller;

import ru.nsu.fit.g16207.melnikov.model.event.FilterApplied;
import ru.nsu.fit.g16207.melnikov.model.filter.*;
import ru.nsu.fit.g16207.melnikov.view.ValueChangedHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FilterController {
    private ValueChangedHandler handler;

    public void setHandler(ValueChangedHandler handler) {
        this.handler = handler;
    }

    private void setChangedImage(Object value) {
        handler.handle(value);
    }

    public void makeFilter(Filter filter, BufferedImage image) {
        setChangedImage(new FilterApplied(filter.process(image)));
    }
    /*
    public void makeFloydSteinbergDithering(BufferedImage oldImage, int nRed, int nGreen, int nBlue) {
        FloydSteinbergDitheringFilter floydSteinbergDitheringFilter =
                new FloydSteinbergDitheringFilter(nRed, nGreen, nBlue);
        BufferedImage newImage = floydSteinbergDitheringFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

   public void makeSharpening(BufferedImage oldImage) {
        SharpeningFilter sharpeningFilter = new SharpeningFilter();
        BufferedImage newImage = sharpeningFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeEmbossing(BufferedImage oldImage) {
        EmbossingFilter embossingFilter = new EmbossingFilter();
        BufferedImage newImage = embossingFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeWatercoloring(BufferedImage oldImage) {
        WatercoloringFilter watercoloringFilter = new WatercoloringFilter();
        BufferedImage newImage = watercoloringFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeMedian(BufferedImage oldImage) {
        MedianFilter medianFilter = new MedianFilter();
        BufferedImage newImage = medianFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeBlur(BufferedImage oldImage) {
        BlurFilter blurFilter = new BlurFilter();
        BufferedImage newImage = blurFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeGammaCorrection(BufferedImage oldImage, double gamma) {
        GammaFilter gammaFilter = new GammaFilter(gamma);
        BufferedImage newImage = gammaFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeRotation(BufferedImage oldImage, int angle) {
        RotationFilter rotationFilter = new RotationFilter(angle);
        BufferedImage newImage = rotationFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeDoubling(BufferedImage oldImage) {
        DoublingFilter doublingFilter = new DoublingFilter();
        BufferedImage newImage = doublingFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeRoberts(BufferedImage oldImage, int threshold) {
        RobertsFilter robertsFilter = new RobertsFilter(threshold);
        BufferedImage newImage = robertsFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }

    public void makeSobel(BufferedImage oldImage, int threshold) {
        SobelFilter sobelFilter = new SobelFilter(threshold);
        BufferedImage newImage = sobelFilter.process(oldImage);
        setChangedImage(new FilterApplied(newImage));
    }
*/
    public void saveImage(File file, BufferedImage image) throws IOException {
        boolean isWritten = ImageIO.write(image, "png", file);
        System.out.println(isWritten);
    }
}
