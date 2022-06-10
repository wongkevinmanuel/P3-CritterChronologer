package com.udacity.jdnd.course3.critter.user;

public enum EmployeeSkills {
    WALKING(0),
    FEEDING(1),
    PETTING(2),
    MEDICATING(3),
    SHAVING(4);

    private Integer codigo;

    EmployeeSkills(Integer _codigo){
        this.codigo = _codigo;
    }
}
