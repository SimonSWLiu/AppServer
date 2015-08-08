package com.onemenu.server.util.interFax;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.interfax.outbound.SendfaxEx_2;
import net.interfax.outbound.SendfaxEx_2Response;

public class SendMultipleFilesToMultipleRecipients {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static String FAX_NUMBERS = "+12077301111;+492077301222;+442077301333"; // semicolon-delimited
                                                                            // list of destination
                                                                            // fax
                                                                            // numbers
    static String CONTACTS = "Rachel Burns;Albie Eins;Max Planck"; // optional semicolon-delimited
                                                                   // list of recipient names; one
                                                                   // per
                                                                   // fax number
    static String[] FILES = new String[] {"etc/sample.pdf", "etc/sample.html"}; // List of files to
                                                                                // fax
    // These files need to be resident on the file system
    static int RETRIES = 3;
    static String CSID = "AA CSID";
    static String PAGE_HEADER = "To: {To} From: {From} Pages: {TotalPages}";
    static String SUBJECT = "Anything goes";
    static String REPLY_EMAIL = "";
    static String PAGE_SIZE = "A4";
    static String PAGE_ORIENTATION = "Portrait";
    static boolean HIGH_RESOLUTION = false;
    static boolean FINE_RENDERING = true;

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.outbound.InterFaxSoapStub theBinding =
                    (net.interfax.outbound.InterFaxSoapStub) new net.interfax.outbound.InterFaxLocator()
                            .getInterFaxSoap();
            theBinding.setTimeout(60000);

            System.out.println("Sending Faxes using sendfaxEx_2()");

            // Read files into a single byte[].
            List<String> fileLengths = new ArrayList<String>(FILES.length);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            for (String filename : FILES) {
                byte[] currFileBytes = transformToBytes(filename);
                os.write(currFileBytes);
                fileLengths.add(Integer.toString(currFileBytes.length));
            }
            byte[] fileData = os.toByteArray();

            // Build a String describing the length of each text fax (e.g. "15;16")
            String fileLengthsStr = join(fileLengths, ";");

            // Create a Calendar instance.
            GregorianCalendar cal = new GregorianCalendar(2001, 12, 31, 0, 0); // December 31, 2001,
                                                                               // 00:00

            // Invoke the sendfaxEx_2 method
            SendfaxEx_2 params =
                    new SendfaxEx_2(USERNAME, PASSWORD, FAX_NUMBERS, CONTACTS, fileData,
                            "PDF;HTML", fileLengthsStr,
                            cal, // postpone
                            RETRIES, CSID, PAGE_HEADER,
                            "", // jobId
                            SUBJECT, REPLY_EMAIL, PAGE_SIZE, PAGE_ORIENTATION, HIGH_RESOLUTION,
                            FINE_RENDERING);

            SendfaxEx_2Response response = theBinding.sendfaxEx_2(params);
            long theResult = response.getSendfaxEx_2Result();

            if (theResult > 0) {
                // Positive result indicates that the fax was sent successfully.
                // The return value is the TransactionID.
                System.out.println("Fax was sent successfully. Transaction ID: " + theResult);
            } else {
                // Fax send failure. Please see the documentation for a list of
                // error codes and their meanings:
                // http://interfax.net/en/dev/webservice/reference.html#appendixa
                System.out.println("Error sending fax!" + theResult);
            }
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

    /**
     * Helper method to join strings using a delimeter.
     */
    private static String join(Collection<? extends Object> c, String d) {
        StringBuffer buffer = new StringBuffer();
        Iterator<? extends Object> it = c.iterator();
        while (it.hasNext()) {
            buffer.append(it.next());
            if (it.hasNext()) {
                buffer.append(d);
            }
        }
        return buffer.toString();
    }
}
