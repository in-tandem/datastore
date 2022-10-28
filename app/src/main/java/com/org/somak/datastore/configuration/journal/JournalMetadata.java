package com.org.somak.datastore.configuration.journal;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.org.somak.datastore.util.Utility;
import com.org.somak.datastore.exception.ConfigurationException;
import com.org.somak.datastore.exception.ExceptionCode;

import java.util.Map;

public final class JournalMetadata {

    private static final String FILE_NAME = "journal_meta_data.properties";

    @VisibleForTesting
    private static final Map<String, String> metaDataMap = readMetaData();

    private static Map<String, String> readMetaData(){

        ImmutableMap.Builder<String,String> builder = Utility.readFromFile(JournalMetadata.class.getResource(FILE_NAME));
        System.out.println(builder.build());
        return builder.build();
    }

    public static String getEngineMetaData(String key){
        if(!JournalMetadata.metaDataMap.containsKey(key.trim().toLowerCase()))
            throw new ConfigurationException(ExceptionCode.CONFIGURATION_EXCEPTION,
                    String.format("Unable to get value from metadata file '%s' for keyword '%s'", FILE_NAME, key));
        return JournalMetadata.metaDataMap.get(key.trim().toLowerCase());
    }

}
