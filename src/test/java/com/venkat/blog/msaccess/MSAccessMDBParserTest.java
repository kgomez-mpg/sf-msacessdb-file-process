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
import java.util.stream.Stream;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import net.lingala.zip4j.ZipFile;


public class MSAccessMDBParserTest {
    static Logger logger = java.util.logging.Logger.getLogger(MSAccessMDBParserTest.class.getName());
    
    // // https://open.canada.ca/data/en/dataset/06022cc0-a31e-4b4c-850d-d4dccda5f3ac

    // @Test
    // void list_tables() throws Exception {
    //     String mdb_filepath = "./temp/Finance.accdb";

    //     ListTables tbls = new ListTables();
    //     tbls.process(
    //         new FileInputStream(new File(mdb_filepath))
    //         ,"test_fin.mdb");
            
    // }


    // @Test
    // void list_tables_3() throws Exception {
    //     String mdb_filepath = "/Users/vsekar/Downloads/LCP_database_v5.1_accdb.zip";
    //     String tmpdir = System.getProperty("java.io.tmpdir");

    //     Path path = Paths.get(mdb_filepath);
    //     Path fileName = path.getFileName();
  
    //     String unzip_folder = tmpdir + File.separatorChar + fileName.toString().replaceAll(".zip", "") + File.separatorChar;
    //     logger.info("unarchived to folder : " + unzip_folder);
    //     new ZipFile(mdb_filepath).extractAll(unzip_folder);

    //     String access_file_path = unzip_folder + File.separatorChar + "LCP_database_v5.1.accdb";
        
    //     String jdbc_url = String.format("jdbc:ucanaccess://%s;openExclusive=true", access_file_path); 
    //     logger.info("Connecting to " + jdbc_url + " ...");

    //     Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); /* often not required for Java 6 and later (JDBC 4.x) */
    //     Connection dbconn = DriverManager.getConnection(jdbc_url,null, null);

    //     // for example:
    //     // Connection conn=DriverManager.getConnection("jdbc:ucanaccess://c:/pippo.mdb;memory=true"); 

    //     logger.info("List of tables ...");
    //     try (ResultSet rsMD = dbconn.getMetaData().getTables(null, null, "%", null)) {
    //         while (rsMD.next()) {
    //             String tblName = rsMD.getString("TABLE_NAME");
    //             System.out.println(tblName);
    //         }
    //     }

    //     // java.io.InputStream warc_filestream = new java.io.FileInputStream(WARC_FILE);
    //     // Stream<WARCResponseRecord> parsed_record_stream = parser.process(warc_filestream, crawl_file, -1);
    //     logger.info("Finished!!");
    // }



    // @Test
    // void list_tables_2() throws Exception {
    //     String mdb_filepath = "./temp/Finance.accdb";
    //     mdb_filepath = "/var/folders/74/2yx7c4mj1t38gbbdj_rpmd6h0000gn/T//test_fin.mdb";

    //     String jdbc_url = String.format("jdbc:ucanaccess://%s;openExclusive=true", mdb_filepath); 
    //     logger.info("Connecting to " + jdbc_url + " ...");

    //     Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); /* often not required for Java 6 and later (JDBC 4.x) */
    //     Connection dbconn = DriverManager.getConnection(jdbc_url,null, null);

    //     // for example:
    //     // Connection conn=DriverManager.getConnection("jdbc:ucanaccess://c:/pippo.mdb;memory=true"); 

    //     logger.info("List of tables ...");
    //     try (ResultSet rsMD = dbconn.getMetaData().getTables(null, null, "%", null)) {
    //         while (rsMD.next()) {
    //             String tblName = rsMD.getString("TABLE_NAME");
    //             System.out.println(tblName);
    //         }
    //     }

    //     // java.io.InputStream warc_filestream = new java.io.FileInputStream(WARC_FILE);
    //     // Stream<WARCResponseRecord> parsed_record_stream = parser.process(warc_filestream, crawl_file, -1);
    //     logger.info("Finished!!");
    // }

    // @Test
    // void read_table() throws Exception {
    //     String mdb_filepath = "/Users/vsekar/Downloads/GI-Assets-DB-4/Assets4_Data.mdb";
    //     mdb_filepath = "/Users/vsekar/Downloads/NPRI-INRP_DatabaseBaseDeDonn‚es_1993-2020.accdb";
    //     mdb_filepath = "/Users/vsekar/Downloads/Finance.accdb";
    //     mdb_filepath = "/var/folders/74/2yx7c4mj1t38gbbdj_rpmd6h0000gn/T//test_fin.mdb";

    //     String jdbc_url = String.format("jdbc:ucanaccess://%s;openExclusive=true", mdb_filepath); 
    //     logger.info("Connecting to " + jdbc_url + " ...");

    //     Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); /* often not required for Java 6 and later (JDBC 4.x) */
    //     Connection dbconn = DriverManager.getConnection(jdbc_url,null, null);

    //     String table = "Companies";
    //     logger.info(String.format("Retreive all records from table %s ....", table));

    //     ResultSetToJsonMapper rsmapper = new ResultSetToJsonMapper();

    //     // Statement st = dbconn.createStatement();
    //     String sql_stmt = String.format("select count(*) from %s", table);
    //     sql_stmt = String.format("select * from %s", table);
    //     try (ResultSet rsMD = dbconn.createStatement().executeQuery(sql_stmt)) {
    //         while (rsMD.next()) {
    //             //String tblName = rsMD.toString();
    //             JSONArray jArray = rsmapper.mapResultSet(rsMD);
    //             System.out.println(jArray);
    //         }
    //     }
        
    //     logger.info("Finished!!");
    // }


}
