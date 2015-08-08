package com.onemenu.server.util.interFax;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.interfax.inbound.GetImageChunk;
import net.interfax.inbound.GetImageChunkResponse;

public class GetFaxWithGetImageChunk {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static int MESSAGE_ID = 0; // Transaction ID of the inbound message to be read
    static int MESSAGE_SIZE = 0; // Size in bytes of the inbound message. This value
                                 // is returned by method GetList, which should be run
                                 // before this one
    static boolean MARK_AS_READ = false; // Mark the item as 'read' after downloading? If TRUE,
    // item will no longer be 'new' and will be shown upon
    // subsequent list retrieval via GetList
    private static long CHUNK_SIZE = 100000l;

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.inbound.InboundSoapStub theBinding =
                    (net.interfax.inbound.InboundSoapStub) new net.interfax.inbound.InboundLocator()
                            .getInboundSoap();
            theBinding.setTimeout(60000);

            ByteArrayOutputStream os = new ByteArrayOutputStream(MESSAGE_SIZE);
            long from = 0l;
            for (long i = 0; i < MESSAGE_SIZE; i += CHUNK_SIZE) {
                from = i;

                GetImageChunk parameters =
                        new GetImageChunk(USERNAME, PASSWORD, MESSAGE_ID, MARK_AS_READ, CHUNK_SIZE,
                                from, new byte[0]);
                GetImageChunkResponse response = theBinding.getImageChunk(parameters);
                System.out.println("GetList() call returned with code: "
                        + response.getGetImageChunkResult());

                // Write this chunk to our byte array stream.
                os.write(response.getGetImageChunkResult());
            }

            byte[] imageBytes = os.toByteArray();

            String filename = Integer.toString(MESSAGE_ID) + ".tif";
            writeFile(filename, imageBytes);

        } catch (Exception theE) {
            theE.printStackTrace();
        }
    }

    /**
     * Helper method to write a byte[] to a local file.
     */
    private static void writeFile(String aFilename, byte[] aFileData) throws Exception {

        if (null == aFilename) {
            throw new NullPointerException("aFilename is null");
        }
        if (null == aFileData) {
            throw new NullPointerException("aFileData is null");
        }

        File theFile = new File(aFilename);
        theFile.createNewFile();

        InputStream theIs = new BufferedInputStream(new ByteArrayInputStream(aFileData));
        OutputStream theOs = new FileOutputStream(theFile);

        byte theBuffer[] = new byte[1024];
        int theBytesRead;

        try {
            while ((theBytesRead = theIs.read(theBuffer)) != -1) {

                if (theBytesRead < 1024) {
                    byte theSlice[] = new byte[theBytesRead];
                    System.arraycopy(theBuffer, 0, theSlice, 0, theBytesRead);
                    theOs.write(theSlice);
                } else {
                    theOs.write(theBuffer);
                }
            }
        } finally {
            theIs.close();
            theOs.close();
        }
        System.out.println("Fax image saved to file: " + theFile.getAbsolutePath());
    }
}
