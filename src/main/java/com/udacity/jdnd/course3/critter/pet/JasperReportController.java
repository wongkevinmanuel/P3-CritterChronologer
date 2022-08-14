package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.utils.CustomJasperReport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class JasperReportController {

    public ResponseEntity<byte[] > respondReportPDF(CustomJasperReport jasperReport){
        if(jasperReport == null)
            throw new RuntimeException();
        //CustomJasperReport
        return null;
    }
}
