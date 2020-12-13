package sample.model;

import sample.enum_class.ENM_OrderType;

import java.math.BigDecimal;

public class TakeProfitModel
{
    private int orderId;
    private BigDecimal quantity;
    private BigDecimal price;
    private ENM_OrderType orderType;

    public TakeProfitModel( )
    {

    }


    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ENM_OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(ENM_OrderType orderType) {
        this.orderType = orderType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
