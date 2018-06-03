package cn.azure.chatbot.preprocessingservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(description = "Text preprocessing API")
public class PreprocessingController {
    private final Segmenter segmenter;
    private final WordVectorizer vectorizer;
    private final Map<String, Object> infoMap;

    @Autowired
    public PreprocessingController(AppConfig config, Segmenter segmenter, WordVectorizer vectorizer) {
        this.segmenter = segmenter;
        this.vectorizer = vectorizer;
        infoMap = new HashMap<String, Object>() {{
            put("name", "preprocessing-service");
            put("version", "v1");
            put("synonym-list", config.getSynonymListPath());
            put("user-dictionary", config.getUserDictPath());
            put("word-vector-dimension", vectorizer.getDimension());
        }};
    }

    @RequestMapping(method = RequestMethod.GET, path = "/info")
    @ApiOperation("Returns information about this preprocessing service.")
    Map<String, Object> getInfo() {
        return infoMap;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/cut")
    @ApiOperation("Cut text into words.")
    List<String> cut(@RequestBody String text) {
        return segmenter.cut(text);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/cutforindex")
    @ApiOperation("Cut text into words, returns all possible candidates for indexing.")
    List<String> cutForIndex(@RequestBody String text) {
        return segmenter.cutAll(text);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/synonyms")
    @ApiOperation("Get synonym list of a word.")
    List<String> getSynonyms(@RequestBody String word) {
        return segmenter.getSynonyms(word);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/vectorize")
    @ApiOperation("Expand text into a list of word vectors.")
    List<List<Float>> vectorize(@RequestBody String text, @ApiParam("Sequence length, 0 if no padding needed") @RequestParam(value = "seqlen", defaultValue = "0") int seqlen) {
        return vectorizer.getVectors(segmenter.cut(text), seqlen);
    }
}
