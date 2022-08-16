package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.utils.CustomJasperReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;

import java.util.HashMap;
import java.util.Map;

@Controller
public class JasperReportController {

    public byte[] responseReportPDF(CustomJasperReport jasperReport){
        if(jasperReport == null)
            throw new RuntimeException();


        //CustomJasperReport
        //SOLO RECIBE TODOS LOS DATOS PARA REALIZAR EL REPORTE
        //ENCARGADO DE CONFIGURAR LOS DATOS FINALES COMO EL NOMBRE, LA RUTA, Y DE MAS DEL REPORT JASPER
        //POR OTRO LADO TAMBIEN SE ENCARGA DE PROCEDER A LA IMPRIMIR EN PDF

        //Parámetros dinámicos necesarios para el informe
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CompanyName", "Tech-Wong");
        params.put("employeeData", new JRBeanCollectionDataSource(jasperReport.getReportData() ));

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        JasperCompileManager.compileReport(
                                ResourceUtils.getFile(jasperReport.getResourceLocation()).getAbsolutePath())
                                ,params
                                ,new JREmptyDataSource()
                        );
        try {
            return JasperExportManager.exportReportToPdf(null);
        }catch (JRException ex){
            return null;
        }

    }
}
