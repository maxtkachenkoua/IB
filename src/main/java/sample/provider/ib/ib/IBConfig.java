package sample.provider.ib.ib;

/**
 * configuration for IB
 */

public class IBConfig
{

    private static IBConfig instance = null;
    private int ibPort = 7497; //IB port for socket connection
    private int masterClientId = 100; //master client id for IB connection
    private String ipAddress = "127.0.0.1";

    private IBConfig()
    {

    }

    public static IBConfig getInstance()
    {
        if (instance == null)
            instance = new IBConfig();
        return instance;
    }

    /**
     * Get the port where IB connects
     */
    public int getIBPort()
    {
        return this.ibPort;
    }

    /**
     * Set Connection port ffor IB API
     */
    public void setIBPort(int x)
    {
        this.ibPort = x;
    }

    /**
     * set master client id for IB API
     * must match the same on TWS
     */
    public void setMasterClientId(int x)
    {
        this.masterClientId = x;
    }

    /**
     * get the master client id for connection to tws
     */
    public int getMasterClientId()
    {
        return this.masterClientId;
    }

    /**
     * Get the connection ip address for tws
     */
    public String getConnectionIP()
    {
        return this.ipAddress;
    }


    /**
     * Set the connection ip for tws connection
     * use only when connecting to a remote session
     */
    public void setConectionIP(String ip)
    {
        this.ipAddress = ip;
    }
}
