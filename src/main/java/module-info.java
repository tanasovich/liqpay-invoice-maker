module com.stitch_house_market.liqpay_invoice_maker {
    requires javafx.controls;
    requires javafx.fxml;
	requires com.liqpay;

    opens com.stitch_house_market.liqpay_invoice_maker to javafx.fxml;
    exports com.stitch_house_market.liqpay_invoice_maker;
}