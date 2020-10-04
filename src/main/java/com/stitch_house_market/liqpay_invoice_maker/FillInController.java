package com.stitch_house_market.liqpay_invoice_maker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class FillInController implements Initializable {

	private final ObservableResourceFactory resourceFactory = new ObservableResourceFactory();
	private InvoiceService invoiceService;
	
	@FXML TextField phoneNumber;
	@FXML CheckMenuItem testMode;
	@FXML Menu configuration;
	@FXML Menu language;
	@FXML ToggleGroup toggleLangGroup;
	@FXML RadioMenuItem uaLanguage;
	@FXML RadioMenuItem ruLanguage;
	@FXML RadioMenuItem enLanguage;
	@FXML TextField email;
	@FXML TextField description;
	@FXML TextField price;
	@FXML ComboBox<Currency> currency;
	@FXML Button composeInvoice;
	@FXML TextField invoiceURL;
	@FXML Button copyInvoiceURL;
	
	@FXML
	protected void showTestDialog(ActionEvent actionEvent) {
		String contentText =
				(testMode.isSelected())?
						resourceFactory.getResources()
							.getString("test-mode-on-content")
						: resourceFactory.getResources()
							.getString("test-mode-off-content");
		
		Alert alert;
		alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(resourceFactory.getResources()
				.getString("test-mode-header"));
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
	@FXML
	protected void toggleAppLanguage(ActionEvent actionEvent) {
		resourceFactory.setResources(
				ResourceBundle.getBundle(App.LOCALE_BUNDLE_LOCATION,
						(Locale) toggleLangGroup.getSelectedToggle()
						.getUserData()));
	}

	@FXML
	protected void sendMakeInvoiceRequest(ActionEvent event) throws NumberFormatException, Exception {
		invoiceURL.setText("");
		
		Invoice invoice = new Invoice(email.getText(),
				Double.valueOf(price.getText()), currency.getValue());
		invoice.setPhoneNumber(phoneNumber.getText());
		invoice.setDescription(description.getText());
		invoice.setTestMode(testMode.isSelected());
		
		Map<String,Object> result = invoiceService.createInvoiceFrom(invoice);
		if (String.valueOf(result.get("result")).equals("ok")) {
			invoiceURL.setText(String.valueOf(result.get("href")));
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(resourceFactory.getResources()
					.getString("invoice-sent-header"));
			alert.setContentText(resourceFactory.getResources()
					.getString("invoice-sent-content"));
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(resourceFactory.getResources()
					.getString("invoice-not-sent-header"));
			alert.setContentText(resourceFactory.getResources()
					.getString("invoice-not-sent-content"));
		}
	}
	
	@FXML
	protected void copyURLToClipboard(ActionEvent event) {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(invoiceURL.getText());
		clipboard.setContent(content);
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(resourceFactory.getResources()
				.getString("copy-link-header"));
		alert.setContentText(resourceFactory.getResources()
				.getString("copy-link-content"));
		alert.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resourceFactory.setResources(ResourceBundle
				.getBundle(App.LOCALE_BUNDLE_LOCATION));
		
		bindComposeButtonWithEmailAndPrice();
		
		bindResourcesAndTexts();
		
		bindCopyAndInvoiceURL();
		
		populateLocales();
		
		populateCurrencies();
		
		loadApiKeys();
	}

	private void loadApiKeys() {
		InputStream propertiesFile = App.class.getResourceAsStream("/api-keys.properties");
		Properties properties = new Properties();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(propertiesFile))) {
			properties.load(reader);
			invoiceService = new InvoiceService(
					properties.getProperty("public-key"),
					properties.getProperty("private-key"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void populateCurrencies() {
		Currency currencies[] = new Currency[] {
			Currency.getInstance("UAH"),
			Currency.getInstance("USD"),
			Currency.getInstance("RUB"),
			Currency.getInstance("EUR")
		};
		currency.setItems(FXCollections.observableArrayList(currencies));
		currency.setValue(Currency.getInstance("UAH"));
	}
	
	private void populateLocales() {
		uaLanguage.setUserData(new Locale("ua"));
		ruLanguage.setUserData(new Locale("ru"));
		enLanguage.setUserData(new Locale("en"));
	}
	
	private void bindResourcesAndTexts() {
		configuration.textProperty().bind(
				resourceFactory.getStringBinding("configuration"));
		language.textProperty().bind(
				resourceFactory.getStringBinding("language"));
		phoneNumber.promptTextProperty().bind(
				resourceFactory.getStringBinding("phone"));
		testMode.textProperty().bind(
				resourceFactory.getStringBinding("test-mode"));
		email.promptTextProperty().bind(
				resourceFactory.getStringBinding("email"));
		description.promptTextProperty().bind(
				resourceFactory.getStringBinding("description"));
		price.promptTextProperty().bind(
				resourceFactory.getStringBinding("price"));
		composeInvoice.textProperty().bind(
				resourceFactory.getStringBinding("compose-invoice"));
		invoiceURL.promptTextProperty().bind(
				resourceFactory.getStringBinding("invoice-url"));
		copyInvoiceURL.textProperty().bind(
				resourceFactory.getStringBinding("copy-invoice-url"));
	}

	private void bindCopyAndInvoiceURL() {
		BooleanBinding copyBinding = new BooleanBinding() {
			{
				super.bind(invoiceURL.textProperty());
			}
			@Override
			protected boolean computeValue() {
				return invoiceURL.getText().isEmpty();
			}
		};
		
		copyInvoiceURL.disableProperty().bind(copyBinding);
	}

	private void bindComposeButtonWithEmailAndPrice() {
		BooleanBinding composeTextBinding = new BooleanBinding() {
			{
				super.bind(email.textProperty(), price.textProperty());
			}
			@Override
			protected boolean computeValue() {
				
				return email.getText().isEmpty() || price.getText().isEmpty();
			}
		};
		
		composeInvoice.disableProperty().bind(composeTextBinding);
	}

}
