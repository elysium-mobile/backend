package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

public record CreateOrderRequest(

        Long userAccountId,
        Integer amount,
        Long membershipId
) {
}
