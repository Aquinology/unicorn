module poui.unicorn {
    requires javafx.controls;
    requires javafx.fxml;


    opens poui.unicorn to javafx.fxml;
    exports poui.unicorn;
}