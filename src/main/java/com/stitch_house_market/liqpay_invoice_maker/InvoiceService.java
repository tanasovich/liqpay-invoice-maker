package com.stitch_house_market.liqpay_invoice_maker;

import java.util.HashMap;
import java.util.Map;

import com.liqpay.LiqPay;

public class InvoiceService {
	private String publicKey;
	private String privateKey;
	
	public InvoiceService(String publicKey, String privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public Map<String, Object> createInvoiceFrom(Invoice invoice) throws Exception {
		return sendInvoiceRequest(invoice);
	}
	
	private Map<String, Object> sendInvoiceRequest(Invoice invoice) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("action", "invoice_send");
		params.put("version", "3");
		params.put("email", invoice.getEmail());
		params.put("phone", invoice.getPhoneNumber());
		params.put("description", invoice.getDescription());
		params.put("amount", Double.toString(invoice.getPrice()));
		params.put("currency", invoice.getCurrency().toString());
		
		LiqPay liqpay = new LiqPay(publicKey, privateKey);
		
		if (invoice.isTestMode()) {
			liqpay.setCnbSandbox(true);
		}
		
		Map<String, Object> result = liqpay.api("request", params);
		
		return result;
	}
}
