package es.upm.fi.dia.oeg.rdb;


import es.upm.fi.dia.oeg.rmlc.api.model.TriplesMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RDBConexion {

    private static final Logger _log = LoggerFactory.getLogger(RDBConexion.class);

    private ArrayList<String> foreignkeys;
    private PrintWriter pw;
    private PrintWriter pw2;

    public RDBConexion(String rdb){
        try{
            Class.forName ("org.h2.Driver");
            foreignkeys = new ArrayList<>();
            try {
               pw  = new PrintWriter("output/"+rdb+"-schema.sql", "UTF-8");
               pw2 = new PrintWriter("output/"+rdb+"-inserts.sql","UTF-8");
            }catch (Exception e){

            }
        }catch (Exception e){
            _log.error("The H2 driver has not found");
        }
    }

    public void close(){
        pw.close();pw2.close();
    }

    public void createDatabase(String rdb, String tables){

        try {
            long startTime = System.currentTimeMillis();
            createTables(tables,rdb);
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            _log.info("The "+rdb+" has been created in H2 successfully in: "+elapsedTime+"ms");
        }catch (Exception e ){
            _log.error("Error connecting with H2: "+e.getMessage());
        }
    }

    private void createTables(String tables, String rdb){
        try {
            Class.forName ("org.h2.Driver");
            Connection c = DriverManager.getConnection("jdbc:h2:./output/"+rdb+";AUTO_SERVER=TRUE", "sa", "");
            Statement s=c.createStatement();
            String[] st = tables.split("\n");
            for(String saux : st) {
                if(!saux.matches(".*FOREIGN.*")) {
                    s.execute(saux);
                    pw.println(saux);
                    //System.out.println(saux);
                }
                else{
                   String tableName = saux.split("TABLE")[1].split("\\(")[0];
                   String[] splitedst = saux.split("FOREIGN");
                   for(int i=1;i<splitedst.length;i++) {
                       if(splitedst[i].matches(".*,")){
                           foreignkeys.add("ALTER TABLE " + tableName + " ADD FOREIGN " + splitedst[i].replace(",", ";"));
                       }
                       else {
                           foreignkeys.add("ALTER TABLE " + tableName + " ADD FOREIGN " + splitedst[i].replace(");", ";"));
                       }
                   }
                   s.execute(splitedst[0].substring(0,splitedst[0].length()-1)+");");
                   pw.println(splitedst[0].substring(0,splitedst[0].length()-1)+");");
                   //System.out.println(splitedst[0].substring(0,splitedst[0].length()-1)+");");
                }
            }
            s.close();c.close();

        }catch (Exception e){
            _log.error("Error creating the tables in the rdb "+rdb+": "+e.getMessage());
        }
    }

    public void loadCSVinTable(TriplesMap tp, List<String[]> rows, String table, String rdb){
        try {

            Class.forName ("org.h2.Driver");
            Connection c = DriverManager.getConnection("jdbc:h2:./output/"+rdb+";AUTO_SERVER=TRUE", "sa", "");
            Statement s=c.createStatement();
            String inserts="",totalInserts="";
            for(int i=1; i<rows.size();i++){
                StringBuilder insert = new StringBuilder();
                insert.append("INSERT INTO "+table+" ");
                insert.append("VALUES(");
                for(int j=0;j<rows.get(i).length;j++){
                    if(RDBUtils.checkColumnInMapping(rows.get(0)[j],tp))
                        if(rows.get(i)[j].equals("")){
                            insert.append("'',");
                        }
                        else if(rows.get(i)[j].equals("NULL")){
                            insert.append("NULL,");
                        }
                        else {
                            insert.append("'" + rows.get(i)[j].replace("'","''") + "',");
                        }
                }
                String exec = insert.substring(0,insert.length()-1)+");\n";
                inserts+=exec;
                if(i%5000==0){
                    _log.info("Inserting 5000 instances in "+table+"...");
                    totalInserts += inserts;
                    long startTime = System.currentTimeMillis();
                    s.execute(inserts);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    _log.info("The instances have been indexed in H2 successfully in: "+elapsedTime+"ms");
                    inserts="";
                }
                //System.out.println(exec);
            }
            _log.info("Inserting last instances in "+table+"...");
            long startTime = System.currentTimeMillis();
            s.execute(inserts);
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            _log.info("The instances have been indexed in H2 successfully in: "+elapsedTime+"ms");
            totalInserts+=inserts;
             startTime = System.currentTimeMillis();
            pw2.println(totalInserts);
            stopTime = System.currentTimeMillis();
            elapsedTime = stopTime - startTime;
            _log.info("The instances have been printed at output file in: "+elapsedTime+"ms");
            s.close();c.close();

        }catch (Exception e){
            _log.error("Error creating the tables in the rdb "+rdb+": "+e.getMessage());
        }
    }

    public void addForeignKeys(String rdb){
        try {
            Class.forName("org.h2.Driver");
            Connection c = DriverManager.getConnection("jdbc:h2:./output" + rdb+";AUTO_SERVER=TRUE", "sa", "");
            Statement s = c.createStatement();
            for(String f: foreignkeys) {
                s.execute(f);
                pw.println(f);
                //System.out.println(st);
            }
        }catch (Exception e){
            _log.error("Error creating the tables in the rdb "+rdb+": "+e.getMessage());
        }
    }


    public void updateDataWithFunctions (HashMap<String,HashMap<String,String>> functions, String rdb, boolean index){
        long startTime = System.currentTimeMillis();
        try {
            Connection c = DriverManager.getConnection("jdbc:h2:./output"+rdb+";AUTO_SERVER=TRUE", "sa", "");
            Statement s = c.createStatement();
            for(Map.Entry<String,HashMap<String,String>> entry : functions.entrySet()){
                String table_name = entry.getKey();
                HashMap<String,String> column_function = entry.getValue();
                for(Map.Entry<String,String> function_entry : column_function.entrySet()){
                    String alter_column = function_entry.getKey();
                    String function_exp = function_entry.getValue().replace("{","").replace("}","");
                    //if(function_exp.matches(".*\\(.*")) {
                        s.execute("ALTER TABLE " + table_name + " ADD " + alter_column + ";");
                        pw.println("ALTER TABLE " + table_name + " ADD " + alter_column + ";");
                        //System.out.println("ALTER TABLE " + table_name + " ADD " + alter_column + ";");
                        s.execute("UPDATE " + table_name + " SET " + alter_column.split(" ")[0] + "=" + function_exp + ";");
                        pw2.println("UPDATE "  + table_name + " SET " + alter_column.split(" ")[0] + "=" + function_exp + ";");
                        //System.out.println("UPDATE " + table_name + " SET " + alter_column.split(" ")[0] + "=" + function_exp + ";");
                    //}
                    if(index) {
                        s.execute("CREATE INDEX "+alter_column.split(" ")[0]+"s ON "+table_name+" ("+alter_column.split(" ")[0]+")");
                        pw.println("CREATE INDEX "+alter_column.split(" ")[0]+"s ON "+table_name+" ("+alter_column.split(" ")[0]+");");
                        //System.out.println("CREATE INDEX "+alter_column.split(" ")[0]+"s ON "+table_name+" ("+alter_column.split(" ")[0]+")");
                    }

                }
            }
            s.close();c.close();

        }catch (Exception e){
            _log.error("Error in update the table: "+e.getMessage());
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        _log.info("The "+rdb+" has been updated in H2 successfully in: "+elapsedTime+"ms");
    }
}
