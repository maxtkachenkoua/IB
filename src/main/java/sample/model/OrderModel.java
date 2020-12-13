package sample.model;

import sample.enum_class.ENM_OrderAction;
import sample.enum_class.ENM_OrderType;

import java.math.BigDecimal;

public class OrderModel
{
    private String symbol;
    private String exchange;
    private int orderId;
    private ENM_OrderAction action;
    private BigDecimal quantity;
    private BigDecimal price;
    private ENM_OrderType orderType;

    public OrderModel()
    {

    }

    public ENM_OrderType getOrderType() {
        return orderType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public ENM_OrderAction getAction() {
        return action;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getExchange() {
        return exchange;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setAction(ENM_OrderAction action) {
        this.action = action;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setOrderType(ENM_OrderType orderType) {
        this.orderType = orderType;
    }
}
