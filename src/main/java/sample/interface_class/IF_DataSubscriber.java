package sample.interface_class;

import sample.model.BalanceModel;
import sample.model.OrderStatusModel;

/**
 * Interface to be implemented for data subscribes.
 */
public interface IF_DataSubscriber
{
    void currentBalance(BalanceModel balanceModel);
    void orderStatus(OrderStatusModel orderStatusModel);
}