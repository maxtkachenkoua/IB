package sample.provider.ib.ib;

import com.ib.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class IBClient
{

    private static IBClient instance = null;
    private IBWrapper wrapper;
    private EClientSocket m_client;
    private EReaderSignal m_signal;
    private int reqId = 1000;
    private int orderid = 1000;
    public ConcurrentHashMap<Integer, Contract> contractConcurrentHashMap;

    private IBClient()
    {
        contractConcurrentHashMap = new ConcurrentHashMap<>();
        wrapper = new IBWrapper();

        m_client = wrapper.getClient();
        m_signal = wrapper.getSignal();
        //! [connect]
        String ip = IBConfig.getInstance().getConnectionIP();
        int port = IBConfig.getInstance().getIBPort();
        int masterclientid = IBConfig.getInstance().getMasterClientId();
        m_client.eConnect(ip, port, masterclientid);
        //! [connect]
        //! [ereader]
        final EReader reader = new EReader(m_client, m_signal);

        reader.start();
        //An additional thread is created in this program design to empty the messaging queue
        new Thread(() ->
        {
            while (m_client.isConnected())
            {
                m_signal.waitForSignal();
                try
                {
                    reader.processMsgs();
                }
                catch (Exception e)
                {
                    System.out.println("Exception: " + e.getMessage());
                }
            }
        }).start();
        //! [ereader]
        // A pause to give the application time to establish the connection
        // In a production application, it would be best to wait for callbacks to confirm the connection is complete
    }

    public static IBClient getInstance()
    {
        if (instance == null)
            instance = new IBClient();
        return instance;
    }

   public synchronized void reqDelayedMarketDataType() {
//        # Switch to live (1) frozen (2) delayed (3) delayed frozen (4).
       m_client.reqMarketDataType( 3 );
    }

   public synchronized int reqRealTimeBar(Contract contract) {
        int r = reqId++;
        m_client.reqRealTimeBars(r, contract, 5, "MIDPOINT", true, null);
        return r;
    }

    public void placeOrder(Contract contract, Order order)
    {
        m_client.placeOrder(order.orderId(), contract, order);
    }

    public void submitOrder(Contract contract, String action, double quantity, double sl, double tp)
    {
        List<Order> orders = BracketOrder(action, quantity, 0, tp, sl);
        for (Order order : orders)
        {
            System.out.println("placing order " + order);
            m_client.placeOrder(order.orderId(), contract, order);
        }
    }

   /* public int reqHistoricalBarData(Instrument instrument, String endtime, String duration, String barSizeSetting,
                                    String whatToShow, int useRTH) {
        int id = IdGenerator.getInstance().getCounter();
        InstrumentSingleton.getInstance().put(id, instrument);
        m_client.reqHistoricalData(id, instrument.buildContract(), endtime, duration, barSizeSetting, whatToShow, useRTH, 1, false, null);
        return id;
    }*/

    /**
     * Request tick himanshu.data for last trade
     */
    /*public void reqRealtimeTickDataLast(Instrument instrument)
    {
    	int id = IdGenerator.getInstance().getCounter();
    	m_client.reqTickByTickData(id, instrument.buildContract(), "Last", 0, false);
    	InstrumentSingleton.getInstance().put(id, instrument);
    }*/
    public List<Order> BracketOrder(String action, double quantity, double limitPrice,
                                    double takeProfitLimitPrice, double stopLossPrice)
    {
        //This will be our main or "parent" order
        Order parent = new Order();
        int parentid = orderid++;
        parent.orderId(parentid);
        parent.action(action);
        parent.orderType("MKT");
        parent.totalQuantity(quantity);

        //The parent and children orders will need this attribute set to false to prevent accidental executions.
        //The LAST CHILD will have it set to true,
        parent.transmit(false);
        Order takeProfit = new Order();
        takeProfit.orderId(orderid++);
        takeProfit.action((action.equals("BUY") ? "SELL" : "BUY"));
        takeProfit.orderType("LMT");
        takeProfit.totalQuantity(quantity);
        takeProfit.lmtPrice(takeProfitLimitPrice);
        takeProfit.parentId(parentid);
        takeProfit.transmit(false);
        Order stopLoss = new Order();
        stopLoss.orderId(orderid++);
        stopLoss.action((action.equals("BUY") ? "SELL" : "BUY"));
        stopLoss.orderType("STP");
        //Stop trigger price
        stopLoss.auxPrice(stopLossPrice);
        stopLoss.totalQuantity(quantity);
        stopLoss.parentId(parentid);
        //In this case, the low side order will be the last child being sent. Therefore, it needs to set this attribute to true
        //to activate all its predecessors
        stopLoss.transmit(true);
        List<Order> bracketOrder = new ArrayList<>();
        bracketOrder.add(parent);
        bracketOrder.add(takeProfit);
        bracketOrder.add(stopLoss);
        return bracketOrder;
    }

    public void reqMktData(int id, Contract contract)
    {
        m_client.reqMktData(id, contract, "101,106", false, false, new ArrayList<>());
    }


    /**
     * request historical tick himanshu.data for a given instruemnt
     *
     * @param id            - request id
     * @param contract      - the contract for which we are making the request
     * @param starttime     - starttime for the historical himanshu.data format should be 20170712 21:39:33
     * @param endtime       - endtime for the historica himanshu.data. format should be 20170712 21:39:33
     * @param numberOfTicks - number of ticks requsted
     * @param whatToShow    - TRADES, MIDPOINT OR BIDASK
     * @param useRTH        - suggest whether to pull himanshu.data out of regular trading hours
     * @param ignoreSize    - ignore ticks where only size is affected not price
     */
    public void reqHistoricalTick(int id, Contract contract, String starttime, String endtime,
                                  int numberOfTicks, String whatToShow, int useRTH, boolean ignoreSize)
    {
        m_client.reqHistoricalTicks(id, contract, starttime, endtime, numberOfTicks, whatToShow,
                useRTH, ignoreSize, null);


    }

    public void requestAccountSummary()
    {
        reqId++;
        m_client.reqAccountSummary(reqId, "All", "$LEDGER:USD"/*"""DU1529868"*/);
    }

    public void cancelAccountSummary(int reqId)
    {
        m_client.cancelAccountSummary(reqId);
    }

    public void closeAllPositions()
    {

    }

    public void cancelAllOpenTrades()
    {
        m_client.reqGlobalCancel();
    }

    public void cancelOrder(int orderId)
    {
        m_client.cancelOrder(orderId);
    }

    public synchronized int getOrderId()
    {
        return orderid++;
    }

    public void updateOrderID(int orderid)
    {
        this.orderid = orderid;
    }

    public void requestCommission()
    {
        int reqId = IdGenerator.getInstance().getCounter();
        m_client.reqExecutions(reqId, new ExecutionFilter());
    }

}
