package com.onemenu.server.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author lin
 *
 */
public class ImageUtils {
	
	private static final Logger logger = Logger.getLogger(ImageUtils.class);
	
    public static final int IMAGE_WIDTH_270 = 270;
    public static final int IMAGE_WIDTH_540 = 540;
    public static final int IMAGE_WIDTH_1080 = 1080;
    public static final int IMAGE_WIDTH_1680 = 1680;

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * 
     * @param imgFilePath 图片路径
     * @extension extension 文件后缀
     * @return String
     */
    public static String getBase64FromInputStream(InputStream in, String extension) {
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

    public static int getTargetHeight(BufferedImage bi, int targetWidth) {

        double ratioHeightWidth = (double) bi.getHeight() / bi.getWidth();
        int targetHeight = (int) (ratioHeightWidth * targetWidth);

        return targetHeight;
    }


    public static BufferedImage resize(BufferedImage bi, int targetWidth, int targetHeight) {

        // targetW，targetH分别表示目标长和宽
        int type = bi.getType();
        BufferedImage target = null;

        double sx = (double) targetWidth / bi.getWidth();
        double sy = (double) targetHeight / bi.getHeight();

        if (type == BufferedImage.TYPE_CUSTOM) { // handmade
            ColorModel cm = bi.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetWidth, targetHeight);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(targetWidth, targetHeight, type);
        }

        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(bi, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();

        return target;
    }

    // public static BufferedImage saveImageAsJpg(String fromFilePath, String toFilePath, int width,
    // int height) throws Exception {
    // BufferedImage bi;
    //
    // File fromFile = new File(fromFilePath);
    // File toFile = new File(toFilePath);
    //
    // // String imgType = FilenameUtils.getExtension(fromFile.getName()); //get the extension
    // String imgType = "jpeg";
    // bi = ImageIO.read(fromFile);
    //
    // if (width > 0 || height > 0) {
    // bi = resize(bi, width, height);
    // }
    // ImageIO.write(bi, imgType, toFile);
    //
    // return bi;
    // }

    public static InputStream getInputStreamFromBufferedImage(BufferedImage bi, String imgType) {

        InputStream is = null;
        bi.flush();

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut;
        try {
            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bi, imgType, imOut);
            is = new ByteArrayInputStream(bs.toByteArray());

        } catch (IOException e) {
            logger.error("", e);
        }

        return is;
    }

    public static String getBase64FromInputStream(InputStream is, String extension, int targetWidth) {

        String base64Str = "";
        try {

            BufferedImage bi = ImageIO.read(new BufferedInputStream(is)); // if no
                                                                          // BufferedInputStream,
                                                                          // ImageIO.read(is) will
                                                                          // return null.
            if (bi != null) {
                if (bi.getWidth() > targetWidth) {
                    int targetHeight = getTargetHeight(bi, targetWidth);
                    bi = resize(bi, targetWidth, targetHeight);
                }
                InputStream targetIs = getInputStreamFromBufferedImage(bi, extension);
                base64Str = getBase64FromInputStream(targetIs, extension);
                System.out.println(targetIs.available());
            }

        } catch (IOException e) {
           logger.error("", e);
        }

        return base64Str;
    }
}
