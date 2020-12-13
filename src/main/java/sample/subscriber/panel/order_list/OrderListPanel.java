package sample.subscriber.panel.order_list;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.json.JSONArray;
import sample.Main;
import sample.abstract_class.AB_PanelUI;
import sample.database.DatabaseHandler;
import sample.model.MasterOrderModel;
import sample.model.OrderStatusModel;
import sample.subscriber.panel.order_list.components.OrderListCell;
import sample.utility.Utility;

import static sample.utility.Utility.*;

public class OrderListPanel extends AB_PanelUI
{

    private static OrderListPanel _instance;
    ObservableList<MasterOrderModel> listMasterOrderModel;

    public static OrderListPanel getInstance()
    {
        if(_instance == null)
        {
            _instance = new OrderListPanel();
        }
        return _instance;
    }

    OrderListPanel()
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

        JSONArray jsonTickerData = DatabaseHandler.getInstance().executeQuery("Select omt.id, tmt.symbol, omt.total_quantity from order_master_table as omt, ticker_master_table as tmt where omt.ticker_master_id = tmt.id order by timestamp desc;");

        if(listMasterOrderModel == null)
        {
            listMasterOrderModel = FXCollections.observableArrayList();
        }

        listMasterOrderModel.clear();

        MasterOrderModel masterOrderModel = null;
        for(int i=0;i<jsonTickerData.length();i++)
        {
            int id = jsonTickerData.getJSONObject(i).getInt("id");
            String symbol = jsonTickerData.getJSONObject(i).getString("symbol");
            double totalQuantity = jsonTickerData.getJSONObject(i).getDouble("total_quantity");

            masterOrderModel = new MasterOrderModel(id,symbol,totalQuantity);
            listMasterOrderModel.add(masterOrderModel);
        }

        final ListView<MasterOrderModel> listView = new ListView<MasterOrderModel>(listMasterOrderModel);
        listView.setCellFactory(new Callback<ListView<MasterOrderModel>, ListCell<MasterOrderModel>>() {
            @Override
            public ListCell<MasterOrderModel> call(ListView<MasterOrderModel> listView) {
                return new OrderListCell();
            }
        });

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(listView.getSelectionModel() != null && listView.getSelectionModel().getSelectedItem() != null) {
                    System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem().getSymbol());
                }
            }
        });

        listView.setMinHeight(Utility.getSizeFromPercentage(Main.window.getHeight(),80));

        getChildren().add(listView);
    }

    @Override
    protected void reset() {
        super.reset();
    }

    @Override
    public void orderStatus(OrderStatusModel orderStatusModel) {
        super.orderStatus(orderStatusModel);
    }


}
