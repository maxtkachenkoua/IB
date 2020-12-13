package sample.provider.ib.ib;

public class IdGenerator {

    private static IdGenerator instance = null;
    private static int counter = 1224;

    private IdGenerator()
    {

    }

    public static IdGenerator getInstance()
    {
        if(instance == null)
            instance = new IdGenerator();
        return instance;
    }

    /**
     * @return Int next valid id
     * */
    public synchronized int getCounter()
    {
        return counter++;
    }

    public void updateId(int id)
    {
        counter = id;
    }

}
