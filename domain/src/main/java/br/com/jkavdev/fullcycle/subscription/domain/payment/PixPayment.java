package br.com.jkavdev.fullcycle.subscription.domain.payment;

public record PixPayment(
        Double amount,
        String orderId,
        BillingAddress address
) implements Payment {

    public PixPayment {
        this.assertArgumentNotNull(amount, "payment 'amount' should not be null");
        this.assertArgumentNotEmpty(orderId, "payment 'orderId' should not be empty");
        this.assertArgumentNotNull(address, "payment 'address' should not be null");
    }

}
