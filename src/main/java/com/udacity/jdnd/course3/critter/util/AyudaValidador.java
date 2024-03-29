package com.udacity.jdnd.course3.critter.util;

import java.util.Objects;

public class AyudaValidador {

    public static boolean errorVarNulloLong(long Id){
        try{
            if(Objects.isNull(Id))
                return true;

            Long.valueOf(Id);
            return false;
        }catch (Exception exception){
            return true;
        }
    }

    public static boolean errorVarNulloInt(String Id){
        try{
            if(Objects.isNull(Id))
                return true;

            int valor = Integer.parseInt(Id);
            return false;

        }catch (NumberFormatException exception){
            return true;
        }
    }

}
