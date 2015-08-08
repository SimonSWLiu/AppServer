package com.onemenu.server.util.interFax;

import net.interfax.outbound.FaxItemEx;
import net.interfax.outbound.FaxStatusEx;
import net.interfax.outbound.FaxStatusExResponse;

public class GetStatusWithFaxStatusEx {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static int TRANSACTION_ID = 999999999; // Enter the transaction ID for which data
                                           // is to be retrieved, or 999999999 for
                                           // the most recent transaction(s)
    static int MAX_ITEMS = 10; // How many transactions to return information for

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.outbound.InterFaxSoapStub theBinding =
                    (net.interfax.outbound.InterFaxSoapStub) new net.interfax.outbound.InterFaxLocator()
                            .getInterFaxSoap();
            theBinding.setTimeout(60000);

            FaxStatusEx params = new FaxStatusEx(USERNAME, PASSWORD, TRANSACTION_ID, MAX_ITEMS, 0, // totalCount
                    0, // listSize
                    0 // resultCode
                    );

            FaxStatusExResponse response = theBinding.faxStatusEx(params);

            FaxItemEx[] theResults = response.getFaxStatusExResult();

            System.out.println("faxStatusEx call succeeded.");
            for (int i = 0; i < theResults.length; i++) {
                System.out.println("===============================");
                System.out.println("TransactionID: " + theResults[i].getTransactionID());
                System.out.println("Destination: " + theResults[i].getDestinationFax());
                System.out.println("Status: " + theResults[i].getStatus());
            }
        } catch (Exception theE) {
            theE.printStackTrace();
        }
    }
}
