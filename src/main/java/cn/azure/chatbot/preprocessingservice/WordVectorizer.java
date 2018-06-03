package cn.azure.chatbot.preprocessingservice;

import com.github.jfasttext.JFastText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WordVectorizer {
    private final int DIMENSION;
    private final List<Float> EMPTY_VECTOR;
    private final JFastText jft = new JFastText();

    @Autowired
    WordVectorizer(AppConfig config) {
        this(config.getFastTextModelPath());
    }

    WordVectorizer(String modelPath) {
        try {
            jft.loadModel(modelPath);
            DIMENSION = jft.getDim();
            EMPTY_VECTOR = Collections.nCopies(DIMENSION, 0f);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    List<Float> getWordVector(String word) {
        if (word == null || word.isEmpty())
            return EMPTY_VECTOR;
        return jft.getWordVector(word);
    }

    List<List<Float>> getVectors(List<String> words, int seqlen) {
        List<List<Float>> ret = words
                .stream()
                .limit(seqlen == 0 ? Integer.MAX_VALUE : seqlen)
                .map(this::getWordVector)
                .collect(Collectors.toList());
        if (seqlen == 0) return ret;
        if (seqlen > ret.size()) {
            // Padding
            ret.addAll(Collections.nCopies(seqlen - ret.size(), EMPTY_VECTOR));
        }
        return ret;
    }

    int getDimension() {
        return DIMENSION;
    }
}
