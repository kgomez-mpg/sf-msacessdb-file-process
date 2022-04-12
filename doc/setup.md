#Setup

This page will walk through the initial steps of staging and defining the java UDTF. In my environment i am using a database 'stage_db' and 'public' schema. 

## Compiling and Packaging
The project is maven based, hence compiling and building the package is done using MVN. It is expected that you are knowledgeable on Java and its development lifecycle.

```sh
mvn clean compile package
```

Once completed, the jar (sf-msacessdb-file-process-0.1-jar-with-dependencies.jar) will be present in target directory. The target directory gets created as part of the maven execution.

## Staging the library
I am staging the library in an internal stage, though you can use the external stage too. I am able to stage using SnowSQL.

```sql
use role sysadmin;
use schema stage_db.public;
use warehouse lab_wh;

-- create the stage
create or replace stage data_lib_stage
    directory = ( enable = true )
    comment = 'used for staging data & libraries'
    ;

-- upload the jar file to the stage
put file://./target/sf-msacessdb-file-process-0.1-jar-with-dependencies.jar @data_lib_stage/jar_lib;

-- ensure to refresh the stage
alter stage data_lib_stage refresh;

-- run this to verify the jar shows up in the list
select *
from directory(@data_lib_stage);
```

Alter Refresh is a must before defining the UDTFs.

## Defining the UDTF
The following commands are used to define the UDTFs.

### List Table
The list table, is used to parse the access file and output the list of tables.

```sql
create or replace function msaccess_parser(msaccess_fl_url varchar ,msaccess_filename varchar)
    RETURNS TABLE ( 
        msaccess_file varchar, 
        msaccess_table varchar,  
        rs_json variant
    )
  language JAVA
  imports = ('@data_lib_stage/jar_lib/sf-msacessdb-file-process-0.1-jar-with-dependencies.jar')
  handler = 'com.venkat.blog.msaccess.ListTables'
  comment = 'used to get the list of tables present in the access file';
```

### Fetch Table
This method is used to return all the rows found the requested table.

```sql
create or replace function msaccess_fetch_table(msaccess_fl_url varchar ,msaccess_filename varchar ,tableto_parse varchar)
    RETURNS TABLE ( 
        msaccess_file varchar, 
        msaccess_table varchar,  
        rs_json variant
    )
  language JAVA
  imports = ('@data_lib_stage/jar_lib/sf-msacessdb-file-process-0.1-jar-with-dependencies.jar')
  handler = 'com.venkat.blog.msaccess.FetchTableRecords'
  comment = 'used to get all the rows from the request table, present in the access file';
```

## Staging data file
While this is not necessarily a setup, but I am covering here to demonstrate the steps i took to stage the MSAccess file. For my demo, i am using the dataset from [European Environment Agency: Large combustion plants industrial emission](https://www.eea.europa.eu/data-and-maps/data/lcp-8). 

### Zip the data file
NOTE: As of today, the UDTF can process only files with a max of size 16MB. I am aware that the product team will be overcoming this current limitation in the future. One way to overcome this is to zip the MSAccess file, for example, the above 32MB size is zipped to 7MB.

At run time, we can expand the file into the compute temporary directory and process the file. 

For the demo, I have the sample data at [src/test/data/sample_accessdb.zip](../src/test/data/sample_accessdb.zip).

### Upload to stage

```sql
use role sysadmin;
use schema stage_db.public;
use warehouse lab_wh;

-- upload the sample to stage
put file://./src/test/data/sample_accessdb.zip @data_lib_stage/data AUTO_COMPRESS = FALSE;

-- refresh should be done
alter stage data_lib_stage refresh;

-- verify the staged data file is present 
select *
from directory(@data_lib_stage);


```