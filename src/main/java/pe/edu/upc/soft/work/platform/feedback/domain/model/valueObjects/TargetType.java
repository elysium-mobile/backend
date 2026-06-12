package pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects;

import java.util.Arrays;

/**
 * Enumeration representing the type of target for feedback
 */
public enum TargetType {
    AREA_COMPANY(0),
    UNIT_OF_WORK(1),
    TEAM_OF_WORK(2);

    private final int value;

    TargetType(int value){this.value=value;}

    public int getValue(){return value;}

    public static TargetType fromValue(int value){
        return Arrays.stream(TargetType.values())
                .filter(tt -> tt.value == value)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("[TargetType] Invalid value for TargetType: " + value));
    }
}
