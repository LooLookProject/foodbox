package shanepark.foodbox.image.ocr.tesseract;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.springframework.stereotype.Component;
import shanepark.foodbox.api.exception.ImageParseException;
import shanepark.foodbox.image.domain.DayRegion;
import shanepark.foodbox.image.domain.ParseRegion;
import shanepark.foodbox.image.domain.ParsedMenu;
import shanepark.foodbox.image.ocr.ImageMarginCalculator;
import shanepark.foodbox.image.ocr.ImageParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageParserTesseract implements ImageParser {

    private final Tesseract tesseract;
    private final ImageMarginCalculator imageMarginCalculator;

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
    public List<ParsedMenu> parse(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            List<DayRegion> regions = imageMarginCalculator.calcParseRegions(bufferedImage);

            List<ParsedMenu> days = new ArrayList<>();
            for (DayRegion region : regions) {
                String date = readImagePartHeader(bufferedImage, tesseract, region.date()).trim();
                String menu = readImagePartMenu(bufferedImage, tesseract, region.menu());

                ParsedMenu parsedMenu = new ParsedMenu(date);
                parsedMenu.setMenu(menu);
                days.add(parsedMenu);
            }
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
        BufferedImage subImage = image.getSubimage(region.x(), region.y(), region.width(), region.height());
        subImage = preprocessImage(subImage);

        return tesseract.doOCR(subImage)
                .replaceAll("\n\n", "\n");
    }

}
