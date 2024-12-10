//package shanepark.foodbox.image.service;
//
//import net.sourceforge.tess4j.TesseractException;
//import org.bytedeco.javacv.Java2DFrameConverter;
//import org.bytedeco.javacv.OpenCVFrameConverter;
//import org.bytedeco.opencv.global.opencv_imgproc;
//import org.bytedeco.opencv.opencv_core.Mat;
//import org.bytedeco.opencv.opencv_core.Size;
//import org.springframework.stereotype.Component;
//import shanepark.foodbox.api.exception.ImageParseException;
//import shanepark.foodbox.image.domain.ImageMarginData;
//import shanepark.foodbox.image.domain.ParseRegion;
//import shanepark.foodbox.image.domain.ParsedMenu;
//import shanepark.foodbox.ocr.OCRConfig;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Component
//public class ImageParserV3 {
//
//    private final ImageMarginCalculator imageMarginCalculator;
//    final int DAY_PER_ROW = 5;
//
//    public ImageParserV3(ImageMarginCalculator imageMarginCalculator) {
//        this.imageMarginCalculator = imageMarginCalculator;
//    }
//
//    public List<ParsedMenu> parse(InputStream inputStream, OCRConfig ocrConfig) {
//        try {
//            BufferedImage bufferedImage = ImageIO.read(inputStream);
//            ImageMarginData marginData = imageMarginCalculator.calcMargin(bufferedImage);
//
//            int x = marginData.marginLeft();
//            int y = marginData.marginTop();
//            List<ParsedMenu> days = new ArrayList<>(readFiveDays(bufferedImage, x, y, marginData,ocrConfig));
//
//            y = marginData.marginTop() + marginData.headerHeight() + marginData.gapSmall() + marginData.singleHeight() + marginData.gapBig();
//            days.addAll(readFiveDays(bufferedImage, x, y, marginData,ocrConfig));
//
//            return days;
//        } catch (IOException | TesseractException e) {
//            throw new ImageParseException(e);
//        }
//
//    }
//
//
//    private List<ParsedMenu> readFiveDays(BufferedImage image, int x, int y, ImageMarginData marginData,OCRConfig ocrConfig) throws TesseractException, IOException {
//        List<ParsedMenu> days = new ArrayList<>();
//        ParseRegion region = new ParseRegion(x, y, marginData.singleWidth(), marginData.headerHeight());
//        for (int i = 0; i < DAY_PER_ROW; i++) {
//            String date = readImagePartHeader(image, region, ocrConfig).trim();
//            days.add(new ParsedMenu(date));
//            region.addX(marginData.singleWidth() + marginData.gapSmall());
//        }
//
//        region = new ParseRegion(x, y + marginData.headerHeight() + marginData.gapSmall(), marginData.singleWidth(), marginData.singleHeight());
//        for (int i = 0; i < DAY_PER_ROW; i++) {
//            String menu = readImagePartMenu(image, region, ocrConfig);
//            days.get(i).setMenu(menu);
//            region.addX(marginData.singleWidth() + marginData.gapSmall());
//        }
//        return days;
//    }
//
//    private String readImagePartMenu(BufferedImage image, ParseRegion region,OCRConfig ocrConfig) throws TesseractException, IOException {
////        tesseract.setVariable("tessedit_char_whitelist", "");
////        tesseract.setVariable("tessedit_char_blacklist", "0123456789");
//        return readImagePartWithOCR(image, region, ocrConfig);
//    }
//
//    private String readImagePartHeader(BufferedImage image,ParseRegion region,OCRConfig ocrConfig) throws TesseractException, IOException {
////        tesseract.setVariable("tessedit_char_whitelist", "0123456789년월화수목금/()");
////        tesseract.setVariable("tessedit_char_blacklist", "");
//        return readImagePartWithOCR(image, region, ocrConfig);
//    }
//
//    private String readImagePartWithOCR(BufferedImage image, ParseRegion region, OCRConfig ocrConfig) throws TesseractException, IOException {
//        String apiUrl = ocrConfig.getApiUrl();
//        String secretKey = ocrConfig.getSecretKey();
//
//        String result = "";
//
//        BufferedImage subImage = image.getSubimage(region.getX(), region.getY(), region.getWidth(), region.getHeight());
//        subImage = preprocessImage(subImage);
//
//
//        File imageFile = File.createTempFile(String.valueOf(subImage.hashCode()), ".png");
//        imageFile.deleteOnExit();
//        ImageIO.write(subImage, "jpg", imageFile);
//
//        // Create JSON request body
//        JSONObject requestJson = new JSONObject();
//        JSONArray images = new JSONArray();
//        JSONObject imageInfo = new JSONObject();
//        imageInfo.put("format", "png");
//        imageInfo.put("name", "demo");
//        images.put(imageInfo);
//        requestJson.put("images", images);
//        requestJson.put("requestId", UUID.randomUUID().toString());
//        requestJson.put("version", "V2");
//        requestJson.put("timestamp", System.currentTimeMillis());
//
//        // Create payload
//        String message = requestJson.toString();
//        String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString();
////            File imageFile = new File(imageFilePath);
//
//        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//        connection.setRequestProperty("X-OCR-SECRET", secretKey);
//
//        try (OutputStream outputStream = connection.getOutputStream();
//             PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true)) {
//
//            // Add JSON payload
//            writer.append("--").append(boundary).append("\r\n");
//            writer.append("Content-Disposition: form-data; name=\"message\"\r\n");
//            writer.append("Content-Type: application/json; charset=UTF-8\r\n\r\n");
//            writer.append(message).append("\r\n");
//
//            // Add image file
//            writer.append("--").append(boundary).append("\r\n");
//            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(imageFile.getName()).append("\"\r\n");
//            writer.append("Content-Type: ").append(Files.probeContentType(imageFile.toPath())).append("\r\n\r\n");
//            writer.flush();
//
//            Files.copy(imageFile.toPath(), outputStream);
//            outputStream.flush();
//            writer.append("\r\n");
//            writer.append("--").append(boundary).append("--").append("\r\n");
//            writer.flush();
//        }
//
//        // Get response
//
//        int responseCode = connection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
//                StringBuilder response = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//
//                // Parse and print the response
//                JSONObject jsonResponse = new JSONObject(response.toString());
//                JSONArray fields = jsonResponse.getJSONArray("images").getJSONObject(0).getJSONArray("fields");
//
//                for (int i = 0; i < fields.length(); i++) {
//                    JSONObject field = fields.getJSONObject(i);
//                    String text = field.getString("inferText");
//                    result += text;
//                    System.out.println(text);
//                }
//            }
//        } else {
//            System.out.println("Error: " + responseCode);
//        }
//
//        return result.replaceAll("\n\n", "\n");
//    }
//
//    private BufferedImage preprocessImage(BufferedImage image) {
//        try (OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
//             Java2DFrameConverter converterToFrame = new Java2DFrameConverter()) {
//
//            Mat mat = converterToMat.convert(converterToFrame.convert(image));
//            Mat resizedMat = new Mat();
//            opencv_imgproc.resize(mat, resizedMat, new Size(mat.cols() * 2, mat.rows() * 2));
//            return converterToFrame.convert(converterToMat.convert(resizedMat));
//        }
//    }
//
//}
