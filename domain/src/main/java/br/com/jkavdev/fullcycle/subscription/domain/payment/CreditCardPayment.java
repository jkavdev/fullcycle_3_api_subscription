package br.com.jkavdev.fullcycle.subscription.domain.payment;

public record CreditCardPayment(
        Double amount,
        String orderId,
        String token,
        BillingAddress address
) implements Payment {

    public CreditCardPayment {
        this.assertArgumentNotNull(amount, "payment 'amount' should not be null");
        this.assertArgumentNotEmpty(orderId, "payment 'orderId' should not be empty");
        this.assertArgumentNotEmpty(token, "payment 'token' should not be empty");
        this.assertArgumentNotNull(address, "payment 'address' should not be null");
    }

}
