package br.com.jkavdev.fullcycle.subscription.domain.payment;

public record Transaction(String transactionId, String errorMessage) {

    public boolean isSuccess() {
        return errorMessage != null;
    }

    public static Transaction success(final String transactionId) {
        return new Transaction(transactionId, null);
    }

    public static Transaction failure(final String transactionId, final String errorMessage) {
        return new Transaction(transactionId, errorMessage);
    }

}
