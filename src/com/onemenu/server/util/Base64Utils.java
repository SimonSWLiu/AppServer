package com.onemenu.server.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author lin
 *
 */
public class Base64Utils {
	
	private static final Logger logger = Logger.getLogger(Base64Utils.class);

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * 
     * @param imgFilePath 图片路径
     * @extension extension 文件后缀
     * @return String
     */
    public static String getImageStr(InputStream in, String extension) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
        	logger.error("", e);
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return "data:" + MimeUtils.guessMimeTypeFromExtension(extension) + ";base64,"
                + encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    public static InputStream getInputStreamFromBase64(String base64) {
        byte[] result = null;

        if (base64 != null && base64.startsWith("data")) {
            base64 = base64.substring(base64.indexOf(",") + 1); // 这里要把上传上来的base64
            // 里面的data:image/png;base64,处理一下才能保存成文件
            result = Base64.decodeBase64(base64);
        }

        InputStream in = new ByteArrayInputStream(result);
        return in;
    }


}
