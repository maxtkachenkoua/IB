package sample.utility;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.function.UnaryOperator;

public class Utility {

    public static final String BACKGROUND_COLOR = "#44566F";
    public static final String DARK_BACKGROUND_COLOR = "#38485F";
    public static final String LIGHT_BACKGROUND_COLOR = "#44566F";
    public static final String WHITE_COLOR = "#FFFFFF";
    public static final String GREEN_COLOR = "#028f0f";
    public static final String RED_COLOR = "#ff0000";

    public static void showAlertWithConfirmation(String alertMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert");
        alert.setContentText(alertMessage);
        alert.showAndWait().ifPresent((btnType) -> {

        });
    }

    public static Background getBackgroundColor(String color)
    {
        return new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY));
    }
    public static String getFontColorStyle(String color)
    {
        return "-fx-text-fill: "+color;
    }
    public static String getButtonStyle(String bgColor,String fontColor)
    {
        return "-fx-text-fill: "+fontColor+";-fx-background-color: "+bgColor;
    }

    public static String getBackgroundStyle(String bgColor)
    {
        return "-fx-background-color: "+bgColor;
    }

    public static String getControlInnerBackgroundStyle(String bgColor)
    {
        return "-fx-control-inner-background: "+bgColor;
    }

    public static final UnaryOperator<TextFormatter.Change> NUMERIC_FILTER = change -> {
        String text = change.getText();

        if (text.matches("[0-9\\.]*")) {
            return change;
        }

        return null;
    };

    public static final UnaryOperator<TextFormatter.Change> NUMERIC_PERCENTAGE_FILTER = change -> {
        String text = change.getText();

        if (text.matches("[0-9\\.\\%]*")) {
            return change;
        }

        return null;
    };

    public static double getSizeFromPercentage(double size, int percentage)
    {
        return (size*percentage)/100;
    }

}
