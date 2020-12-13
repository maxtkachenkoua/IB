package sample.subscriber.panel.take_profit.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import sample.enum_class.ENM_OrderType;
import sample.model.TakeProfitModel;
import sample.subscriber.panel.order_distribution.OrderDistributionPanel;
import sample.subscriber.panel.take_profit.TakeProfitPanel;
import sample.utility.Utility;

import static sample.utility.Utility.*;


public class TakeProfitItem extends HBox
{

    public final TakeProfitModel takeProfitModel;

    TextField txtPriceLimit;
    TextField txtPercentageOfQuantity;
    Label lblStatus;
    Button btnRemove;
    TextFormatter<String> textFormatter;

    public TakeProfitItem(TakeProfitModel takeProfitModel)
    {
        this.takeProfitModel = takeProfitModel;
        initialization();
        settings();
    }


    private void settings() {
        setSpacing(10);
        setAlignment(Pos.CENTER);
    }

    private void initialization() {

        txtPriceLimit = new TextField();
        txtPriceLimit.setPromptText("Price");
        txtPriceLimit.setAlignment(Pos.CENTER);
        textFormatter = new TextFormatter<>(NUMERIC_FILTER);
        txtPriceLimit.setTextFormatter(textFormatter);
        txtPriceLimit.setMaxWidth(120);
        txtPriceLimit.setText((takeProfitModel.getPrice() == null)?"":String.valueOf(takeProfitModel.getPrice()));

        txtPercentageOfQuantity = new TextField();
        txtPercentageOfQuantity.setPromptText("Quantity (%)");
        txtPercentageOfQuantity.setAlignment(Pos.CENTER);
        textFormatter = new TextFormatter<>(NUMERIC_FILTER);
        txtPercentageOfQuantity.setTextFormatter(textFormatter);
        txtPercentageOfQuantity.setMaxWidth(120);
        txtPercentageOfQuantity.setText((takeProfitModel.getQuantity() == null)?"":String.valueOf(takeProfitModel.getQuantity()));

        btnRemove = new Button("-");
        btnRemove.setStyle(Utility.getButtonStyle(Utility.RED_COLOR,Utility.WHITE_COLOR));

        lblStatus = new Label("PENDING");
        lblStatus.setStyle(getFontColorStyle(WHITE_COLOR));
        lblStatus.setMinWidth(70);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(txtPriceLimit);

        getChildren().add(stackPane);
        getChildren().add(txtPercentageOfQuantity);
        getChildren().add(lblStatus);
        getChildren().add(btnRemove);


        btnRemove.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TakeProfitPanel.getInstance().removeItem(TakeProfitItem.this);
            }
        });

    }

    public double getPercentageQuantity()
    {
        try {
            return Double.parseDouble(txtPercentageOfQuantity.getText());
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public double getPriceLimit()
    {
        try {
            return Double.parseDouble(txtPriceLimit.getText());
        }
        catch (Exception e)
        {
            return 0;
        }

    }
}
