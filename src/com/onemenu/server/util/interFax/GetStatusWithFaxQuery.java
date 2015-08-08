package com.onemenu.server.util.interFax;

import net.interfax.outbound.FaxItemEx;
import net.interfax.outbound.FaxQuery;
import net.interfax.outbound.FaxQueryResponse;

public class GetStatusWithFaxQuery {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static String QUERY_VERB = "LT"; // Very flexible, see documentation
    static String QUERY_DATA = "999999999";
    static int MAX_ITEMS = 10; // How many transactions to return information for

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.outbound.InterFaxSoapStub theBinding =
                    (net.interfax.outbound.InterFaxSoapStub) new net.interfax.outbound.InterFaxLocator()
                            .getInterFaxSoap();
            theBinding.setTimeout(60000);

            FaxQuery theParams = new FaxQuery(USERNAME, PASSWORD, QUERY_VERB, QUERY_DATA, -1, // max
                                                                                              // items
                    -1 // result code
                    );
            FaxQueryResponse response = theBinding.faxQuery(theParams);

            int resultCode = response.getResultCode();
            FaxItemEx[] results = response.getFaxQueryResult();
            System.out.println("Service call completed with status: " + resultCode);
            if (null == results) {
                System.out.println("Null result");
            } else {
                System.out.println("FaxQuery call succeeded.");
                for (int i = 0; i < results.length; i++) {
                    System.out.println("===============================");
                    System.out.println("TransactionID: " + results[i].getTransactionID());
                    System.out.println("Destination: " + results[i].getDestinationFax());
                    System.out.println("Status: " + results[i].getStatus());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
