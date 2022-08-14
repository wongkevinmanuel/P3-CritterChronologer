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
    //String outPutFilename;

}

