package shanepark.foodbox.image.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shanepark.foodbox.api.exception.ImageParseException;
import shanepark.foodbox.image.domain.ParsedMenu;
import shanepark.foodbox.ocr.OCRConfig;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NaverClovaOCR {

    public List<ParsedMenu> parse(InputStream inputStream, OCRConfig ocrConfig) {
        try {
            // 1. get Image
            File imageFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".png");
            imageFile.deleteOnExit();
            copyInputStreamToFile(inputStream, imageFile);
            List<ParsedMenu> days = new ArrayList<>();
            // 2. Parse Image
            JsonArray result = parseImageWithOCR(ocrConfig, imageFile);
            // 3. Set Menus
            if(result!=null){
                days = setMenus(result,days);
            }
            System.out.println(days);
            return days;
        } catch (Exception e) {
            throw new ImageParseException(e);
        }
    }

    private List<ParsedMenu> setMenus(JsonArray fields, List<ParsedMenu> days) {
        String menuOfDay = "";
        String date = "";

        for (int i = 0; i < fields.size(); i++) {
            JsonObject obj = (JsonObject) fields.get(i);
            String val = obj.get("name").toString();
            val = val.replaceAll("\"","");
            String fieldName = "day"+(i+1);
            if(fieldName.equals(val)){
                menuOfDay = obj.get("inferText").toString();
                menuOfDay = menuOfDay.replaceAll("\"","");
//                menuOfDay = menuOfDay.replaceAll("\n","  ");
                String[] arr = menuOfDay.split("\\)");
                date = arr[0]+")";
                ParsedMenu menuObj = new ParsedMenu(date);
                menuObj.setMenu(arr[1]);
                days.add(menuObj);
            }
        }
        return days;
    }

    private JsonArray parseImageWithOCR(OCRConfig ocrConfig, File imageFile) throws IOException {
        String apiUrl = ocrConfig.getApiUrl();
        String secretKey = ocrConfig.getSecretKey();

        // Create JSON request body
        JsonObject requestJson = new JsonObject();
        JsonArray images = new JsonArray();
        JsonObject imageInfo = new JsonObject();
        imageInfo.addProperty("format", "png");
        imageInfo.addProperty("name", "demo");
        images.add(imageInfo);
        requestJson.add("images", images);
        requestJson.addProperty("requestId", UUID.randomUUID().toString());
        requestJson.addProperty("version", "V2");
        requestJson.addProperty("timestamp", System.currentTimeMillis());

        // Create payload
        String message = requestJson.toString();
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString();

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("X-OCR-SECRET", secretKey);

        try (OutputStream outputStream = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true)) {

            // Add JSON payload
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"message\"\r\n");
            writer.append("Content-Type: application/json; charset=UTF-8\r\n\r\n");
            writer.append(message).append("\r\n");

            // Add image file
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(imageFile.getName()).append("\"\r\n");
            writer.append("Content-Type: ").append(Files.probeContentType(imageFile.toPath())).append("\r\n\r\n");
            writer.flush();

            Files.copy(imageFile.toPath(), outputStream);
            outputStream.flush();
            writer.append("\r\n");
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.flush();
        }

        // Get response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String resultString = response.toString();
                JsonParser parser = new JsonParser();
                JsonObject jsonResponse = (JsonObject) parser.parse(resultString);

                JsonArray fields = jsonResponse.getAsJsonArray("images").get(0).getAsJsonObject().getAsJsonArray("fields");
                return fields;
            }
        } else {
            System.out.println("Error: " + responseCode);
        }
        return null;
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ParsedMenu> setDates(String date1, String date2) {
        String[] arr1 = date1.split(" ");
        String[] arr2 = date1.split(" ");
        List<String> list1 = new ArrayList<>(Arrays.asList(arr1));
        List<String> list2 = new ArrayList<>(Arrays.asList(arr2));
        list1.addAll(list2);
        List<ParsedMenu> days = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            days.add(new ParsedMenu(list1.get(i)));
        }
        return days;
    }

    public static JsonObject convertBufferedImageToJson(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // JSON 객체 생성
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("width", width);
        jsonObject.addProperty("height", height);

        JsonArray pixelArray = new JsonArray();

        // 이미지 픽셀 데이터를 JSON 배열로 변환
        for (int y = 0; y < height; y++) {
            JsonArray row = new JsonArray();
            for (int x = 0; x < width; x++) {
                int pixel = bufferedImage.getRGB(x, y);

                // ARGB 값 추출
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                // JSON 객체로 픽셀 정보 추가
                JsonObject pixelJson = new JsonObject();
                pixelJson.addProperty("alpha", alpha);
                pixelJson.addProperty("red", red);
                pixelJson.addProperty("green", green);
                pixelJson.addProperty("blue", blue);

                row.add(pixelJson);
            }
            pixelArray.add(row);
        }

        jsonObject.add("pixels", pixelArray);
        return jsonObject;
    }

}
