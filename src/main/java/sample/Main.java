package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.subscriber.panel.info.InfoPanel;
import sample.subscriber.panel.order.OrderPanel;
import sample.subscriber.panel.order_distribution.OrderDistributionPanel;
import sample.subscriber.panel.order_list.OrderListPanel;
import sample.subscriber.panel.stop_loss.StopLossPanel;
import sample.subscriber.panel.take_profit.TakeProfitPanel;
import sample.utility.Utility;

public class Main extends Application {

    public static final int STAGE_WIDTH = 1400;
    public static final int STAGE_HEIGHT = 700;

    private SplitPane main_split_pane;
    public static Stage window;

    @Override
    public void start(Stage stage) throws Exception{

        System.out.println("Start");
        window = stage;

        windowSetup();

        // it will start program. it will add subscribers and start ib.
       Controller.getInstance().start();
    }

    private void windowSetup() {

        window.setTitle("Staruh");

        window.setWidth(STAGE_WIDTH);
        window.setHeight(STAGE_HEIGHT);

        main_split_pane = new SplitPane();
        main_split_pane.setOrientation(Orientation.HORIZONTAL);

        SplitPane firstSplitPane = new SplitPane();
        firstSplitPane.setMaxWidth(Utility.getSizeFromPercentage(STAGE_WIDTH,30));
        firstSplitPane.setOrientation(Orientation.VERTICAL);
        firstSplitPane.getItems().add(InfoPanel.getInstance());
        firstSplitPane.getItems().add(OrderListPanel.getInstance());
        main_split_pane.getItems().add(firstSplitPane);


        SplitPane secondSplitPane = new SplitPane();
        secondSplitPane.setMaxWidth(Utility.getSizeFromPercentage(STAGE_WIDTH,40));
        secondSplitPane.setOrientation(Orientation.VERTICAL);
        secondSplitPane.getItems().add(OrderPanel.getInstance());
        secondSplitPane.getItems().add(OrderDistributionPanel.getInstance());
        main_split_pane.getItems().add(secondSplitPane);


        SplitPane thirdSplitPane = new SplitPane();
        thirdSplitPane.setMaxWidth(Utility.getSizeFromPercentage(STAGE_WIDTH,30));
        thirdSplitPane.setOrientation(Orientation.VERTICAL);
        thirdSplitPane.getItems().add(StopLossPanel.getInstance());
        thirdSplitPane.getItems().add(TakeProfitPanel.getInstance());
        main_split_pane.getItems().add(thirdSplitPane);

        // create a scene
        Scene scene = new Scene(main_split_pane, window.getWidth(), window.getHeight());
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
