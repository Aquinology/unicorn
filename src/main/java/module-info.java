module poui.unicorn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens poui.unicorn to javafx.fxml;
    exports poui.unicorn;
}