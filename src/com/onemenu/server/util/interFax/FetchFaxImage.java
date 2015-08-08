package com.onemenu.server.util.interFax;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.interfax.outbound.GetFaxImage;
import net.interfax.outbound.GetFaxImageResponse;

public class FetchFaxImage {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static int TRANSACTION_ID = 123266916; // Enter the transaction ID for which data
                                           // is to be retrieved, or "999999999" for
                                           // the most recent transaction(s)

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.outbound.InterFaxSoapStub theBinding =
                    (net.interfax.outbound.InterFaxSoapStub) new net.interfax.outbound.InterFaxLocator()
                            .getInterFaxSoap();
            theBinding.setTimeout(60000);

            // Send a simple text fax using the InterFax sendFax() web service method.
            System.out.println("Retrieving Fax using getFaxImage().");

            GetFaxImage params = new GetFaxImage(USERNAME, PASSWORD, TRANSACTION_ID, null // byte[]
                                                                                          // for
                                                                                          // image
                    );
            GetFaxImageResponse response = theBinding.getFaxImage(params);
            response.getGetFaxImageResult();

            System.out.println("getFaxImage() call returned with code: "
                    + response.getGetFaxImageResult());
            System.out.println("Image size: " + response.getImage().length);

            String filename = Integer.toString(TRANSACTION_ID) + ".tif";
            writeFile(filename, response.getImage());

        } catch (Exception theE) {
            theE.printStackTrace();
        }
    }

    /**
     * Helper method for writing a byte[] to a local file.
     */
    public static void writeFile(String aFilename, byte[] aFileData) throws Exception {

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
