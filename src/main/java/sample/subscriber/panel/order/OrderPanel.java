package sample.subscriber.panel.order;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.json.JSONArray;
import sample.Controller;
import sample.Main;
import sample.abstract_class.AB_PanelUI;
import sample.database.DatabaseHandler;
import sample.enum_class.ENM_OrderQuantityType;
import sample.model.OrderStatusModel;
import sample.utility.ComboBoxAutoComplete;
import sample.utility.Utility;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static sample.utility.Utility.*;

public class OrderPanel extends AB_PanelUI
{
    private static OrderPanel _instance;

    ENM_OrderQuantityType quantityType = ENM_OrderQuantityType.SHARE;

    ComboBox<String> cmbExchangeTicker;
    TextField txtTotalQuantity;
    Button btnNew;
    Button btnExecute;
    ToggleGroup group;

    public static OrderPanel getInstance()
    {
        if(_instance == null)
        {
            _instance = new OrderPanel();
        }
        return _instance;
    }

    OrderPanel()
    {
        super();
    }


    @Override
    protected void settings() {
        super.settings();

        setAlignment(Pos.CENTER);
        setSpacing(10);
        this.setMaxHeight(Utility.getSizeFromPercentage(Main.window.getHeight(),20));
        setBackground(getBackgroundColor(BACKGROUND_COLOR));
    }

    RadioButton rbShare = new RadioButton("Share");
    RadioButton rbUSD = new RadioButton("USD");
    @Override
    protected void initialization() {
        super.initialization();

        cmbExchangeTicker = new ComboBox<>();
        txtTotalQuantity = new TextField();
        btnNew = new Button("NEW");
        btnExecute = new Button("EXECUTE");
        group = new ToggleGroup();
        rbShare = new RadioButton("Share");
        rbUSD = new RadioButton("USD");

        // load data from database.
        loadTickerData();

        cmbExchangeTicker.setPromptText("Select Ticker");

        btnExecute.setStyle(getButtonStyle(GREEN_COLOR,WHITE_COLOR));

        txtTotalQuantity.setPromptText("Total Quantity");
        TextFormatter<String> textFormatter = new TextFormatter<>(NUMERIC_FILTER);
        txtTotalQuantity.setTextFormatter(textFormatter);
        txtTotalQuantity.setAlignment(Pos.CENTER);

        // add a change listener
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {

                RadioButton rb = (RadioButton)group.getSelectedToggle();

                if (rb != null)
                {
                    String selectedText = rb.getText();

                    if(selectedText.equalsIgnoreCase("Share"))
                    {
                        quantityType = ENM_OrderQuantityType.SHARE;
                        // change the label
                        txtTotalQuantity.setPromptText("Total Quantity In Share");
                    }
                    else
                    {
                        quantityType = ENM_OrderQuantityType.USD;
                        // change the label
                        txtTotalQuantity.setPromptText("");
                        txtTotalQuantity.setPromptText("Total Quantity In USD");
                    }

                }
            }
        });

        // add radiobuttons to toggle group
        rbShare.setStyle(getFontColorStyle(WHITE_COLOR));
        rbShare.setToggleGroup(group);
        rbUSD.setStyle(getFontColorStyle(WHITE_COLOR));
        rbUSD.setToggleGroup(group);
        rbShare.setSelected(true);

        HBox topHBox = new HBox();
        topHBox.setSpacing(10);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.getChildren().add(rbShare);
        topHBox.getChildren().add(rbUSD);

        Separator separator = new Separator(Orientation.HORIZONTAL);

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(10);
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.getChildren().add(cmbExchangeTicker);
        bottomHBox.getChildren().add(txtTotalQuantity);
        bottomHBox.getChildren().add(btnNew);
        bottomHBox.getChildren().add(btnExecute);

        getChildren().addAll(topHBox);
        getChildren().addAll(separator);
        getChildren().addAll(bottomHBox);


        cmbExchangeTicker.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty) ;
                if (empty || item == null) {
                    setText("Select Ticker");
                } else {
                    setText(item);
                }
            }
        });

        btnExecute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Controller.getInstance().startPlaceOrder();
            }
        });

        btnNew.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                Controller.getInstance().resetAll();
            }
        });

    }

    @Override
    protected void reset() {
        super.reset();

        cmbExchangeTicker.getItems().clear();
        cmbExchangeTicker.getItems().addAll(mapExchangeTickerData.keySet());
        cmbExchangeTicker.setValue(null);

        new ComboBoxAutoComplete<String> (cmbExchangeTicker);
    }

    @Override
    public void orderStatus(OrderStatusModel orderStatusModel) {
        super.orderStatus(orderStatusModel);
    }


//-------------------------------------- CUSTOM ---------------------------------

    /**
     * validate all fileds and show error message as per flag.
     * @param isAlert
     * @return
     */
    public boolean validate(boolean isAlert)
    {
        if(cmbExchangeTicker.getSelectionModel().getSelectedIndex() < 0 || txtTotalQuantity.getText().isBlank())
        {
            if(isAlert)
            {
                showAlertWithConfirmation("Exchange, Ticker and Total Quantity is mandatory.");
            }
            return  false;
        }
        return true;
    }


    /**
     * It will use to place stop loss order.
     */
    public Number placeOrder()
    {
        if(validate(false))
        {
            int ticker_master_id = mapExchangeTickerData.get(cmbExchangeTicker.getSelectionModel().getSelectedItem().toString());
            double total_quantity = Double.parseDouble(txtTotalQuantity.getText());
            String outsideRTH = "OrderBook";

            // TODO - Start order placing.
            Object key = DatabaseHandler.getInstance().executeUpdate("INSERT INTO order_master_table (ticker_master_id,total_quantity,outside_rth,timestamp) VALUES ("+ticker_master_id+","+total_quantity+",'"+outsideRTH+"',NOW())");

            return (Number) key;
        }

        return  null;
    }


    /**
     * It will load ticker data for combobox.
     */
    Map<String,Integer> mapExchangeTickerData;
     private void loadTickerData()
    {
        if(mapExchangeTickerData == null) {
            mapExchangeTickerData= new ConcurrentHashMap<>();

            JSONArray jsonTickerData = DatabaseHandler.getInstance().executeQuery("Select * from ticker_master_table;");

            for (int i = 0; i < jsonTickerData.length(); i++) {
                int id = jsonTickerData.getJSONObject(i).getInt("id");
                String exchange = jsonTickerData.getJSONObject(i).getString("exchange");
                String symbol = jsonTickerData.getJSONObject(i).getString("symbol");

                mapExchangeTickerData.put(symbol + " - " + exchange, id);

                System.out.println("id : " + id + " - exchange : " + exchange + " - symbol : " + symbol);
            }
        }
    }
}
