package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.AnswerResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateAnswerRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateAnswerRequest;

public class AnswerAssembler {


    /**
     * Converts a CreateAnswerRequest to a CreateAnswerCommand.
     */
    public static CreateAnswerCommand toCommandFromRequest(CreateAnswerRequest request) {
        return new CreateAnswerCommand(request.value(), request.scoreAnswer());
    }

    /**
        * Converts an UpdateAnswerRequest to an UpdateAnswerCommand.
     */
    public static UpdateAnswerCommand toCommandFromRequest(Long answerId, UpdateAnswerRequest request)
    {
        return new UpdateAnswerCommand(answerId, request.value(), request.scoreAnswer());
    }

    /**
     * Converts an Answer entity to an AnswerResponse.
     */
    public static AnswerResponse toResponseFromEntity(Answer answer) {
        return new AnswerResponse(answer.getId(), answer.getValue(), answer.getScoreAnswer());
    }
}
