package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.client;

import br.com.jkavdev.fullcycle.subscription.domain.payment.Payment;
import br.com.jkavdev.fullcycle.subscription.domain.payment.PaymentGateway;
import br.com.jkavdev.fullcycle.subscription.domain.payment.Transaction;
import org.springframework.stereotype.Component;

@Component
public class PagarmeClient implements PaymentGateway {
    @Override
    public Transaction processPayment(Payment payment) {
        return null;
    }
}
