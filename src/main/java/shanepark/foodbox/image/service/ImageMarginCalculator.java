package shanepark.foodbox.image.service;

import org.springframework.stereotype.Component;
import shanepark.foodbox.image.domain.ImageMarginData;

import java.awt.image.BufferedImage;

@Component
public class ImageMarginCalculator {

    public ImageMarginData calcMargin(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        Point leftTop = findLeftTop(image, width, height);
        int marginTop = leftTop.y;
        int marginLeft = leftTop.x;

        int singleWidth = calcSingleWidth(image, leftTop, width);
        int gapSmall = calcGapSmall(image, leftTop, singleWidth, width);
        int headerHeight = calcHeaderHeight(image, leftTop, height);
        int singleHeight = calcSingleHeight(image, leftTop, headerHeight, height);
        int gapBig = calcGapBig(image, height, leftTop);

        return new ImageMarginData(
                marginLeft,
                marginTop,
                singleWidth,
                singleHeight,
                headerHeight,
                gapSmall,
                gapBig
        );
    }

    private int calcGapBig(BufferedImage image, int height, Point leftTop) {
        int y = leftTop.y;
        while (y < height - 1 && isNotBackgroundColor(image.getRGB(leftTop.x, y))) {
            y++;
        }
        while (y < height - 1 && isBackgroundColor(image.getRGB(leftTop.x, y))) {
            y++;
        }
        while (y < height - 1 && isNotBackgroundColor(image.getRGB(leftTop.x, y))) {
            y++;
        }
        int menuEndY = y;
        while (y < height - 1 && isBackgroundColor(image.getRGB(leftTop.x, y))) {
            y++;
        }
        if (y == height - 1) {
            throw new RuntimeException("Cannot find gap big");
        }
        return y - menuEndY;
    }

    private int calcSingleHeight(BufferedImage image, Point leftTop, int headerHeight, int height) {
        int y = leftTop.y + headerHeight;
        while (isBackgroundColor(image.getRGB(leftTop.x, y))) {
            y++;
        }
        int menuStartY = ++y;
        while (isNotBackgroundColor(image.getRGB(leftTop.x, y))) {
            y++;
        }
        return y - menuStartY;
    }

    private int calcHeaderHeight(BufferedImage image, Point leftTop, int height) {
        for (int y = leftTop.y; y < height; y++) {
            if (isBackgroundColor(image.getRGB(leftTop.x, y))) {
                return y - leftTop.y;
            }
        }
        throw new RuntimeException("Cannot find header height");
    }

    private int calcGapSmall(BufferedImage image, Point leftTop, int singleWidth, int width) {
        for (int x = leftTop.x + singleWidth; x < width; x++) {
            if (isNotBackgroundColor(image.getRGB(x, leftTop.y))) {
                return x - leftTop.x - singleWidth;
            }
        }
        throw new RuntimeException("Cannot find gap small");
    }

    private int calcSingleWidth(BufferedImage image, Point leftTop, int width) {
        for (int x = leftTop.x; x < width; x++) {
            if (isBackgroundColor(image.getRGB(x, leftTop.y))) {
                return x - leftTop.x;
            }
        }
        throw new RuntimeException("Cannot find single width");
    }

    private Point findLeftTop(BufferedImage image, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isNotBackgroundColor(image.getRGB(x, y))) {
                    return new Point(x, y);
                }
            }
        }
        throw new RuntimeException("Cannot find left top point");
    }

    private boolean isBackgroundColor(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        return red == 255 && green == 255 && blue == 255;
    }

    private boolean isNotBackgroundColor(int rgb) {
        return !isBackgroundColor(rgb);
    }

    record Point(
            int x, int y
    ) {
    }

}
