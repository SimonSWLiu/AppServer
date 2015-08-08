package com.onemenu.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

/**
 * 
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @author linhang
 * @date [2014-4-22]
 */
public class NetUtils {
	
	private static final  Logger  logger = Logger.getLogger(NetUtils.class);
	
    /**
     * 解压缩收到的数据流
     */
    public static String unzipDataAndLog(InputStream inStream) {
        
        if (inStream == null) {
            return null;
        }

        try {
            byte[] old_bytes = toByteArray(inStream);
            // byte[] new_bytes = ungzip(old_bytes);
            return new String(old_bytes, "utf-8");
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return null;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static byte[] ungzip(byte[] bs) throws Exception {
        GZIPInputStream gzin = null;
        ByteArrayInputStream bin = null;
        try {
            bin = new ByteArrayInputStream(bs);
            gzin = new GZIPInputStream(bin);
            return toByteArray(gzin);
        } catch (Exception e) {
        	logger.error("", e);
            throw e;
        } finally {
            if (bin != null) {
                bin.close();
            }
        }
    }

}
