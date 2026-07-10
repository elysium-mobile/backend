package pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects;

import java.util.Arrays;

/**
 * Enumeration representing the type of notification in the system
 */
public enum NotificationType {
    FORUM(0), MESSAGE(1), PAYMENT(2),SURVEY(3);

    private final int value;

    NotificationType(int value) {this.value=value;}

    public int getValue() {return value;}

    public static NotificationType fromValue(int value) {
         return Arrays.stream(NotificationType.values())
                .filter(nt -> nt.value == value)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("[NotificationType] Invalid value for NotificationType: " + value));
    }

}
