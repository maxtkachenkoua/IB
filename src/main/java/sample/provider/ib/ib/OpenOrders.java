package sample.provider.ib.ib;

import com.ib.client.Order;

import java.util.concurrent.ConcurrentHashMap;

public class OpenOrders {

    private static OpenOrders instance = null;
    private ConcurrentHashMap<String, Order> orderConcurrentHashMap;

    private OpenOrders()
    {
        orderConcurrentHashMap = new ConcurrentHashMap<>();
    }

    public static OpenOrders getInstance()
    {
        if(instance == null)
            instance = new OpenOrders();
        return instance;
    }

}
