package shanepark.foodbox.image;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import shanepark.foodbox.image.domain.ParseRegion;
import shanepark.foodbox.image.domain.ParsedMenu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageParser {

    private final Tesseract tesseract;
    private final int SINGLE_HEIGHT = 346;
    private final int HEADER_HEIGHT = 46;
    private final int GAP_SMALL = 4;
    final int DAY_PER_ROW = 5;
    final int singleWidth = 183;

    public ImageParser(String dataPath) {
        this.tesseract = new Tesseract();
        tesseract.setDatapath(dataPath);
        this.tesseract.setLanguage("kor");
        this.tesseract.setPageSegMode(6);
    }

    public List<ParsedMenu> parse(File file) throws TesseractException, IOException {
        final int marginLeft = 129;
        final int marginTop = 46;
        final int gapBig = 30;

        BufferedImage bufferedImage = ImageIO.read(file);

        List<ParsedMenu> days = new ArrayList<>();
        days.addAll(readFiveDays(bufferedImage, tesseract, marginLeft, marginTop));
        days.addAll(readFiveDays(bufferedImage, tesseract, marginLeft, marginTop + HEADER_HEIGHT + GAP_SMALL + SINGLE_HEIGHT + gapBig));

        return days;
    }

    private BufferedImage preprocessImage(BufferedImage image) {
        try (OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
             Java2DFrameConverter converterToFrame = new Java2DFrameConverter()) {

            Mat mat = converterToMat.convert(converterToFrame.convert(image));
            Mat resizedMat = new Mat();
            opencv_imgproc.resize(mat, resizedMat, new Size(mat.cols() * 2, mat.rows() * 2));
            return converterToFrame.convert(converterToMat.convert(resizedMat));
        }
    }

    private List<ParsedMenu> readFiveDays(BufferedImage image, Tesseract tesseract, int x, int y) throws TesseractException {


        List<ParsedMenu> days = new ArrayList<>();
        ParseRegion region = new ParseRegion(x, y, singleWidth, HEADER_HEIGHT);
        for (int i = 0; i < DAY_PER_ROW; i++) {
            String date = readImagePartHeader(image, tesseract, region);
            days.add(new ParsedMenu(date));
            region.addX(singleWidth + GAP_SMALL);
        }

        region = new ParseRegion(x, y + HEADER_HEIGHT + GAP_SMALL, singleWidth, SINGLE_HEIGHT);
        for (int i = 0; i < DAY_PER_ROW; i++) {
            String menu = readImagePartMenu(image, tesseract, region);
            days.get(i).setMenu(menu);
            region.addX(singleWidth + GAP_SMALL);
        }
        return days;
    }

    private String readImagePartMenu(BufferedImage image, Tesseract tesseract, ParseRegion region) throws TesseractException {
        tesseract.setVariable("tessedit_char_whitelist", "");
        tesseract.setVariable("tessedit_char_blacklist", "0123456789");
        return readImagePart(image, tesseract, region);
    }

    private String readImagePartHeader(BufferedImage image, Tesseract tesseract, ParseRegion region) throws TesseractException {
        tesseract.setVariable("tessedit_char_whitelist", "0123456789년월화수목금/()");
        tesseract.setVariable("tessedit_char_blacklist", "");
        return readImagePart(image, tesseract, region);
    }

    private String readImagePart(BufferedImage image, Tesseract tesseract, ParseRegion region) throws TesseractException {
        BufferedImage subImage = image.getSubimage(region.getX(), region.getY(), region.getWidth(), region.getHeight());
        subImage = preprocessImage(subImage);

        return tesseract.doOCR(subImage)
                .replaceAll("\n\n", "\n");
    }

}
