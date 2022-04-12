package com.venkat.blog.msaccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import net.lingala.zip4j.ZipFile;

/**
 * Base class for parsing MSAccess files
 * 
 * @author : Venkatesh Sekar 
 */
public abstract class MSAccessFileParserBase {
    static Logger logger = java.util.logging.Logger.getLogger(MSAccessFileParserBase.class.getName());

    /**
     * The output class that will present in the stream.
     * @return
     */
    public static Class getOutputClass() {
        return JDBCResultSetRecord.class;
    }

    protected Connection getDBConnection(String p_access_file ,String p_user ,String p_passwrd)
        throws Exception {
            String jdbc_url = String.format("jdbc:ucanaccess://%s;openExclusive=true", p_access_file); 
            logger.info("Connecting to " + jdbc_url + " ...");

            return DriverManager.getConnection(jdbc_url,p_user, p_passwrd);
        }


    /**
     * Stores the file input stream, into a temporary local file.
     * 
     * @param p_ifs : The input filestream
     * @param p_target_path : The target path, under which the file will be stored. ex: /tmp/accessdb.zip
     * 
     * @throws IOException
     */
    protected File storeInputStreamToLocalFile(InputStream p_ifs, String p_target_path)
        throws IOException {

        try (OutputStream output = new FileOutputStream(p_target_path, false)) {
            p_ifs.transferTo(output);
        }

        return new File(p_target_path);
    }

    /**
     * The MS-Access file, which should have been zipped and stored in the stage, is downloaded
     * and extracted locally. It is assumed that there could be multiple MS-Access file in a single zip file,
     * hence we need information on which specific MS-Access file is the request is being made for
     * 
     * 
     * @param p_ifs : The input filestream  
     * @param p_accessdb_filename : The access database file, which is part of the zip file upon which we need to operate.
     * 
     * @throws Exception
     */
    protected String extractZippedDBFileToLocal(InputStream p_ifs, String p_accessdb_filename)
        throws Exception {
            //The file will be extracted at runtime, into a temp directory ex: /tmp
            String tmpdir = System.getProperty("java.io.tmpdir");

            //InputStream - stored -> ex: /tmp/msaccessdb.zip
            String local_archive_filepath = tmpdir + File.separatorChar + "msaccessdb.zip";
            storeInputStreamToLocalFile(p_ifs ,local_archive_filepath);
            if(new File(local_archive_filepath).exists() == false) { 
                throw new FileNotFoundException(
                    String.format("The filestream was not stored locally."));
            }
            logger.info("Archived file stored locally at " + local_archive_filepath);

            //extract the zip
            // /tmp/msaccessdb.zip - extracted to -> /tmp/msaccessdb
            String local_extract_folderpath = tmpdir + File.separatorChar + "msaccessdb";
            new ZipFile(local_archive_filepath).extractAll(local_extract_folderpath);
            if(new File(local_extract_folderpath).exists() == false) { 
                throw new FileNotFoundException(
                    String.format("The local extract folder was not created."));
            }
            logger.info("Archived file extracted locally at " + local_extract_folderpath);

            // file to process: /tmp/msaccessdb/northwind.accdb
            String accessdb_filepath = local_extract_folderpath + File.separatorChar + p_accessdb_filename;
            if(new File(accessdb_filepath).exists() == false) { 
                throw new FileNotFoundException(
                    String.format("MSAccess file: %s is not present in the archive", p_accessdb_filename));
            }

            return accessdb_filepath;
        }
    
}