package com.org.somak.datastore.util;

import com.google.common.collect.ImmutableMap;
import com.org.somak.datastore.configuration.engine.EngineMetadata;
import com.org.somak.datastore.configuration.journal.JournalMetadata;
import com.org.somak.datastore.engine.impl.FileSequencer;
import com.org.somak.datastore.exception.ConfigurationException;
import com.org.somak.datastore.exception.ExceptionCode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class Utility {

    private final static String KRYO_OUTPUT_FILE_NAME="FILE_OUTPUT.bin";
    public static String getSerializedOutputDirectoryName(String tableName){

        Objects.requireNonNull(tableName);
        String journalDirectory = JournalMetadata.getEngineMetaData("JOURNAL_DIR");
        return journalDirectory+"\\"+tableName+"\\";
    }

    public static String getSerializedOutputFileName(String tableName) throws IOException {

        Objects.requireNonNull(tableName);
        String dirName = getSerializedOutputDirectoryName(tableName);
        createPath(dirName);
        String fileName = FileSequencer.uniqueInstance.fileNumber(tableName);
        fileName = fileName.concat("_").concat(KRYO_OUTPUT_FILE_NAME);
        return dirName+fileName;
    }

    public static void createPath(String directoryName) throws IOException{
        if(!Path.of(directoryName).toFile().exists())
            Files.createDirectories(Path.of(directoryName));
    }
    public static ImmutableMap.Builder<String,String> readFromFile(URL fileName) {

        ImmutableMap.Builder<String,String> builder = ImmutableMap.builder();

        try (InputStream is = fileName.openConnection().getInputStream();)
        {
            Properties properties = new Properties();
            properties.load(is);
            Enumeration enuKeys = properties.keys();
            Map<String, String> envMap = System.getenv();

            while (enuKeys.hasMoreElements()) {

                String key = (String) enuKeys.nextElement();

                if(envMap!=null && envMap.containsKey(key)){
                    builder.put(key.toLowerCase(),envMap.get(key));
                }
                else{
                    builder.put(key.toLowerCase(),properties.getProperty(key));
                }
            }
        }
        catch (IOException e)
        {
            throw new ConfigurationException(ExceptionCode.CONFIGURATION_EXCEPTION,
                    String.format("Unable to read reserved keywords file '%s'", fileName), e);
        }
        return builder;
    }

}
