package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

public record RefundResponse(
    String refundId,
    String paymentIntentId,
    Integer refundedAmountCents,
    String status
) {
}
