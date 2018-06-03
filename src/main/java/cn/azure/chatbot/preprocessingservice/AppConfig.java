package cn.azure.chatbot.preprocessingservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
public class AppConfig {
    private String stopwordsPath = "";
    private String userDictPath = "";
    private String synonymListPath = "";
    private String fastTextModelPath = "/models/cc.zh.300.bin";

    public String getUserDictPath() {
        return userDictPath;
    }

    public String getSynonymListPath() {
        return synonymListPath;
    }

    public String getFastTextModelPath() {
        return fastTextModelPath;
    }

    public String getStopwordsPath() {
        return stopwordsPath;
    }
}
