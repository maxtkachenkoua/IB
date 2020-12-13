package sample;

import com.ib.client.Contract;
import com.ib.client.Order;
import org.json.JSONArray;
import sample.database.DatabaseHandler;
import sample.provider.IBHandler;
import sample.subscriber.panel.info.InfoPanel;
import sample.subscriber.panel.order.OrderPanel;
import sample.subscriber.panel.order_distribution.OrderDistributionPanel;
import sample.subscriber.panel.order_list.OrderListPanel;
import sample.subscriber.panel.stop_loss.StopLossPanel;
import sample.subscriber.panel.take_profit.TakeProfitPanel;

import java.math.BigInteger;

public class Controller
{
    private static Controller _instance;

    public static Controller getInstance()
    {
        if(_instance == null)
        {
            _instance = new Controller();
        }
        return _instance;
    }

    /**
     * It will subscribe and start ib.
     */
    public void start()
    {
        // Subscribers added for getting IB events.
        IBHandler.getInstance().addSubscribers(InfoPanel.getInstance());
        IBHandler.getInstance().addSubscribers(OrderPanel.getInstance());
        IBHandler.getInstance().addSubscribers(OrderDistributionPanel.getInstance());
        IBHandler.getInstance().addSubscribers(OrderListPanel.getInstance());
        IBHandler.getInstance().addSubscribers(TakeProfitPanel.getInstance());
        IBHandler.getInstance().addSubscribers(StopLossPanel.getInstance());

        IBHandler.getInstance().start();
    }

    /**
     * It will use to prevent duplication in database.
     * sometime stoploss order not placed but order id registered in database. so that it make conflict and so that its need once
     * in every section order distribution, take profit, stop loss.
     */
    public void setupForNextValidOrderId()
    {
        JSONArray jsonArray = DatabaseHandler.getInstance().executeQuery("SELECT max(id) as max_id FROM order_table;");
        if(jsonArray.length() > 0 && jsonArray.getJSONObject(0).has("max_id")) {
            int total_quantity = jsonArray.getJSONObject(0).getInt("max_id");
            total_quantity++;
            IBHandler.getInstance().updateOrderID(total_quantity);
        }
    }

    /**
     * It will start placing order.
     */
    public void startPlaceOrder()
    {
//        TakeProfitPanel.getInstance().updateStatus(21,"FILLED",0,10);
//        Number masterOrderId1 = MainOrderPanel.getInstance().placeOrder();
//        System.out.println("masterOrderId === "+masterOrderId1);
        if(!OrderPanel.getInstance().validate(true))
        {
            System.out.println("Ticker and Total Quantity is mandatory.");

            return;
        }
        if(!OrderDistributionPanel.getInstance().validate(true))
        {
            System.out.println("Order Distribution Quantity Percentage not full filled.");

            return;
        }
        if(!TakeProfitPanel.getInstance().validate(true))
        {
            System.out.println("Take Profit Quantity Percentage not full filled.");

            return;
        }
        if(!StopLossPanel.getInstance().validate(true))
        {
            System.out.println("Stop loss Price Percentage not full filled.");

            return;
        }
        boolean isPlaceOrder = false;

        Number masterOrderId = OrderPanel.getInstance().placeOrder();

        if(masterOrderId != null) {
            isPlaceOrder = OrderDistributionPanel.getInstance().placeOrder(((BigInteger) masterOrderId).intValue());

            if (isPlaceOrder) {
                isPlaceOrder = TakeProfitPanel.getInstance().placeOrder(((BigInteger) masterOrderId).intValue());

                if (isPlaceOrder) {

                    // order_place_type empty means it will prefer textfield value.
                    isPlaceOrder = StopLossPanel.getInstance().placeOrder(((BigInteger) masterOrderId).intValue(),"",0);

                    if (isPlaceOrder) {
                        System.out.println("Finally order placed.");

                        // after place order reset all panel.
                        resetAll();
                    }
                }
            }
        }
    }

    /**
     * It will reset all panel.
     */
    public void resetAll()
    {
        // it will update list of order.
//        OrderListPanel.getInstance().reset();
//
//        OrderPanel.getInstance().reset();
//        OrderDistributionPanel.getInstance().reset();
//        TakeProfitPanel.getInstance().reset();
//        StopLossPanel.getInstance().reset();
    }

    /**
     * It will use to restore ui with order details.
     * @param mainOrderItem
     */
   /* public void restoreAll(MainOrderItem mainOrderItem) {
        JSONArray jsonArrayOrderTable = DatabaseHandler.getInstance().executeQuery("select tmt.exchange, tmt.symbol, omt.ticker_master_id, omt.outside_rth, omt.total_quantity, ot.* from order_master_table as omt, order_table as ot, ticker_master_table tmt where omt.ticker_master_id = tmt.id and ot.order_master_id = '"+mainOrderItem.getMasterOrderId()+"' and omt.id = '"+mainOrderItem.getMasterOrderId()+"';");
        System.out.println("jsonArray == "+ jsonArrayOrderTable);
        MainOrderPanel.getInstance().restorePanelData(jsonArrayOrderTable);
        OrderDistributionPanel.getInstance().restorePanelData(jsonArrayOrderTable);

        JSONArray jsonArrayTPTable = DatabaseHandler.getInstance().executeQuery("select omt.total_quantity, tpt.* from order_master_table as omt, take_profit_table as tpt where omt.id = "+mainOrderItem.getMasterOrderId()+" and tpt.order_master_id = "+mainOrderItem.getMasterOrderId()+";");
        TakeProfitPanel.getInstance().restorePanelData(jsonArrayTPTable);
        StopLossPanel.getInstance().restorePanelData(jsonArrayOrderTable);
    }*/
}
