package sample.abstract_class;

import javafx.scene.layout.VBox;
import sample.interface_class.IF_DataSubscriber;
import sample.model.BalanceModel;
import sample.model.OrderStatusModel;

public class AB_PanelUI extends VBox implements IF_DataSubscriber
{

  public AB_PanelUI() {
    initialization();
    settings();
    reset();
  }

  protected void settings()
  {

  }

  protected void initialization()
  {

  }

  protected void reset()
  {

  }


  @Override
  public void currentBalance(BalanceModel balanceModel) {

  }

  @Override
  public void orderStatus(OrderStatusModel orderStatusModel) {

  }
}
