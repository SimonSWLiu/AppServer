package com.onemenu.server.util.interFax;

import java.util.GregorianCalendar;

import net.interfax.outbound.FaxItemEx2;
import net.interfax.outbound.FaxQuery2;
import net.interfax.outbound.FaxQuery2Response;
import net.interfax.outbound.OrderByColumnEnum;
import net.interfax.outbound.QueryCondition;
import net.interfax.outbound.QueryControl;
import net.interfax.outbound.QueryForm;
import net.interfax.outbound.SQLOperatorEnum;

public class GetStatusWithFaxQuery2 {

    /******** Begin settings ********/
    // See http://www.interfax.net/en/dev/webservice/reference/faxquery2
    // for an explanation of properties

    // Valid values for [property]_verb are:
    // Equals | GreaterThan | GreaterOrEqual | LessThan | LessOrEqual | Like | IncludedIn | Between
    // Leave both [property]_verb and [property]_data empty to ignore the property in the query

    static String USERNAME = ""; // Enter your Interfax username here
    static String PASSWORD = ""; // Enter your Interfax password here

    static String SUBJECT_DATA = null;
    static String FAXNUMBER_DATA = null;
    static String DATE_TO = null;
    static String USER_ID_DATA = null;
    static String REPLY_ADDRESS_DATA = null;
    static String TRANSACTION_ID_DATA = null;
    static String PARENT_TRANSACTION_ID_DATA = null;
    static String STATUS_DATA = null;

    // The following values control display settings
    static boolean SHOW_HIDDEN_TRANSACTIONS = true;
    static boolean ONLY_PARENTS = false;
    static int NUM_OF_RESULTS = 999;
    static int STARTING_RECORD = 0;

    // Valid values for $order_by are:
    // TransactionID | SubmitTime | CompletionTime | Status | DestinationFax | Subject | PagesSent |
    // UserID | ReplyEmail
    static String ORDER_BY = "TransactionID";
    static boolean ASC_ORDER_DIRECTION = true;
    static boolean RETURN_ITEMS = true;
    static boolean RETURN_STATS = true;

    /******** End settings ********/

    public static void main(String[] anArgs) {
        try {
            net.interfax.outbound.InterFaxSoapStub theBinding =
                    (net.interfax.outbound.InterFaxSoapStub) new net.interfax.outbound.InterFaxLocator()
                            .getInterFaxSoap();
            theBinding.setTimeout(60000);

            QueryForm queryForm = new QueryForm();
            queryForm.setSubject(new QueryCondition(SQLOperatorEnum.Equals, SUBJECT_DATA));
            queryForm.setFaxNumber(new QueryCondition(SQLOperatorEnum.Equals, ""));
            queryForm.setDateFrom(new GregorianCalendar(1999, 1, 1, 0, 0));
            queryForm.setDateTo(new GregorianCalendar(2999, 12, 31, 23, 59));
            queryForm.setUserID(new QueryCondition(SQLOperatorEnum.Equals, USER_ID_DATA));
            queryForm
                    .setReplyAddress(new QueryCondition(SQLOperatorEnum.Equals, REPLY_ADDRESS_DATA));
            queryForm.setTransactionID(new QueryCondition(SQLOperatorEnum.Equals,
                    TRANSACTION_ID_DATA));
            queryForm.setParentTransactionID(new QueryCondition(SQLOperatorEnum.Equals,
                    PARENT_TRANSACTION_ID_DATA));
            queryForm.setStatus(new QueryCondition(SQLOperatorEnum.Equals, STATUS_DATA));

            QueryControl queryControl =
                    new QueryControl(ONLY_PARENTS, NUM_OF_RESULTS, STARTING_RECORD,
                            OrderByColumnEnum.TransactionID, ASC_ORDER_DIRECTION, RETURN_ITEMS,
                            RETURN_STATS);

            FaxQuery2 theParams = new FaxQuery2(USERNAME, PASSWORD, queryForm, queryControl);
            FaxQuery2Response response = theBinding.faxQuery2(theParams);

            int resultCode = response.getFaxQuery2Result().getResultCode();
            FaxItemEx2[] results = response.getFaxQuery2Result().getFaxItems();
            System.out.println("Service call completed with status: " + resultCode);
            if (null == results) {
                System.out.println("Null result");
            } else {
                System.out.println("FaxQuery2 call succeeded.");
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
