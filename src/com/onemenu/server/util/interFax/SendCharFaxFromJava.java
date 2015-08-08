package com.onemenu.server.util.interFax;

import net.interfax.outbound.SendCharFax;
import net.interfax.outbound.SendCharFaxResponse;

public class SendCharFaxFromJava {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static String FAX_NUMBER = ""; // Enter the destination fax number here in the format:
                                   // +[country code][area code][fax number]
                                   // for example: +12125554874
    static String TEXT_TO_FAX = "My text goes here"; // Enter your fax contents here (ASCII text
                                                     // only)
    static String FILE_TYPE = "TXT"; // If TEXT_TO_FAX holds regular text, enter "TXT" here.
                                     // If TEXT_TO_FAX holds HTML enter "HTML" here

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.outbound.InterFaxSoapStub theBinding =
                    (net.interfax.outbound.InterFaxSoapStub) new net.interfax.outbound.InterFaxLocator()
                            .getInterFaxSoap();
            theBinding.setTimeout(60000);
            System.out.println("Sending Fax using sendCharFax()");
            SendCharFax theParams =
                    new SendCharFax(USERNAME, PASSWORD, FAX_NUMBER, TEXT_TO_FAX, FILE_TYPE);
            SendCharFaxResponse theResponse = theBinding.sendCharFax(theParams);

            long theReturnCode = theResponse.getSendCharFaxResult();
            System.out.println("sendCharFax() call returned with code: " + theReturnCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
