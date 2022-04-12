package com.venkat.blog.msaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import net.lingala.zip4j.ZipFile;


public class FetchTableRecordsTest {
    static Logger logger = java.util.logging.Logger.getLogger(FetchTableRecordsTest.class.getName());

    //Source : https://www.eea.europa.eu/data-and-maps/data/lcp-8
    //Reported data on large combustion plants covered by the Industrial Emissions Directive (2010/75/EU) 
    static String ACCESS_DATA_FILE = "./src/test/data/sample_accessdb.zip";
    static String ACCESS_FILE = "accessdb/LCP_database_v5.1.accdb";

    @Test
    void fetch_table() throws Exception {
        FetchTableRecords tbls = new FetchTableRecords();
        Stream<JDBCResultSetRecord> rs_stream = tbls.process(
            new FileInputStream(new File(ACCESS_DATA_FILE))
            ,ACCESS_FILE
            ,"1_BasicData");
        
        assertNotNull(rs_stream);

        List<JDBCResultSetRecord> listof_tables = rs_stream.collect(Collectors.toList());
        listof_tables.forEach(r -> {
            logger.info(r.rs_json);
        });

        assertEquals(listof_tables.size(),389);
    }


}
