package sample.provider.ib.ib;

import com.ib.client.Contract;
import com.ib.client.Order;

public class main {

    public static void main(String[] args)
    {
        Contract contract = new Contract();
        contract.symbol("AAPL");
        contract.exchange("ISLAND");
        contract.secType("STK");
        IBClient.getInstance().reqMktData(0,contract);

        IBClient.getInstance().reqDelayedMarketDataType();
        IBClient.getInstance().reqRealTimeBar(contract);

//        IBClient.getInstance().requestAccountSummary();
//        "AFLT"
//        placeOrder("AAPL", "SELL", 100, 288.33, true);
//        placeOrder("AAPL", "BUY", 50, 122, true);
        System.out.println("Hello World");
    }


    private static void placeOrder(String symbol, String action, double quantity, double price, boolean isLimitOrder)
    {
        Contract contract = new Contract();
        contract.symbol(symbol);
        String eqexchange = "ISLAND";
//        String eqexchange = "MOEX";

        /*switch (Symbol.getInstance().getExchange(symbol))
        {
            case "NASDAQ":
                eqexchange = "ISLAND";
                break;
            case "NYSE":
                eqexchange = "NYSE";
                break;
            default:
                eqexchange = Symbol.getInstance().getExchange(symbol);

        }*/

        contract.exchange(eqexchange);
        contract.secType("STK");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int orderId = 54;//IBClient.getInstance().getOrderId();
        System.out.println("orderId ::: "+ orderId);
        Order parent = new Order();
        parent.orderId(orderId);
        parent.action(action);
        parent.totalQuantity(quantity);
        parent.transmit(true);
        parent.outsideRth(true);

        if (isLimitOrder)
        {
            parent.orderType("LMT");
            parent.lmtPrice(price);
        } else
        {
            parent.orderType("MKT");
        }

        System.out.println("place order request generated");
        IBClient.getInstance().placeOrder(contract, parent);
        System.out.println("place order request send");

        System.out.println("Order place request posted successfully");
    }

}
