package com.udacity.jdnd.course3.critter.user.dto;

import com.udacity.jdnd.course3.critter.user.domain.EmployeeSkill;

import java.time.LocalDate;
import java.util.Set;

/**
 * Representa una solicitud para encontrar empleados disponibles por habilidades.
 * No se asigna directamente a la base de datos.
 */
public class EmployeeRequestDTO {
    private Set<EmployeeSkill> skills;
    private LocalDate date;

    public Set<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
