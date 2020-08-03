package com.fokantech.media_stream_service;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLEncoder;
import java.util.Date;

public class LicensePlate {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static void licensePlate(File file) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
        try {
            String filePath = file.getPath();
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            String accessToken = AuthService.getAuth();
            String result = HttpUtil.post(url, accessToken, param);
            JSONObject object = new JSONObject(result);
            BufferedImage bufferedImage = ImageIO.read(file);
            if (object.has("words_result")) {
                JSONObject words_result = (JSONObject) object.get("words_result");
                String number = String.valueOf(words_result.get("number"));

                ImageIO.write(bufferedImage,"jpg",new File("D:\\"+new Date().getTime()+number+".jpg"));
                file.delete();
            }else {
                ImageIO.write(bufferedImage,"jpg",new File("C:\\Users\\FKT00093\\Desktop\\out\\"+file.getName()));
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File[] files = new File("C:\\Users\\FKT00093\\Desktop\\new").listFiles();
        for (int i = 0; i < files.length; i++) {
            if (i<200){
                LicensePlate.licensePlate(files[i]);
            }
        }
    }
}