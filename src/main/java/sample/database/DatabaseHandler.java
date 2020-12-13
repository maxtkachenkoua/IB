package sample.database;


import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class DatabaseHandler {

    private static DatabaseHandler _instance;

    private Connection connection;

    private static final String server = "dev.cr72n5fpq5g8.us-east-1.rds.amazonaws.com";
    private static final String database = "staruh_ib";
    private static final String user = "admin";
    private static final String password = "himanshu123";


    public static DatabaseHandler getInstance() {
        if(_instance == null)
        {
            _instance = new DatabaseHandler();
        }
        return _instance;
    }

    DatabaseHandler()
    {

        getConnection();
    }


    private Connection getConnection()
    {
        if(connection == null) {
            try {

                  Class.forName("com.mysql.cj.jdbc.Driver");
                  connection = DriverManager.getConnection("jdbc:mysql://"+server+"/"+database+"?useSSL=false", user, password);

                if (connection != null) {
                    System.out.println("Connected");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return  connection;
    }


    public Object executeUpdate(String query) {
        try  {
            PreparedStatement statement = getConnection().prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getObject(1);
                }
                else {
                    return null;
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return  null;
    }


    public JSONArray executeQuery(String query)
    {
        JSONArray jsonArray = null;

        try
        {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            try
            {
                ResultSetMetaData metaData = rs.getMetaData();
                int count = metaData.getColumnCount();
                String[] columnName = new String[count];
                jsonArray = new JSONArray();
                while (rs.next())
                {
                    JSONObject jsonObject = new JSONObject();
                    for (int i = 1; i <= count; i++){
                        columnName[i-1] = metaData.getColumnLabel(i);
                        jsonObject.put(columnName[i-1], rs.getObject(i));
                    }
                    jsonArray.put(jsonObject);
                }
            }catch (SQLException ex) {

                System.out.println("resultSet ::: "+ ex.getMessage());
            }

            statement.close();

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return jsonArray;
    }


}