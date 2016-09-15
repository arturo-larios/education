package com.nvisium.moneyx.service;

import java.math.BigDecimal;
import java.util.List;

import com.nvisium.moneyx.model.Event;
import com.nvisium.moneyx.model.Payment;
import com.nvisium.moneyx.model.User;


public interface PaymentService {

	public boolean makePayment(Event event, BigDecimal amount);

	public List<Payment> getSentPayments(User user);

	public List<Payment> getReceivedPayments(User user);
	
	public BigDecimal getTotalPayments(Event event);
}
