package ru.grishman.springcourse.FirstRestApp.Properties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    public void readerProperties() throws IOException {
        Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
        String url = properties.getProperty("spring.datasource.url");
        System.out.println(url);
    }


}
