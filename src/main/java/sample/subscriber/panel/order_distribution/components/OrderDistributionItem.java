package sample.subscriber.panel.order_distribution.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import sample.enum_class.ENM_OrderType;
import sample.model.OrderDistributionModel;
import sample.subscriber.panel.order_distribution.OrderDistributionPanel;
import sample.utility.Utility;

import static sample.utility.Utility.*;


public class OrderDistributionItem extends HBox
{

    public final OrderDistributionModel orderDistributionModel;

    TextField txtPriceLimit;
    TextField txtPercentageOfQuantity;
    Label lblStatus;
    Label lblPrice;
    Button btnRemove;
    ObservableList<String> options;
    ComboBox cmbOrderType;
    TextFormatter<String> textFormatter;

    public OrderDistributionItem(OrderDistributionModel orderDistributionModel)
    {
        this.orderDistributionModel = orderDistributionModel;
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
        txtPriceLimit.setText((orderDistributionModel.getPrice() == null)?"":String.valueOf(orderDistributionModel.getPrice()));

        txtPercentageOfQuantity = new TextField();
        txtPercentageOfQuantity.setPromptText("Quantity (%)");
        txtPercentageOfQuantity.setAlignment(Pos.CENTER);
        textFormatter = new TextFormatter<>(NUMERIC_FILTER);
        txtPercentageOfQuantity.setTextFormatter(textFormatter);
        txtPercentageOfQuantity.setMaxWidth(120);
        txtPercentageOfQuantity.setText((orderDistributionModel.getQuantity() == null)?"":String.valueOf(orderDistributionModel.getQuantity()));

        btnRemove = new Button("-");
        btnRemove.setStyle(Utility.getButtonStyle(Utility.RED_COLOR,Utility.WHITE_COLOR));

        lblPrice = new Label("");
        lblPrice.setMaxWidth(120);

        lblStatus = new Label("PENDING");
        lblStatus.setStyle(getFontColorStyle(WHITE_COLOR));
        lblStatus.setMinWidth(70);

        options = FXCollections.observableArrayList(ENM_OrderType.LIMIT.toString(),ENM_OrderType.MARKET.toString());
        cmbOrderType = new ComboBox(options);
        cmbOrderType.getSelectionModel().select(orderDistributionModel.getOrderType() == ENM_OrderType.LIMIT?0:1);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(lblPrice);
        stackPane.getChildren().addAll(txtPriceLimit);

        getChildren().add(cmbOrderType);
        getChildren().add(stackPane);
        getChildren().add(txtPercentageOfQuantity);
        getChildren().add(lblStatus);
        getChildren().add(btnRemove);


        btnRemove.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                OrderDistributionPanel.getInstance().removeItem(OrderDistributionItem.this);
            }
        });

        cmbOrderType.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    System.out.println("newValue == "+newValue);

                    if(cmbOrderType.getSelectionModel().getSelectedIndex() == 1)
                    {
                        txtPriceLimit.setVisible(false);
                        lblPrice.setVisible(true);
                    }
                    else
                    {
                        txtPriceLimit.setVisible(true);
                        lblPrice.setVisible(false);
                    }
                }
        );
    }

    public double getPriceLimit()
    {
        try {
            if(txtPriceLimit.isVisible()) {
                return Double.parseDouble(txtPriceLimit.getText());
            }
            else
            {
                return 0;
            }
        }catch (Exception e)
        {
            return 0;
        }
    }

    public double getPercentageOfQuantity()
    {
        try {
            return Double.parseDouble(txtPercentageOfQuantity.getText());
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public ENM_OrderType getOrderType()
    {
        return cmbOrderType.getSelectionModel().getSelectedIndex() == 0?ENM_OrderType.LIMIT:ENM_OrderType.MARKET;
    }
}
