package com.nvisium.moneyx.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nvisium.moneyx.model.Event;
import com.nvisium.moneyx.model.Payment;
import com.nvisium.moneyx.model.User;


@Repository
@Qualifier(value = "paymentRepository")
public interface PaymentRepository extends CrudRepository<Payment, Long> {

	@Query("select p from Payment p where p.sender = ?1")
	public List<Payment> findPaymentsBySender(User sender);

	@Query("select p from Payment p where p.receiver = ?1")
	public List<Payment> findPaymentsByReceiver(User receiver);
	
	@Query("select p from Payment p where p.event = ?1")
	public List<Payment> findPaymentsByEvent(Event event);
}
