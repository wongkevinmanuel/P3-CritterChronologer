package com.udacity.jdnd.course3.critter.user;

/**
 * Una lista de ejemplo de habilidades de los empleados que podrían incluirse en un empleado o en una solicitud de horario.
 */
public enum EmployeeSkill {
    PETTING(0),
    WALKING(1),
    FEEDING(2),
    MEDICATING(3),
    SHAVING(4);

    private Integer codigo;

    EmployeeSkill(Integer codigo) {
        this.codigo = codigo;
    }
}
