package com.venkat.blog.msaccess;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.json.JSONArray;

/**
 * Parses the MSAccess file and returns the records in the requested table.
 * 
 * @author : Venkatesh Sekar 
 */
public class FetchTableRecords 
    extends MSAccessFileParserBase {
    static Logger logger = java.util.logging.Logger.getLogger(FetchTableRecords.class.getName());

    /**
     * Snowflake would be invoking this method for each partition or a file. The path
     * to the file is the first parameter, Snowflake takes the effort of reading the file
     * from the stage and passing the same as an input stream.
     * 
     * @param p_filestream : The input filestream  
     * @param p_msaccess_filename : The access database file, which is part of the zip file upon which we need to operate.
     * @return 
     */
    
    public Stream<JDBCResultSetRecord> process(InputStream p_filestream ,String p_msaccess_filename ,String p_table) 
        throws Exception{
            String msaccess_filepath = extractZippedDBFileToLocal(p_filestream ,p_msaccess_filename);
            List<JDBCResultSetRecord> records = fetch_table(msaccess_filepath, p_table);
            return records.stream();
    }

    private List<JDBCResultSetRecord> fetch_table(String p_access_file, String p_table) 
        throws Exception {
        List<JDBCResultSetRecord> records = new ArrayList<>();
        logger.info(String.format("Retreive all records from table %s ....", p_table));
        String sql_stmt = String.format("select * from %s", p_table);
        RowSetFactory factory = RowSetProvider.newFactory();
        // CachedRowSet doesn't require active db connection unlike RowSet
        CachedRowSet cachedRowSet = factory.createCachedRowSet();
        try{
            //TODO: pass username/password to access the file
            Connection dbconn = getDBConnection(p_access_file,null,null);
            ResultSet rs = dbconn.createStatement().executeQuery(sql_stmt);
            cachedRowSet.populate(rs);
            dbconn.close();

            while(cachedRowSet.next()){
                JSONArray jArray = ResultSetToJsonMapper.mapResultSet(cachedRowSet);
                jArray.forEach((r) -> {
                    JDBCResultSetRecord rec =
                        new JDBCResultSetRecord(p_access_file ,p_table
                                ,r.toString());
                        records.add(rec);
                });
            }
        }
        catch(Exception e){
            logger.info(String.format("Exception thrown: %s", e.toString()));
        }
        
        return records;
    }

    
}
