package com.stitch_house_market.liqpay_invoice_maker;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	
	private static final int SCENE_HEIGHT = 250;
	private static final int SCENE_WIDTH = 320;
	public static final String LOCALE_BUNDLE_LOCATION = "l10n/compose-form";
	private static FXMLLoader loader;

	@Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {
		loader = new FXMLLoader(App.class.getResource("/Fill-in-invoice.fxml"));
		setAppLanguage("ru");
		Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);

		stage.setTitle("Liqpay Invoice Maker");
		stage.setScene(scene);
		stage.show();
    }
	
	public static void setAppLanguage(String language) {
		loader.setResources(ResourceBundle.getBundle(LOCALE_BUNDLE_LOCATION, new Locale(language)));
	}

    public static void main(String[] args) {
        launch();
    }

}