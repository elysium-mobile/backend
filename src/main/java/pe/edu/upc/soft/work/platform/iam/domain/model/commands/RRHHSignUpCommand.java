package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

public record RRHHSignUpCommand(String name,
                                String lastname,
                                String phoneNumber,
                                String dni,
                                String email,
                                String password,
                                String RRHHDepartment,
                                String statusHierarchy) {
}
