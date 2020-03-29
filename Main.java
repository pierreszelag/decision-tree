package com.pierre.cours;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //creation of the object test, the creatLearningSet method is called in the constructor
        //CHANGE THE PATH ACCORDING TO YOUR SYSTEM
        learningSet test = new learningSet("/home/pierre/IdeaProjects/decision-tree/src/com/pierre/cours/data/golf.csv");

        //gain of each attribute with no condition
        System.out.println("gain outlook : " + test.infoGain("outlook", new String[0], new String[0]));
        System.out.println("gain temp : " + test.infoGain("temp", new String[0], new String[0]));
        System.out.println("gain humidity : " + test.infoGain("humidity", new String[0], new String[0]));
        System.out.println("gain wind : " + test.infoGain("wind", new String[0], new String[0]));
        System.out.println("best attribute : " + test.bestAttrib(new String[0], new String[0]) + "\n");
        //example of gain with conditions
        System.out.println("gain humidity when outlook=sunny : " + test.infoGain("humidity", new String[]{"outlook"}, new String[]{"sunny"})+ "\n");

        //creation of the decision tree
        test.createTree();
    }
}

