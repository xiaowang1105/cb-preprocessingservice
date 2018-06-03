package cn.azure.chatbot.preprocessingservice;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Api(description = "Text preprocessing API")
public class PreprocessingController {
    private final Segmenter segmenter;
    private final WordVectorizer vectorizer;
    private final Map<String, String> infoMap;

    @Autowired
    public PreprocessingController(AppConfig config, Segmenter segmenter, WordVectorizer vectorizer) {
        this.segmenter = segmenter;
        this.vectorizer = vectorizer;
        infoMap = new HashMap<String, String>() {{
            put("name", "preprocessing-service");
            put("version", "v1");
            put("synonym-list", config.getSynonymListPath());
            put("user-dictionary", config.getUserDictPath());
        }};
    }

    @RequestMapping(method = RequestMethod.GET, path = "/info")
    Map<String, String> getInfo() {
        return infoMap;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/cut")
    List<String> cut(@RequestBody String text) {
        return segmenter.cut(text);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/cutforindex")
    List<String> cutForIndex(@RequestBody String text) {
        return segmenter.cutAll(text);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/synonyms")
    List<String> getSynonyms(@RequestBody String word) {
        return segmenter.getSynonyms(word);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/vectorize")
    List<List<Float>> vectorize(String text) {
        return segmenter.cut(text).stream().map(vectorizer::getWordVector).collect(Collectors.toList());
    }
}
