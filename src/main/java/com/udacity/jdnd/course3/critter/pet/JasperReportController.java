package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.utils.CustomJasperReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.jni.File;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class JasperReportController {

    private CustomJasperReport jasperReport;

    public JasperReportController(){

    }
    public JasperReportController(CustomJasperReport jasperReport) {
        this.jasperReport = jasperReport;
    }

    private Map<String, Object> templateParameters(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CompanyName", "Tech-Wong");
        params.put("employeeData", new JRBeanCollectionDataSource(jasperReport.getReportData() ));
        return params;
    }
    private JasperPrint generateJasperPrint(){

        try{
            return JasperFillManager.fillReport(
                            JasperCompileManager.compileReport(
                                    ResourceUtils.getFile(jasperReport.getResourceLocation()).getAbsolutePath())
                            ,templateParameters()
                            ,new JREmptyDataSource()
                    );
        }catch (FileNotFoundException | JRException exception){
            return null;
        }
    }
    public byte[] responseReportPDF(){
        if(jasperReport == null)
            throw new RuntimeException();

        JasperPrint jasperPrint = generateJasperPrint();

        try {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }catch (JRException ex){
            return null;
        }
    }

    public CustomJasperReport getJasperReport() {
        return jasperReport;
    }

    public void setJasperReport(CustomJasperReport jasperReport) {
        this.jasperReport = jasperReport;
    }
}
