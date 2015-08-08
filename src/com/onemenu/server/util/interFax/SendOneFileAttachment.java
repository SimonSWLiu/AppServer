package com.onemenu.server.util.interFax;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import net.interfax.outbound.Sendfax;
import net.interfax.outbound.SendfaxResponse;

public class SendOneFileAttachment {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static String FAX_NUMBER = ""; // Enter the destination fax number here, e.g. +497116589658
    static String FILE_NAME = "etc/sample.pdf"; // A file in your filesystem

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.outbound.InterFaxSoapStub theBinding =
                    (net.interfax.outbound.InterFaxSoapStub) new net.interfax.outbound.InterFaxLocator()
                            .getInterFaxSoap();
            theBinding.setTimeout(60000);
            System.out.println("Sending Fax using Sendfax()");

            // Read file data into a byte[].
            byte[] fileData = transformToBytes(FILE_NAME);

            System.out.println("Sending Fax using sendFax().  Document size: " + fileData.length);

            Sendfax theParams = new Sendfax(USERNAME, PASSWORD, FAX_NUMBER, fileData, "PDF");
            SendfaxResponse theResponse = theBinding.sendfax(theParams);
            System.out.println("Sendfax() call returned with code: "
                    + theResponse.getSendfaxResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to read a local file into a byte[].
     */
    private static byte[] transformToBytes(String aFilename) throws Exception {

        if (null == aFilename) {
            throw new NullPointerException("aFilename is null");
        }
        File theFile = new File(aFilename);
        if (!theFile.isFile()) {
            throw new IllegalArgumentException("Path doesn't represent a file: " + aFilename);
        }
        if (!theFile.exists()) {
            throw new IllegalArgumentException("File not found: " + aFilename);
        }

        InputStream theIs = new BufferedInputStream(new FileInputStream(theFile));
        ByteArrayOutputStream theRawData = new ByteArrayOutputStream();

        byte theBuffer[] = new byte[1024];
        int theBytesRead;

        try {
            while ((theBytesRead = theIs.read(theBuffer)) != -1) {
                if (theBytesRead < 1024) {
                    byte theSlice[] = new byte[theBytesRead];
                    System.arraycopy(theBuffer, 0, theSlice, 0, theBytesRead);
                    theRawData.write(theSlice);
                } else {
                    theRawData.write(theBuffer);
                }
            }
        } finally {
            theIs.close();
            theRawData.close();
        }

        return theRawData.toByteArray();
    }
}
