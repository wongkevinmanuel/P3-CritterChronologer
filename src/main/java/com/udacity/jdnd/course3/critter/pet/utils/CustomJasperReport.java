package com.udacity.jdnd.course3.critter.pet.utils;

import net.sf.jasperreports.engine.JRDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomJasperReport implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomJasperReport.class);

    private static final long serialVersionUID = -1L;

    //Informacion requerida
    String reportName;
    String reportDir;

    //Parametros reporte
    Map<String, Object> parameters = new HashMap<String, Object>();

    //Formato reporte
    //JasperReportExportFormat reportExportFormat = JasperReportExportFormat.PDF;

    //Data Source
    Collection<?> reportData;
    //
    //JRDataSource dataSource;

    //Configuracion adicional
    Boolean useDefaultConfiguration;
    Map<String, Object> reportConfiguration = new HashMap<String, Object>();

    //Generate byte array
    //byte[] content;

    //output filename
    String outPutFilename;

    //resourceLocation
    String resourceLocation;

    public static long getSerialVersionUID(){
        return serialVersionUID;
    }

    public CustomJasperReport() {
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String getOutPutFilename() {
        return outPutFilename;
    }

    public void setOutPutFilename(String outPutFilename) {
        this.outPutFilename = outPutFilename;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDir() {
        return reportDir;
    }

    public void setReportDir(String reportDir) {
        this.reportDir = reportDir;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Collection<?> getReportData() {
        return reportData;
    }

    public void setReportData(Collection<?> reportData) {
        this.reportData = reportData;
    }

    public Boolean getUseDefaultConfiguration() {
        return useDefaultConfiguration;
    }

    public void setUseDefaultConfiguration(Boolean useDefaultConfiguration) {
        this.useDefaultConfiguration = useDefaultConfiguration;
    }

    public Map<String, Object> getReportConfiguration() {
        return reportConfiguration;
    }

    public void setReportConfiguration(Map<String, Object> reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }
}

