package cn.azure.chatbot.preprocessingservice;

import com.google.common.collect.Lists;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Segmenter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private HashMap<String, String> synonymMap = new HashMap<>();
    private HashMap<String, List<String>> reversedSynonymMap = new HashMap<>();
    private JiebaSegmenter segmenter = new JiebaSegmenter();

    @Autowired
    public Segmenter(AppConfig config) {
        if (!config.getSynonymListPath().isEmpty()) {
            // Load synonym map
            log.info("Loading synonym map at '{}'", config.getSynonymListPath());
            try (Stream<String> stream = Files.lines(Paths.get(config.getSynonymListPath()))) {
                stream.forEach(line -> {
                    line = line.trim();
                    // Skip empty lines
                    if (line.isEmpty()) return;
                    // Skip comments
                    if(line.codePointAt(0)=='#') return;
                    // Line should be in the format "word => syn1, syn2, ..."
                    String[] parts = line.split("=>");
                    if(parts.length!=2) {
                        log.warn("Invalid line in Synonym file: '{}'", line);
                        return;
                    }
                    String[] words=parts[1].split(",");
                    // "word => syn1, syn2..." will generate "syn1:word", "syn2:word", ...
                    Arrays.stream(words).forEach(word -> {
                        synonymMap.put(word.trim(), parts[0].trim());
                    });
                    // Reversed map entry is "word: list(syn1, syn2...)"
                    reversedSynonymMap.put(parts[0].trim(), Lists.newArrayList(words));
                });
                log.info("Synonym map loaded");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @NotNull
    List<String> cut(@NotNull String text) {
        return segmenter.process(text, JiebaSegmenter.SegMode.SEARCH)
                .stream()
                .map(SegToken::toString)
                .map(this::spellCheck)
                .map(this::replaceSynonym)
                .collect(Collectors.toList());
    }

    @NotNull
    List<String> cutAll(@NotNull String text) {
        return segmenter.process(text, JiebaSegmenter.SegMode.INDEX)
                .stream()
                .map(SegToken::toString)
                .map(this::spellCheck)
                .map(this::replaceSynonym)
                .collect(Collectors.toList());
    }

    @NotNull
    private String replaceSynonym(@NotNull String word) {
        if (synonymMap.containsKey(word))
            return synonymMap.get(word);
        return word;
    }

    @NotNull
    private String spellCheck(@NotNull String word) {
        // TODO: Spell check
        return word;
    }

    @NotNull
    List<String> getSynonyms(String word) {
        word=word.trim();
        if (reversedSynonymMap.containsKey(word))
            return reversedSynonymMap.get(word);
        return Collections.emptyList();
    }
}
