package sample.subscriber.panel.stop_loss;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Font;
import org.json.JSONArray;
import sample.Controller;
import sample.Main;
import sample.abstract_class.AB_PanelUI;
import sample.database.DatabaseHandler;
import sample.enum_class.ENM_OrderAction;
import sample.enum_class.ENM_OrderType;
import sample.model.OrderModel;
import sample.model.OrderStatusModel;
import sample.provider.IBHandler;
import sample.utility.Utility;

import java.math.BigDecimal;

import static sample.enum_class.ENM_OrderPlaceType.STOP_LOSS;
import static sample.enum_class.ENM_OrderStatus.PENDING;
import static sample.utility.Utility.*;

public class StopLossPanel extends AB_PanelUI
{

    private static StopLossPanel _instance;

    Label title;
    TextField txtStopLoss;
    private Integer orderId = null;

    public static StopLossPanel getInstance()
    {
        if(_instance == null)
        {
            _instance = new StopLossPanel();
        }
        return _instance;
    }

    StopLossPanel()
    {
        super();
    }


    @Override
    protected void settings() {
        super.settings();

        setAlignment(Pos.CENTER);
        setSpacing(10);
        setMaxHeight(Utility.getSizeFromPercentage(Main.window.getHeight(),20));
        setBackground(getBackgroundColor(BACKGROUND_COLOR));
    }

    @Override
    protected void initialization() {
        super.initialization();

        title = new Label("Stop Loss");
        title.setStyle(getFontColorStyle(WHITE_COLOR));
        title.setFont(Font.font("Arial", 20));

        txtStopLoss = new TextField();
        txtStopLoss.setPromptText("Stop Loss");
        txtStopLoss.setAlignment(Pos.CENTER);
        txtStopLoss.setMaxWidth(120);

        TextFormatter<String> textFormatter = new TextFormatter<>(NUMERIC_PERCENTAGE_FILTER);
        txtStopLoss.setTextFormatter(textFormatter);


        getChildren().addAll(title,txtStopLoss);
    }

    @Override
    protected void reset() {
        super.reset();
    }

    @Override
    public void orderStatus(OrderStatusModel orderStatusModel) {
        super.orderStatus(orderStatusModel);
    }



    public boolean validate(boolean isAlert)
    {
        if(txtStopLoss.getText().isBlank()) {
            System.out.println("Please enter stop loss price/percentage");

            if(isAlert)
            {
                showAlertWithConfirmation("Stop loss price/percentage not inserted.");
            }
            return  false;
        }
        else
        {
            if(txtStopLoss.getText().contains("%"))
            {
                if(!txtStopLoss.getText().substring(txtStopLoss.getText().length() - 1).equals("%"))
                {
                    if(isAlert)
                    {
                        showAlertWithConfirmation("Stop loss price/percentage not inserted correctly.");
                    }
                    return  false;
                }
                else
                {
                    try {
                        double sl_percentage = Double.parseDouble(txtStopLoss.getText().replaceFirst("%","").trim());
                        if(sl_percentage > 100)
                        {
                            if(isAlert)
                            {
                                showAlertWithConfirmation("Stop loss percentage must between 0 to 100.");
                            }
                            return  false;
                        }
                    }
                    catch (Exception e)
                    {
                        if(isAlert)
                        {
                            showAlertWithConfirmation("Stop loss percentage not inserted correctly.");
                        }
                        return  false;
                    }
                }
            }
            else
            {
                try {
                    Double.parseDouble(txtStopLoss.getText().trim());
                }
                catch (Exception e)
                {
                    if(isAlert)
                    {
                        showAlertWithConfirmation("Stop loss price not inserted correctly.");
                    }
                    return  false;
                }
            }
        }
        return true;
    }

    /**
     * It will use to place stop loss order.
     */
    public boolean placeOrder(int order_master_id, String order_place_type, double previous_order_price)
    {
        if(validate(false) || !order_place_type.isEmpty())
        {
            // TODO - this function will call whenever status will updated.
            // TODO - cancel previous order and place new order.

            /// this query use for getting exchange and symbol. its require for order placing.
            JSONArray jsonArray = DatabaseHandler.getInstance().executeQuery("SELECT omt.*, tmt.* from order_master_table as omt, ticker_master_table as tmt where omt.ticker_master_id = tmt.id and omt.id = "+order_master_id+";");
            if(jsonArray.length() > 0) {
                String exchange = jsonArray.getJSONObject(0).getString("exchange");
                String symbol = jsonArray.getJSONObject(0).getString("symbol");

                // this query is use to decide order quantity and price.
                JSONArray jsonArrayOrderTable = DatabaseHandler.getInstance().executeQuery("SELECT * from order_table where order_master_id = " + order_master_id + " and order_place_type not like '%STOP_LOSS%';");
                if (jsonArrayOrderTable.length() > 0) {

                    // it will call once before placing all distribution order.
                    Controller.getInstance().setupForNextValidOrderId();

                    double total_buy_filled_quantity = 0;
                    double total_buy_price = 0;
                    double total_sell_filled_quantity = 0;

                    for (int i = 0; i < jsonArrayOrderTable.length(); i++) {

                        if (jsonArrayOrderTable.getJSONObject(i).getString("action").equalsIgnoreCase("BUY")) {
                            total_buy_filled_quantity += (jsonArrayOrderTable.getJSONObject(i).getDouble("order_quantity") - jsonArrayOrderTable.getJSONObject(i).getDouble("remaining_quantity"));
                            total_buy_price += (jsonArrayOrderTable.getJSONObject(i).getDouble("order_price") * (jsonArrayOrderTable.getJSONObject(i).getDouble("order_quantity") - jsonArrayOrderTable.getJSONObject(i).getDouble("remaining_quantity")));
                        }

                        if (jsonArrayOrderTable.getJSONObject(i).getString("action").equalsIgnoreCase("SELL")) {
                            total_sell_filled_quantity += (jsonArrayOrderTable.getJSONObject(i).getDouble("order_quantity") - jsonArrayOrderTable.getJSONObject(i).getDouble("remaining_quantity"));
                        }

                    }

                    String insertQuery = "";

                    double order_quantity = (total_buy_filled_quantity - total_sell_filled_quantity);

                    double order_price = 0;

                    if(order_place_type.isEmpty())
                    {
                        if(txtStopLoss.getText().contains("%")) {
                            order_place_type = txtStopLoss.getText().trim() + STOP_LOSS;
                        }
                        else
                        {
                            order_place_type = STOP_LOSS.toString();
                            order_price = Double.parseDouble(txtStopLoss.getText().trim());
                        }
                    }

                    if(order_price == 0)
                    {
                        if(order_place_type.contains("%"))
                        {
                            double sl_value = Double.parseDouble(order_place_type.replace("%"+STOP_LOSS, ""));
                            order_price = (total_buy_filled_quantity == 0)?0:(total_buy_price / total_buy_filled_quantity);
                            order_price = order_price - (order_price * sl_value / 100);
                        }
                        else
                        {
                            order_price = previous_order_price;
                        }
                    }

                    // if order place type contain % that means calculate percentage wise price otherwise use previous order price.

                    int ib_order_id = IBHandler.getInstance().getOrderId();
                    ENM_OrderAction action = ENM_OrderAction.SELL;
                    ENM_OrderType order_type = ENM_OrderType.STOP_LOSS;
                    String order_status = PENDING.toString();
                    double remaining_quantity = order_quantity;

                    // it will use to store reference.
                    this.orderId = ib_order_id;

                    // order place.
                    OrderModel orderModel = new OrderModel();
                    orderModel.setOrderId(ib_order_id);
                    orderModel.setSymbol(symbol);
                    orderModel.setExchange(exchange);
                    orderModel.setQuantity(BigDecimal.valueOf(order_quantity));
                    orderModel.setPrice(BigDecimal.valueOf(order_price));
                    orderModel.setOrderType(order_type);
                    orderModel.setAction(action);

                    IBHandler.getInstance().orderPlace(orderModel);

                    insertQuery = "INSERT INTO order_table " +
                            "(id,order_master_id,action,order_type,order_place_type,order_quantity,order_price,order_status,remaining_quantity,timestamp) " +
                            "VALUES ";
                    insertQuery += "(" + ib_order_id + "," + order_master_id + ",'" + action + "','" + order_type + "','" + order_place_type + "'," + order_quantity + "," + order_price + ",'" + order_status + "'," + remaining_quantity + ",NOW());";

                    insertQuery = insertQuery.substring(0, insertQuery.length() - 1) + ";";
                    DatabaseHandler.getInstance().executeUpdate(insertQuery);
                }
            }

            return  true;
        }

        return  false;
    }
}
