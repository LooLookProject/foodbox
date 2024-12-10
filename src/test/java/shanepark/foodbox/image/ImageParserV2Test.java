package shanepark.foodbox.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import shanepark.foodbox.image.service.NaverClovaOCR;
import shanepark.foodbox.ocr.OCRConfig;

import java.io.InputStream;


class ImageParserV2Test {
    private final Logger logger = LoggerFactory.getLogger(ImageParserV2Test.class);
    NaverClovaOCR parser = new NaverClovaOCR();

    String templateSecretKey = "";
    String templateAPIUrl = "";

    @Test
    @DisplayName("Parse image and return parsed menu")
    void parse(){
        //11/11(월)\n흰쌀밥\n데미글라스 소스\n스크램블 함박\n설탕 프렌치 토스트\n어묵 맛살 볶음\n살사 푸실리 샐러드\n시금치 나물 무침\n배추 김치\n버섯 고추장국
//        String tmp = "11/11(월)\n흰쌀밥\n데미글라스 소스\n스크램블 함박\n설탕 프렌치 토스트\n어묵 맛살 볶음\n살사 푸실리 샐러드\n시금치 나물 무침\n배추 김치\n버섯 고추장국";
//        String[] arr = tmp.split("\n");
//        System.out.println(arr);
        OCRConfig config = new OCRConfig(
                templateSecretKey
                ,templateAPIUrl
        );
        ClassPathResource resource = new ClassPathResource("menu-nov-11.png");
        try (InputStream ins = resource.getInputStream()) {
            parser.parse(ins,config);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
