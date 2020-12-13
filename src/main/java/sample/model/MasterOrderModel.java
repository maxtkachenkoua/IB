package sample.model;

import sample.enum_class.ENM_OrderAction;
import sample.enum_class.ENM_OrderType;

public class MasterOrderModel
{
    private final int id;
    private final String symbol;
    private final double total_quantity;

    public MasterOrderModel(int id,String symbol,double total_quantity)
    {
        this.id = id;
        this.symbol = symbol;
        this.total_quantity = total_quantity;
    }

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getTotal_quantity() {
        return total_quantity;
    }
}
