package br.com.jkavdev.fullcycle.subscription.domain.payment;

public interface PaymentGateway {

    Transaction processPayment(Payment payment);

}
