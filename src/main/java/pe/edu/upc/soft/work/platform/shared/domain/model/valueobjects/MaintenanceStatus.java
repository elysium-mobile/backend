package pe.edu.upc.soft.work.platform.shared.domain.model.valueobjects;

import java.util.Arrays;

/**
 * Enumeration representing the various statuses of vehicle maintenance.
 */
public enum MaintenanceStatus {
    NOT_BEING_SERVICED(0, null),
    WAITING(1, "Your vehicle is waiting for maintenance."),
    IN_DIAGNOSIS(2, "Your vehicle is currently being diagnosed."),
    IN_REPAIR(3, "Your vehicle is currently under repair."),
    TESTING(4, "Your vehicle is being tested after repairs."),
    READY_FOR_PICKUP(5, "Your vehicle is ready for pickup."),
    COLLECTED(6, "You have collected your vehicle. Thank you!");

    private final int value;
    private final String notificationMessage;

    /**
     * Constructor for MaintenanceStatus enum.
     *
     * @param value the integer value associated with the MaintenanceStatus
     * @param notificationMessage the notification message associated with the MaintenanceStatus
     */
    MaintenanceStatus(int value, String notificationMessage) {
        this.value = value;
        this.notificationMessage = notificationMessage;
    }

    /**
     * Gets the integer value associated with the MaintenanceStatus.
     *
     * @return the integer value of the MaintenanceStatus
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the notification message associated with the MaintenanceStatus.
     *
     * @return the notification message
     */
    public String getNotificationMessage() {
        return notificationMessage;
    }

    /**
     * Converts an integer value to its corresponding MaintenanceStatus enum.
     *
     * @param value the integer value representing a MaintenanceStatus
     * @return the corresponding MaintenanceStatus enum
     */
    public static MaintenanceStatus fromValue(int value) {
        return Arrays.stream(MaintenanceStatus.values())
                .filter(mt -> mt.value == value)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("[MaintenanceStatus] Invalid value for MaintenanceStatus: "
                                + value));
    }
}
