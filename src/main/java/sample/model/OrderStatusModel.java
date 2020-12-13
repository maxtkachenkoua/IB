package sample.model;

public class OrderStatusModel
{
    private final int orderId;
    private final String status;
    private final double filled;
    private final double remaining;
    private final double avgFillPrice;
    private final int permId;
    private final int parentId;
    private final double lastFillPrice;
    private final int clientId;
    private final String whyHeld;
    private final double mktCapPrice;

    public OrderStatusModel(int orderId, String status, double filled,
                            double remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld, double mktCapPrice)
    {
      this.orderId = orderId;
      this.status = status;
      this.filled = filled;
      this.remaining = remaining;
      this.avgFillPrice = avgFillPrice;
      this.permId = permId;
      this.parentId = parentId;
      this.lastFillPrice = lastFillPrice;
      this.clientId = clientId;
      this.whyHeld = whyHeld;
      this.mktCapPrice = mktCapPrice;
    }

    public double getMktCapPrice() {
        return mktCapPrice;
    }

    public String getWhyHeld() {
        return whyHeld;
    }

    public int getClientId() {
        return clientId;
    }

    public double getLastFillPrice() {
        return lastFillPrice;
    }

    public int getParentId() {
        return parentId;
    }

    public int getPermId() {
        return permId;
    }

    public double getAvgFillPrice() {
        return avgFillPrice;
    }

    public double getRemaining() {
        return remaining;
    }

    public double getFilled() {
        return filled;
    }

    public String getStatus() {
        return status;
    }

    public int getOrderId() {
        return orderId;
    }
}
