package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

public record MembershipPlanResponse(
        Long planId,
        String planName,
        Integer price
) {
}
