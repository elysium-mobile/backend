package pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects;

import java.util.Arrays;

/**
 * Enumeration representing the type of question in a feedback survey
 */
public enum QuestionType {

    OPEN_SURVEY(1),
    MULTIPLE_CHOICE(2),
    RATING(3);

    private final int value;

    QuestionType(int value){this.value=value;}

    public int getValue() {return value;}

    public static QuestionType formValue(int value){
        return Arrays.stream(QuestionType.values())
                .filter(qt -> qt.value == value)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("[QuestionType] Invalid value for QuestionType: " + value));
    }
}
