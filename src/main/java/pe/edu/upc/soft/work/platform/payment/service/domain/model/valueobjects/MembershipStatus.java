package pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects;

/**
 * Enumeration representing the status of Membership in a membership class
 */
public enum MembershipStatus {

    ACTIVE(1),
    PENDING(2),
    ARCHIVED(3),
    FAILED(4);

    private final int value;

    MembershipStatus(int value) { this.value=value;}

    public int getValue(){return value;}

    public static MembershipStatus fromValue(int value){
        for(MembershipStatus status : MembershipStatus.values()){
            if(status.value == value){
                return status;
            }
        }
        throw new IllegalArgumentException("[MembershipStatus] Invalid value for MembershipStatus: " + value);
    }
}
