package com.onemenu.server.util.interFax;

import net.interfax.outbound.CancelFax;
import net.interfax.outbound.CancelFaxResponse;

public class CancelPendingFax {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static int TRANSACTION_ID = 999999999; // Replace with the transaction ID of the fax to be
                                           // canceled

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.outbound.InterFaxSoapStub theBinding =
                    (net.interfax.outbound.InterFaxSoapStub) new net.interfax.outbound.InterFaxLocator()
                            .getInterFaxSoap();
            theBinding.setTimeout(60000);

            System.out.println("Canceling pending fax using CancelFax().");

            CancelFax params = new CancelFax(USERNAME, PASSWORD, TRANSACTION_ID);
            CancelFaxResponse response = theBinding.cancelFax(params);
            response.getCancelFaxResult();

            System.out.println("cancelFax() call returned with code: "
                    + response.getCancelFaxResult());

        } catch (Exception theE) {
            theE.printStackTrace();
        }
    }
}
