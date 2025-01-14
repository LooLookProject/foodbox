package shanepark.foodbox.image.ocr.clova;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class NaverClovaApi {

    // TODO: Implement Clova OCR API request
    public String clovaRequest(String base64Image) {
        String body = String.format(
                """
                        {
                            "images": [
                            {
                                "format": "png",
                                "name": "menu",
                                "data": "%s",
                                "url": null
                            }
                            ],
                            "lang": "ko",
                            "requestId": "string",
                            "resultType": "string",
                            "timestamp": %d,
                            "version": "V1"
                        }
                        """,
                base64Image, Instant.now().toEpochMilli()
        );
        return "";
    }

}
