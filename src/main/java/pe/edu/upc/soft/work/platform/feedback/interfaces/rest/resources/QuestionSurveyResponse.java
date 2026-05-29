package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

public record QuestionSurveyResponse(

        Long questionSurveyId,
        String textQuestion,
        String questionType
) {
}
