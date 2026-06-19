package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

public record PaymentRetryResponse(
    Long paymentId,
    String clientSecret,
    String newTransactionId
) {
}
