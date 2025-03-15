package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.client;

import br.com.jkavdev.fullcycle.subscription.domain.payment.Payment;
import br.com.jkavdev.fullcycle.subscription.domain.payment.PaymentGateway;
import br.com.jkavdev.fullcycle.subscription.domain.payment.Transaction;
import br.com.jkavdev.fullcycle.subscription.domain.utils.IdUtils;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class PagarmeClient implements PaymentGateway {
    @Override
    public Transaction processPayment(Payment payment) {
        if (LocalTime.now().getMinute() % 2 == 0) {
            return Transaction.success(IdUtils.uniqueId());
        } else {
            return Transaction.failure(IdUtils.uniqueId(), "not enough funds");
        }
    }
}
