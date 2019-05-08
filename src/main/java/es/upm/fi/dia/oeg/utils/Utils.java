package es.upm.fi.dia.oeg.utils;


import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;

public class Utils {

    private static final Logger _log = LoggerFactory.getLogger(Utils.class);


    public static JSONArray readConfiguration (String config){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = new JSONObject(IOUtils.toString(new FileReader(config)));
        }catch (Exception e){
            _log.error("Exception reading the configuration file: "+e.getMessage());
        }

        return jsonObject.getJSONArray("sources");
    }



}
