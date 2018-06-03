package cn.azure.chatbot.preprocessingservice;

import com.google.common.collect.Lists;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Segmenter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final HashMap<String, String> synonymMap = new HashMap<>();
    private final HashMap<String, List<String>> reversedSynonymMap = new HashMap<>();
    private final HashSet<String> stopWords = new HashSet<>();
    private final JiebaSegmenter segmenter;

    @Autowired
    public Segmenter(AppConfig config) {
        if (!config.getUserDictPath().isEmpty()) {
            WordDictionary.getInstance().loadUserDict(Paths.get(config.getUserDictPath()));
        }
        segmenter = new JiebaSegmenter();
        if (!config.getSynonymListPath().isEmpty()) {
            // Load synonym map
            log.info("Loading synonym map at '{}'...", config.getSynonymListPath());
            try (Stream<String> stream = Files.lines(Paths.get(config.getSynonymListPath()))) {
                stream.forEach(line -> {
                    line = line.trim();
                    // Skip empty lines
                    if (line.isEmpty()) return;
                    // Skip comments
                    if (line.codePointAt(0) == '#') return;
                    // Line should be in the format "word => syn1, syn2, ..."
                    String[] parts = line.split("=>");
                    if (parts.length != 2) {
                        log.warn("Invalid line in Synonym file: '{}'", line);
                        return;
                    }
                    String[] words = parts[1].split(",");
                    // "word => syn1, syn2..." will generate "syn1:word", "syn2:word", ...
                    Arrays.stream(words).forEach(word -> {
                        synonymMap.put(word.trim(), parts[0].trim());
                    });
                    // Reversed map entry is "word: list(syn1, syn2...)"
                    reversedSynonymMap.put(parts[0].trim(), Lists.newArrayList(words));
                });
                log.info("Synonym map loaded.");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        if (!config.getStopwordsPath().isEmpty()) {
            log.info("Loading stop words list at '{}'...", config.getStopwordsPath());
            try (Stream<String> stream = Files.lines(Paths.get(config.getStopwordsPath()))) {
                stream.forEach(line -> {
                    stopWords.add(line.toLowerCase().trim());
                });
                log.info("Stop words list loaded.");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @NotNull
    List<String> cut(@NotNull String text) {
        // TODO: Language phrases
        return segmenter.process(text, JiebaSegmenter.SegMode.SEARCH)
                .stream()
                .map(token -> token.word)
                .map(String::toLowerCase)
                .map(String::trim)
                .map(this::spellCheck)
                .map(this::replaceSynonym)
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toList());
    }

    @NotNull
    List<String> cutAll(@NotNull String text) {
        return segmenter.process(text, JiebaSegmenter.SegMode.INDEX)
                .stream()
                .map(token -> token.word)
                .map(String::toLowerCase)
                .map(String::trim)
                .map(this::spellCheck)
                .map(this::replaceSynonym)
                .filter(word -> !stopWords.contains(word))
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
        word = word.toLowerCase().trim();
        // TODO: Spell check
        return word;
    }

    @NotNull
    List<String> getSynonyms(String word) {
        word = word.toLowerCase().trim();
        if (reversedSynonymMap.containsKey(word))
            return reversedSynonymMap.get(word);
        return Collections.emptyList();
    }
}
