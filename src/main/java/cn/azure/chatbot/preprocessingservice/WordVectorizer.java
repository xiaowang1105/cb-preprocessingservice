package cn.azure.chatbot.preprocessingservice;

import com.github.jfasttext.JFastText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WordVectorizer {
    private static final int DIMENSION = 300;
    private static final List<Float> EMPTY_VECTOR = Collections.nCopies(DIMENSION, 0f);
    private final JFastText jft = new JFastText();
    private final Set<String> words;

    @Autowired
    WordVectorizer(AppConfig config) {
        try {
            jft.loadModel(config.getFastTextModelPath());
            words = new HashSet<>(jft.getWords());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    List<Float> getWordVector(String word) {
        word = word.toLowerCase().trim();
        if (words.contains(word)) {
            return jft.getWordVector(word);
        }
        return EMPTY_VECTOR;
    }
}
