package com.venkat.blog.msaccess;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.json.JSONArray;

/**
 * Parses the MSAccess file and returns the list of tables found in the database.
 * @author : Venkatesh Sekar 
 */
public class ListTables 
    extends MSAccessFileParserBase {
    static Logger logger = java.util.logging.Logger.getLogger(ListTables.class.getName());

    /**
     * Snowflake would be invoking this method for each partition or a file. The path
     * to the file is the first parameter, Snowflake takes the effort of reading the file
     * from the stage and passing the same as an input stream.
     * 
     * @param p_filestream : The input filestream  
     * @param p_msaccess_filename : The access database file, which is part of the zip file upon which we need to operate.
     * @return 
     */
    public Stream<JDBCResultSetRecord> process(InputStream p_filestream ,String p_msaccess_filename) 
        throws Exception{
        String msaccess_filepath = extractZippedDBFileToLocal(p_filestream ,p_msaccess_filename);
        List<JDBCResultSetRecord> records = list_tables(msaccess_filepath);
        return records.stream();
    }


    private List<JDBCResultSetRecord> list_tables(String p_access_file) 
        throws Exception {
        List<JDBCResultSetRecord> records = new ArrayList<>();
        logger.info("Fetching list of tables ...");
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet cachedRowSet = factory.createCachedRowSet();
        try{
            //TODO : pass username/password to access the file
            Connection dbconn = getDBConnection(p_access_file,null, null);
            logger.info("Fetching list of tables ...");
            ResultSet rs = dbconn.getMetaData().getTables(null,null,"%",null);
            cachedRowSet.populate(rs);
            dbconn.close();
            rs.close();

            while(cachedRowSet.next()){
                JSONArray jArray = ResultSetToJsonMapper.mapResultSet(cachedRowSet);

                jArray.forEach((r) -> {
                    JDBCResultSetRecord rec = 
                        new JDBCResultSetRecord(p_access_file,"*",
                            r.toString());
                    records.add(rec);
                });
            }
        }
        catch(Exception e){
            logger.info(String.format("Exception thrown: %s", e.toString()));
        }
        finally {
            cachedRowSet.release();
            cachedRowSet.close();
        }
        return records;
    }
    
}
