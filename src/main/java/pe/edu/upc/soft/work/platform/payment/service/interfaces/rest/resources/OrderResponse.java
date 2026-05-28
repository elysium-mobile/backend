package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

public record OrderResponse(

        Long orderId,
        Long userAccountId,
        Integer amount,
        Long membershipId
) {
}
