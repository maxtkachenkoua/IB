package sample.subscriber.panel.info;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import sample.Main;
import sample.abstract_class.AB_PanelUI;
import sample.model.BalanceModel;
import sample.model.OrderStatusModel;
import sample.utility.Utility;

import static sample.utility.Utility.*;

public class InfoPanel extends AB_PanelUI
{

    private static InfoPanel _instance;

    Label lbTitle;

    public static InfoPanel getInstance()
    {
        if(_instance == null)
        {
            _instance = new InfoPanel();
        }
        return _instance;
    }

    InfoPanel()
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

        lbTitle = new Label();
        lbTitle.setText("Info will be here.");
        lbTitle.setStyle(getFontColorStyle(WHITE_COLOR));
        lbTitle.setFont(Font.font("Arial", 20));

        getChildren().add(lbTitle);
    }

    @Override
    protected void reset() {
        super.reset();
    }

    @Override
    public void orderStatus(OrderStatusModel orderStatusModel) {
        super.orderStatus(orderStatusModel);
    }


    @Override
    public void currentBalance(BalanceModel balanceModel) {
        super.currentBalance(balanceModel);

//        System.out.println("balance ::: "+balanceModel.getValue());
    }
}
