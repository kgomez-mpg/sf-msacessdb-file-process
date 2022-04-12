package com.venkat.blog.msaccess;

/**
 * A java bean representing resultset of msaccess query.
 */
public class JDBCResultSetRecord {
    
    /**
     * The access file from which this record was parsed.
     */
    public String msaccess_file = null;
  
    /**
     * MSAccess Table
     */
    public String msaccess_table = null;

    /**
     * Resultset as json
     */
    public String rs_json = null;

    public JDBCResultSetRecord(String msaccess_file, String msaccess_table, String rs_json) {
        this.msaccess_file = msaccess_file;
        this.msaccess_table = msaccess_table;
        this.rs_json = rs_json;
    }

    @Override
    public String toString() {
        return "JDBCResultSetRecord [msaccess_file=" + msaccess_file + ", msaccess_table=" + msaccess_table
                + ", rs_json=" + rs_json + "]";
    }

  }
