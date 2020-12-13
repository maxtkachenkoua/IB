package sample.subscriber.panel.order_list.components;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sample.model.MasterOrderModel;

import static sample.utility.Utility.*;

public class OrderListCell extends ListCell<MasterOrderModel> {
    private HBox content;
    private Text symbol;
    private Text lblQuantity;
    private Text quantity;
    private Text lblReturns;
    private Text returns;

    public OrderListCell() {
        super();
        symbol = new Text();
        symbol.setFill(Color.WHITE);

        lblQuantity = new Text("Quantity :");
        lblQuantity.setFill(Color.WHITE);

        quantity = new Text();
        quantity.setFill(Color.WHITE);

        HBox hBoxQuantity = new HBox(lblQuantity, quantity);
        hBoxQuantity.setSpacing(5);

        lblReturns = new Text("Returns :");
        lblReturns.setFill(Color.WHITE);

        returns = new Text();
        returns.setFill(Color.LIGHTGREEN);

        HBox hBoxReturn = new HBox(lblReturns, returns);
        hBoxReturn.setSpacing(5);

        content = new HBox(symbol,new Separator(Orientation.VERTICAL), hBoxQuantity ,new Separator(Orientation.VERTICAL),hBoxReturn);
        content.setSpacing(20);

        setStyle(getControlInnerBackgroundStyle(LIGHT_BACKGROUND_COLOR));

    }

    @Override
    protected void updateItem(MasterOrderModel item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            symbol.setText(item.getSymbol());

            quantity.setText(String.valueOf(item.getTotal_quantity()));
            returns.setText(String.valueOf(item.getTotal_quantity())+"%");

            setStyle(getIndex() % 2 == 0 ? getControlInnerBackgroundStyle(DARK_BACKGROUND_COLOR) : getControlInnerBackgroundStyle(LIGHT_BACKGROUND_COLOR));

            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
