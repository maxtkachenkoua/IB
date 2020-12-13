package sample.provider.ib;

import org.ta4j.core.Decimal;

public class Config {

    private static Config instance = null;
    private Decimal allocatedAsset = Decimal.valueOf(1000000);

    private Config()
    {

    }

    public static Config getInstance()
    {
        if(instance == null)
            instance = new Config();
        return instance;
    }

    public Decimal getAllocatedAsset() {
        return allocatedAsset;
    }

    public void setAllocatedAsset(Decimal allocatedAsset) {
        this.allocatedAsset = allocatedAsset;
    }
}
