package sample.interface_class;


import sample.model.OrderModel;

/**
 * Interface to be implemented for data provide.
 */
public interface IF_DataProvider
{
    void start();
    boolean orderPlace(OrderModel orderModel);
    boolean orderCancel(OrderModel orderModel);
    void currentBalance(int reqId, String account, String tag,
                        String value, String currency);
    void orderStatus(int orderId, String status, double filled,
                     double remaining, double avgFillPrice, int permId, int parentId,
                     double lastFillPrice, int clientId, String whyHeld, double mktCapPrice);
}