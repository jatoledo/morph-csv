package es.upm.fi.dia.oeg.utils;

import es.upm.fi.dia.oeg.model.RDB;
import es.upm.fi.dia.oeg.morph.base.engine.MorphBaseRunner;
import es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunnerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RunQuery {


    static Logger log = LoggerFactory.getLogger(RunQuery.class);
    //run morph
    public static void runBatchMorph(RDB rdb){
        String configurationFile = "output/"+rdb.getName()+".r2rml.properties";
        setProperties(rdb,null);
        try {
          //  MorphRDBRunnerFactory runnerFactory = new MorphRDBRunnerFactory();
          //  MorphBaseRunner runner = runnerFactory.createRunner(".",configurationFile);
            //  runner.run();
            //log.info("Materialization made correctly");
        } catch(Exception e) {
            e.printStackTrace();
            log.info("Error occured: " + e.getMessage());
        }

    }

    public static void runQ(){
        ProcessBuilder pb = new ProcessBuilder("./output/runMorph.sh");
        try {
            Process p = pb.start();
            BufferedReader read = new BufferedReader(new InputStreamReader(p.getInputStream()));
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            while (read.ready()) {
                System.out.println(read.readLine());
            }
        }catch (IOException e){
            log.error("error executing morph-rdb: "+e.getMessage());
        }
    }

    public static void runQueryMorph(RDB rdb, String query){
        String configurationFile = "output/"+rdb.getName()+".r2rml.properties";
        setProperties(rdb,query);
        try {
            MorphRDBRunnerFactory runnerFactory = new MorphRDBRunnerFactory();
            MorphBaseRunner runner = runnerFactory.createRunner(".",configurationFile);
            runner.run();
            log.info("Evaluation query correctly");
        } catch(Exception e) {
            e.printStackTrace();
            log.info("Error occured: " + e.getMessage());
        }
    }

    private static void setProperties(RDB rdb, String query){
        try {
            PrintWriter writer = new PrintWriter("output/"+rdb.getName() + ".r2rml.properties", "UTF-8");
            writer.println("mappingdocument.file.path="+ rdb.getName() + ".r2rml.ttl");
            if(query!=null)
                writer.println("query.file.path=" + query);
            writer.println("output.file.path=" + rdb.getName() + "-query-result.xml");

            writer.println("database.name[0]=" + rdb.getName());
            writer.println("no_of_database=1");
            writer.println("database.driver[0]=org.h2.Driver");
            writer.println("database.url[0]=jdbc:h2:./" + rdb.getName());
            writer.println("database.user[0]=sa");
            writer.println("database.pwd[0]=");
            writer.println("database.type[0]=h2");

            writer.close();
        }catch (Exception e ){
            System.out.println("Error");
        }
    }

    //run ontop


}
