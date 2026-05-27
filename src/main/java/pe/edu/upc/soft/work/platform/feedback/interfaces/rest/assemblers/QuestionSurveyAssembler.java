package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.QuestionType;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateQuestionSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.QuestionSurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateQuestionSurveyRequest;

public class QuestionSurveyAssembler {

    /**
     * Converts a CreateQuestionSurveyRequest to a CreateQuestionSurveyCommand.
     */
    public static CreateQuestionSurveyCommand toCommandFromRequest(CreateQuestionSurveyRequest request){
        return new CreateQuestionSurveyCommand(request.textQuestion(), QuestionType.valueOf( request.questionType()));
    }

    /**
     *  Converts an UpdateQuestionSurveyRequest to an UpdateQuestionSurveyCommand.
     */
    public static UpdateQuestionSurveyCommand toCommandFromRequest(Long questionSurveyId, UpdateQuestionSurveyRequest request){
        return new UpdateQuestionSurveyCommand(questionSurveyId, request.textQuestion(), QuestionType.valueOf(request.questionType()));
    }

    /**
     *  Converts a QuestionSurvey entity to a QuestionSurveyResponse.
     */
    public static QuestionSurveyResponse toResponseFromEntity(QuestionSurvey questionSurvey){
        return new QuestionSurveyResponse(questionSurvey.getId(), questionSurvey.getTextQuestion(), questionSurvey.getQuestionType().name());
    }
}
