package com.org.somak.datastore.configuration.engine;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.org.somak.datastore.util.Utility;
import com.org.somak.datastore.exception.ConfigurationException;
import com.org.somak.datastore.exception.ExceptionCode;

import java.util.Map;

/**
 *  Reads configuration metadata to support the data engine. Default property files are read from file
 *  engine_meta_data.txt<br>
 *  Provides capability to override the values via system environment<br>
 */
public final class EngineMetadata {

    private static final String FILE_NAME = "engine_meta_data.properties";

    @VisibleForTesting
    private static final Map<String, String> metaDataMap = readMetaData();

    private static Map<String, String> readMetaData(){

        ImmutableMap.Builder<String,String> builder = Utility.readFromFile(EngineMetadata.class.getResource(FILE_NAME));
        System.out.println(builder.build());
        return builder.build();
    }

    public static String getEngineMetaData(String key){
        if(!EngineMetadata.metaDataMap.containsKey(key.trim().toLowerCase()))
            throw new ConfigurationException(ExceptionCode.CONFIGURATION_EXCEPTION,
                    String.format("Unable to get value from metadata file '%s' for keyword '%s'", FILE_NAME, key));
        return EngineMetadata.metaDataMap.get(key.trim().toLowerCase());
    }

}
