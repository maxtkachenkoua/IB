package sample.model;

public class BalanceModel
{
    private final int reqId;
    private final String account;
    private final String tag;
    private final String value;
    private final String currency;

    public BalanceModel(int reqId, String account, String tag, String value, String currency)
    {

        this.reqId = reqId;
        this.account = account;
        this.tag = tag;
        this.value = value;
        this.currency = currency;
    }


    public String getCurrency() {
        return currency;
    }

    public String getValue() {
        return value;
    }

    public String getTag() {
        return tag;
    }

    public String getAccount() {
        return account;
    }

    public int getReqId() {
        return reqId;
    }
}
