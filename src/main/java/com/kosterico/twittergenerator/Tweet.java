package com.kosterico.twittergenerator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.io.Serializable;
import java.util.Properties;

public class Tweet implements Serializable {

    private final String text;

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }

    private int type = -1;

    public Tweet(String text) {
        this.text = text;
    }

    public void analyzeType() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(text);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            this.type = RNNCoreAnnotations.getPredictedClass(tree);
            return;
        }
        this.type = 0;
    }

}
