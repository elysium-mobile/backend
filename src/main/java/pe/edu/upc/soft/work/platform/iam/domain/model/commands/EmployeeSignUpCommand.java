package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import java.util.Date;

public record EmployeeSignUpCommand(String name,
                                    String lastname,
                                    String phoneNumber,
                                    String dni,
                                    String email,
                                    String password,
                                    String anonymousName,
                                    Date dateStart,
                                    String position,
                                    Integer salary) {
}
