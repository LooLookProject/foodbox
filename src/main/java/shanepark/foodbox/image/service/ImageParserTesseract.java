package shanepark.foodbox.image.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.springframework.stereotype.Component;
import shanepark.foodbox.api.exception.ImageParseException;
import shanepark.foodbox.image.domain.ImageMarginData;
import shanepark.foodbox.image.domain.ParseRegion;
import shanepark.foodbox.image.domain.ParsedMenu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageParserTesseract implements ImageParser {

    private final Tesseract tesseract;
    private final ImageMarginCalculator imageMarginCalculator;
    final int DAY_PER_ROW = 5;

    public ImageParserTesseract(ImageMarginCalculator imageMarginCalculator) {
        this.imageMarginCalculator = imageMarginCalculator;

        this.tesseract = new Tesseract();
        this.tesseract.setDatapath(getDataPathFromOs());
        this.tesseract.setLanguage("kor");
        this.tesseract.setPageSegMode(6);
    }

    private String getDataPathFromOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return "/opt/homebrew/share/tessdata";
        }
        if (os.contains("win")) {
            return "C:\\Program Files\\Tesseract-OCR\\tessdata";
        }
        if (os.contains("linux")) {
            return "/usr/share/tesseract-ocr/5/tessdata";
        }
        throw new RuntimeException("Unsupported OS");
    }

    @Override
    public List<ParsedMenu> parse(InputStream inputStream) {
        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ImageMarginData marginData = imageMarginCalculator.calcMargin(bufferedImage);

            int x = marginData.marginLeft();
            int y = marginData.marginTop();
            List<ParsedMenu> days = new ArrayList<>(readFiveDays(bufferedImage, tesseract, x, y, marginData));

            y = marginData.marginTop() + marginData.headerHeight() + marginData.gapSmall() + marginData.singleHeight() + marginData.gapBig();
            days.addAll(readFiveDays(bufferedImage, tesseract, x, y, marginData));

            return days;
        } catch (IOException | TesseractException e) {
            throw new ImageParseException(e);
        }
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

    private List<ParsedMenu> readFiveDays(BufferedImage image, Tesseract tesseract, int x, int y, ImageMarginData marginData) throws TesseractException {
        List<ParsedMenu> days = new ArrayList<>();
        ParseRegion region = new ParseRegion(x, y, marginData.singleWidth(), marginData.headerHeight());
        for (int i = 0; i < DAY_PER_ROW; i++) {
            String date = readImagePartHeader(image, tesseract, region).trim();
            days.add(new ParsedMenu(date));
            region.addX(marginData.singleWidth() + marginData.gapSmall());
        }

        region = new ParseRegion(x, y + marginData.headerHeight() + marginData.gapSmall(), marginData.singleWidth(), marginData.singleHeight());
        for (int i = 0; i < DAY_PER_ROW; i++) {
            String menu = readImagePartMenu(image, tesseract, region);
            days.get(i).setMenu(menu);
            region.addX(marginData.singleWidth() + marginData.gapSmall());
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
