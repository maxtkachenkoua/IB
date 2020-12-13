package sample.provider;

import com.ib.client.Contract;
import com.ib.client.Order;
import sample.enum_class.ENM_OrderType;
import sample.interface_class.IF_DataProvider;
import sample.interface_class.IF_DataSubscriber;
import sample.model.BalanceModel;
import sample.model.OrderModel;
import sample.model.OrderStatusModel;
import sample.provider.ib.ib.IBClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IBHandler implements IF_DataProvider
{
    private static IBHandler _instance;

    List<IF_DataSubscriber> listSubscriber;

    public static IBHandler getInstance()
    {
        if(_instance == null)
        {
            _instance = new IBHandler();
        }
        return _instance;
    }


    public void addSubscribers(IF_DataSubscriber dataSubscriber)
    {
        if(listSubscriber == null)
        {
            listSubscriber = new ArrayList<>();
        }

        if(!listSubscriber.contains(dataSubscriber))
        {
            listSubscriber.add(dataSubscriber);
        }
    }


    @Override
    public void start() {
        IBClient.getInstance().requestAccountSummary();
    }


    public synchronized int getOrderId()
    {
        return IBClient.getInstance().getOrderId();
    }

    public void updateOrderID(int orderid)
    {
        IBClient.getInstance().updateOrderID(orderid);
    }


    /**
     * It will provide current balance to subscribers.
     */
    @Override
    public void currentBalance(int reqId, String account, String tag, String value, String currency) {
        if(listSubscriber != null)
        {
            for(IF_DataSubscriber subscriber : listSubscriber)
            {
                subscriber.currentBalance(new BalanceModel(reqId,account,tag,value,currency));
            }
        }
    }

    /**
     * It will provide order status to subscribers
     */
    @Override
    public void orderStatus(int orderId, String status, double filled,
                            double remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld, double mktCapPrice)
    {
        if(listSubscriber != null)
        {
            for(IF_DataSubscriber subscriber : listSubscriber)
            {
                subscriber.orderStatus(new OrderStatusModel(orderId,status,filled,remaining,avgFillPrice,permId,parentId,lastFillPrice,clientId,whyHeld,mktCapPrice));
            }
        }
    }

    /**
     * It will place order odf given order model.
     * @param orderModel
     * @return
     */
    @Override
    public boolean orderPlace(OrderModel orderModel)
    {
        try
        {
            if(orderModel.getQuantity() == null || orderModel.getQuantity().compareTo(BigDecimal.ZERO)<= 0
                    || ((orderModel.getPrice() == null || orderModel.getPrice().compareTo(BigDecimal.ZERO) <= 0) && orderModel.getOrderType() == ENM_OrderType.LIMIT)) {
                return false;
            }

            Contract contract = new Contract();
            contract.symbol(orderModel.getSymbol());
            contract.exchange(orderModel.getExchange());
            contract.secType("STK");


            Order order = new Order();
            order.orderId(orderModel.getOrderId());
            order.action(orderModel.getAction().toString());
            order.totalQuantity(orderModel.getQuantity().doubleValue());

            if (orderModel.getOrderType() == ENM_OrderType.LIMIT)
            {
                order.orderType("LMT");
                order.lmtPrice(orderModel.getPrice().doubleValue());
            } else if (orderModel.getOrderType() == ENM_OrderType.STOP_LOSS)
            {
                order.orderType("STP");
                order.auxPrice(orderModel.getPrice().doubleValue());
            } else
            {
                order.orderType("MKT");
            }

            IBClient.getInstance().placeOrder(contract, order);
            System.out.println("Order place request posted successfully");
            return true;

        }catch (Exception e)
        {
            return false;
        }
    }

    /**
     * It will cancel order of given order model.
     * @param orderModel
     * @return
     */
    @Override
    public boolean orderCancel(OrderModel orderModel)
    {
        try
        {
            IBClient.getInstance().cancelOrder(orderModel.getOrderId());
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
