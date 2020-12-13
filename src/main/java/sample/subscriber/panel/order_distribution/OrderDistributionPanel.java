package sample.subscriber.panel.order_distribution;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.json.JSONArray;
import sample.Controller;
import sample.Main;
import sample.abstract_class.AB_PanelUI;
import sample.database.DatabaseHandler;
import sample.enum_class.ENM_OrderAction;
import sample.enum_class.ENM_OrderPlaceType;
import sample.enum_class.ENM_OrderStatus;
import sample.enum_class.ENM_OrderType;
import sample.model.OrderDistributionModel;
import sample.model.OrderModel;
import sample.model.OrderStatusModel;
import sample.provider.IBHandler;
import sample.subscriber.panel.order_distribution.components.OrderDistributionItem;
import sample.utility.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static sample.utility.Utility.*;

public class OrderDistributionPanel extends AB_PanelUI
{
    private static OrderDistributionPanel _instance;

    private static final int INITIAL_SET = 3;
    // INITIAL_PERCENTAGE length must be equal to INITIAL_SET/
    private static final BigDecimal[] INITIAL_PERCENTAGE = {BigDecimal.valueOf(50),BigDecimal.valueOf(25),BigDecimal.valueOf(25)};

    List<OrderDistributionItem> listOrderDistributionItem;
    ScrollPane scrollPane;
    HBox hBoxHeader;
    VBox vBoxOD;
    VBox vBoxOrderListHolder;
    Label title;
    TextField txtSetValue ;
    Button btnSet;
    Button btnAdd;
    Button btnModify;

    public static OrderDistributionPanel getInstance()
    {
        if(_instance == null)
        {
            _instance = new OrderDistributionPanel();
        }
        return _instance;
    }

    OrderDistributionPanel()
    {
        super();
    }

    @Override
    protected void settings() {
        super.settings();

        setMaxHeight(Utility.getSizeFromPercentage(Main.window.getHeight(),80));
        setBackground(getBackgroundColor(BACKGROUND_COLOR));
    }

    @Override
    protected void initialization() {
        super.initialization();

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        vBoxOrderListHolder = new VBox();
        vBoxOrderListHolder.setAlignment(Pos.CENTER);
        scrollPane.setContent(vBoxOrderListHolder);
        vBoxOrderListHolder.setSpacing(10);

        txtSetValue = new TextField();
        txtSetValue.setPromptText("Count");
        txtSetValue.setMaxWidth(70);
        txtSetValue.setText(String.valueOf(INITIAL_SET));

        btnSet = new Button("SET");

        btnAdd = new Button("ADD");

        btnModify = new Button("MODIFY");

        title = new Label("Order Distribution");
        title.setStyle(getFontColorStyle(WHITE_COLOR));
        title.setFont(Font.font("Arial", 20));

        hBoxHeader = new HBox();
        hBoxHeader.getChildren().addAll(title,txtSetValue, btnSet,btnModify);
        hBoxHeader.setAlignment(Pos.CENTER);
        hBoxHeader.setSpacing(10);

        vBoxOD = new VBox();
        vBoxOD.getChildren().addAll(hBoxHeader,scrollPane,btnAdd);
        vBoxOD.setAlignment(Pos.CENTER);
        vBoxOD.setSpacing(10);
        vBoxOD.setPadding(new Insets(10));

        List<OrderDistributionItem> listOdItem = getOrderDistributionList(INITIAL_SET);
        if(listOdItem != null) {
            listOrderDistributionItem = listOdItem;
            vBoxOrderListHolder.getChildren().addAll(listOrderDistributionItem);
        }
        vBoxOrderListHolder.setBackground(getBackgroundColor(BACKGROUND_COLOR));
        vBoxOrderListHolder.setPadding(new Insets(10));

        getChildren().addAll(vBoxOD);


        btnSet.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    int count = Integer.parseInt(txtSetValue.getText().trim());
                    List<OrderDistributionItem> listOdItem = getOrderDistributionList(count);

                    if(listOdItem != null) {
                        listOrderDistributionItem.clear();
                        vBoxOrderListHolder.getChildren().clear();

                        listOrderDistributionItem.addAll(listOdItem);
                        vBoxOrderListHolder.getChildren().addAll(listOdItem);
                    }
                }
                catch (Exception e)
                {
                    showAlertWithConfirmation(e.getMessage());
                }
            }
        });

        btnAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                List<OrderDistributionItem> listOdItem = getOrderDistributionList(1);
                if(listOdItem != null) {
                    listOrderDistributionItem.addAll(listOdItem);
                    vBoxOrderListHolder.getChildren().addAll(listOdItem);
                }
            }
        });

        btnModify.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                validate(true);
            }
        });
    }


    private List<OrderDistributionItem> getOrderDistributionList(int count)
    {
        if(listOrderDistributionItem == null || count != listOrderDistributionItem.size()) {
            List<OrderDistributionItem> listItems = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                OrderDistributionModel orderDistributionModel = new OrderDistributionModel();
                orderDistributionModel.setOrderType(ENM_OrderType.LIMIT);
                orderDistributionModel.setPrice(null);
                orderDistributionModel.setQuantity(count == INITIAL_SET?INITIAL_PERCENTAGE[i]:null);
                OrderDistributionItem orderDistributionItem = new OrderDistributionItem(orderDistributionModel);
                listItems.add(orderDistributionItem);
            }
            return listItems;
        }
       return null;
    }


    public void removeItem(OrderDistributionItem orderDistributionItem)
    {
       listOrderDistributionItem.remove(orderDistributionItem);
       vBoxOrderListHolder.getChildren().remove(orderDistributionItem);
    }


    @Override
    protected void reset() {
        super.reset();
    }

    @Override
    public void orderStatus(OrderStatusModel orderStatusModel) {
        super.orderStatus(orderStatusModel);
    }


    /**
     * It will validate all list with price and quantity.
     * @param isAlert
     * @return
     */
    public boolean validate(boolean isAlert)
    {
        double remainingPercentageCount = getRemainingPercentage();

        title.setText("Order Distribution");
        if(remainingPercentageCount != 0) {
            System.out.println("Remaining Percentage count : "+ remainingPercentageCount);

            if(isAlert)
            {
                showAlertWithConfirmation("Order Distribution Quantity Percentage must be 100.");
            }
            return  false;
        }
        if(isPriceMissing()) {
            if(isAlert)
            {
                showAlertWithConfirmation("Order Distribution price not inserted.");
            }
            return  false;
        }
        return true;
    }

    /**
     * It will use to place distributed order.
     */
    public boolean placeOrder(int order_master_id)
    {
        if(validate(false)) {
            // TODO - place order

            JSONArray jsonArray = DatabaseHandler.getInstance().executeQuery("SELECT omt.*, tmt.* from order_master_table as omt, ticker_master_table as tmt where omt.ticker_master_id = tmt.id and omt.id = "+order_master_id+";");
            if(jsonArray.length() > 0) {

                // it will call once before placing all distribution order.
                Controller.getInstance().setupForNextValidOrderId();

                double total_quantity = jsonArray.getJSONObject(0).getDouble("total_quantity");
                String exchange = jsonArray.getJSONObject(0).getString("exchange");
                String symbol = jsonArray.getJSONObject(0).getString("symbol");

                StringBuilder insertQuery = new StringBuilder();
                if (listOrderDistributionItem.size() > 0) {
                    for (OrderDistributionItem orderDistributionItem : listOrderDistributionItem)
                    {
                        BigDecimal order_quantity = BigDecimal.valueOf((total_quantity * orderDistributionItem.getPercentageOfQuantity()) / 100);
                        BigDecimal order_price = BigDecimal.valueOf(orderDistributionItem.getPriceLimit());
                        int ib_order_id = IBHandler.getInstance().getOrderId();
                        ENM_OrderAction action = ENM_OrderAction.BUY;
                        ENM_OrderType order_type = orderDistributionItem.getOrderType();
                        ENM_OrderPlaceType order_place_type = ENM_OrderPlaceType.NORMAL;
                        ENM_OrderStatus order_status = ENM_OrderStatus.PENDING;
                        BigDecimal remaining_quantity = order_quantity;


                        OrderModel orderModel = new OrderModel();
                        orderModel.setOrderId(ib_order_id);
                        orderModel.setSymbol(symbol);
                        orderModel.setExchange(exchange);
                        orderModel.setQuantity(order_quantity);
                        orderModel.setPrice(order_price);
                        orderModel.setOrderType(order_type);
                        orderModel.setAction(action);

                        // after placing order its need to assign order id to oditem.
                        orderDistributionItem.orderDistributionModel.setOrderId(ib_order_id);

                        // order place.
                        IBHandler.getInstance().orderPlace(orderModel);

                        if (insertQuery.length() == 0) {
                            insertQuery = new StringBuilder("INSERT INTO order_table " +
                                    "(id,order_master_id,action,order_type,order_place_type,order_quantity,order_price,order_status,remaining_quantity,timestamp) " +
                                    "VALUES ");
                        }
                        insertQuery.append("(").append(ib_order_id).append(",").append(order_master_id).append(",'").append(action).append("','").append(order_type).append("','").append(order_place_type).append("',").append(order_quantity).append(",").append(order_price).append(",'").append(order_status).append("',").append(remaining_quantity).append(",").append("NOW()").append("),");

                    }

                    if (insertQuery.length() > 0) {
                        insertQuery = new StringBuilder(insertQuery.substring(0, insertQuery.length() - 1) + ";");
                        DatabaseHandler.getInstance().executeUpdate(insertQuery.toString());
                    }
                }
            }
            return  true;
        }
        return  false;
    }

    public double getRemainingPercentage()
    {
        double count = 100;
        if(listOrderDistributionItem.size() > 0)
        {
            for(OrderDistributionItem orderDistributionItem : listOrderDistributionItem)
            {
                count = count - orderDistributionItem.getPercentageOfQuantity();
            }
        }
        return count;
    }


    public boolean isPriceMissing()
    {
        if(listOrderDistributionItem.size() > 0)
        {
            for(OrderDistributionItem orderDistributionItem : listOrderDistributionItem)
            {
                if(orderDistributionItem.getOrderType() == ENM_OrderType.LIMIT  && orderDistributionItem.getPriceLimit() <= 0)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
