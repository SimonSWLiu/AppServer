package com.onemenu.server.util.interFax;

import net.interfax.inbound.GetList;
import net.interfax.inbound.GetListResponse;
import net.interfax.inbound.ListType;
import net.interfax.inbound.MessageItem;

public class GetListOfInboundFaxes {

    /******** Begin settings ********/
    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here
    static int MAX_ITEMS = 10; // Maximum number of inbound faxes to return in list

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.inbound.InboundSoapStub theBinding =
                    (net.interfax.inbound.InboundSoapStub) new net.interfax.inbound.InboundLocator()
                            .getInboundSoap();
            theBinding.setTimeout(60000);

            System.out.println("Retrieving inbound fax list using getList().");

            GetList parameters = new GetList(USERNAME, PASSWORD, ListType.AllMessages, // Select the
                                                                                       // type
                                                                                       // of list
                                                                                       // you wish
                                                                                       // to return
                    10, // max items
                    new MessageItem[0]);

            GetListResponse response = theBinding.getList(parameters);
            System.out.println("GetList() call returned with code: " + response.getGetListResult());
            System.out.println("Items returned: " + response.getObjMessageItem().length);

        } catch (Exception theE) {
            theE.printStackTrace();
        }
    }
}
