package com.udacity.jdnd.course3.critter;

import java.lang.reflect.Field;

//Ayuda a inyectar objetos
public class TestUtils {
    public static void InyectarObjeto(Object blancoObjetivo, String nombreCampo
            , Object aInyectar) {
        boolean privado = false;
        Field field = null;

        try {
            field = blancoObjetivo.getClass().getDeclaredField(nombreCampo);
            if (!field.isAccessible()) {
                field.setAccessible(true);
                privado = true;
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            //Establece un objeto con el objeto q quiero inyectar
            field.set(blancoObjetivo, aInyectar);
            if(privado)
                field.setAccessible(false);

        } catch (IllegalAccessException e) {        }
    }
}
