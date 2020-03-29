package com.pierre.cours;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.Math;

public class learningSet {
    String fileName;
    ArrayList<String> attribute = new ArrayList<String>();
    ArrayList<ArrayList<String>> attributeValues = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

    public learningSet(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.createLearningSet(fileName);
    }

    public void createLearningSet(String fileName) throws FileNotFoundException {
        /**
         * The method takes a file name in parameter and parse the csv file to fill attribute,
         *attributeValues and data arrays of the learningSet object
         */
        Scanner scanner = new Scanner(new File(fileName));

        String[] firstLine = scanner.nextLine().split(",");
        attribute.addAll(Arrays.asList(firstLine));

        while (scanner.hasNextLine())
        {
            String[] line = scanner.nextLine().split(",");
            ArrayList<String> lineArray = new ArrayList<String>();
            lineArray.addAll(Arrays.asList(line));
            data.add(lineArray);
            if (attributeValues.size() == 0) {
                for (int i = 0; i < line.length; i++) {
                    ArrayList<String> values = new ArrayList<String>();
                    values.add(line[i]);
                    attributeValues.add(values);
                }
            }
            else{
                for (int i = 0; i < line.length; i++) {
                    if (!(isWordInArray(line[i],attributeValues.get(i)))){
                        attributeValues.get(i).add(line[i]);
                    }
                }
            }
        }
        scanner.close();
        //===============================
        //printing optional
        System.out.println("attributes : " + attribute);
        System.out.println("values : " + attributeValues);
        System.out.println("data : " + data + "\n");
        //===============================
    }

    public boolean isWordInArray(String word, ArrayList<String> array){
        /**
         * The method check if a word is in a given array and return a boolean
         */
        for (String element : array) {
            if (element.equals(word)) {
                return true;
            }
        }
        return false;
    }

    public static double log2(double x)
    {
        return (Math.log(x) / Math.log(2));
    }

    public double entropy(double p,double n){
        if(p-n == p | n-p == n){
            return 0;
        }
        double f1 = p/(p+n);
        double f2 = n/(p+n);
        return (-(f1)*log2(f1)-(f2)*log2(f2));
    }

    public int getIndex(ArrayList<String> table, String word){
        /**
         * The method returns the index of a given word in a given array
         */
        for (int i = 0; i < table.size(); i++){
            if (table.get(i).equals(word)){
                return i;
            }
        }
        return 0;
    }

    public double infoGain(String attrib, String[] previousAttributes, String[] previousValues){
        /**
         * The method returns the gain of a given attribute
         * An association attribute/value can be given in parameter, if so, the method will only consider data that
         *satisfy the condition attribute=value
         */
        int pos = getIndex(this.attribute, attrib);
        int pCount = 0, nCount = 0, totCount = 0, prevCount = 0;
        double weightedAvgValues = 0;

        int[] pCounters = new int[this.attributeValues.get(pos).size()];
        int[] nCounters = new int[this.attributeValues.get(pos).size()];
        int[] totCounters = new int[this.attributeValues.get(pos).size()];


        for (int i = 0; i < this.data.size(); i++) {
            for (int j = 0; j < previousAttributes.length; j++) {
                if (this.data.get(i).get(this.getIndex(this.attribute, previousAttributes[j])).equals(previousValues[j])) {
                    prevCount++;
                }
            }
            if (prevCount == previousAttributes.length){
                if (this.data.get(i).get(this.data.get(i).size() - 1).equals("yes")) {
                    pCounters[getIndex(this.attributeValues.get(pos), this.data.get(i).get(pos))]++;
                }
                if (this.data.get(i).get(this.data.get(i).size() - 1).equals("no")) {
                    nCounters[getIndex(this.attributeValues.get(pos), this.data.get(i).get(pos))]++;
                }
            }
            prevCount = 0;
        }

        for (int i = 0; i < nCounters.length; i++){
            pCount += pCounters[i];
            nCount += nCounters[i];
            totCounters[i] = pCounters[i] + nCounters[i];
        }

        for (int nb: totCounters){
            totCount += nb;
        }

        for (int i = 0; i < nCounters.length; i++) {
            weightedAvgValues += totCounters[i]*entropy(pCounters[i], nCounters[i]);
        }

        weightedAvgValues /= totCount;

        return entropy(pCount, nCount) - weightedAvgValues;
    }

    public String bestAttrib(String[] previousAttributes, String[] previousValues){
        /**
         * The method returns the attribute of the current learning set with the highest gain
         * Conditions can be added like with infoGain()
         */
        double[] gains = new double[this.attribute.size()];
        double  gainMax = 0;
        String maxAttrib = null;
        for(int i = 0; i < this.attribute.size() - 1; i++){
            gains[i] = infoGain(this.attribute.get(i), previousAttributes, previousValues);
            if(gains[i] > gainMax){
                gainMax = gains[i];
                maxAttrib = this.attribute.get(i);
            }
        }
        return maxAttrib;
    }

    public void createTree(){
        /**
         * The method create the decision tree
         */
        String maxAttrib = this.bestAttrib(new String[0], new String[0]);
        node node0 = new node(maxAttrib, this);
        node0.createTree(this.attributeValues.get(getIndex(attribute, maxAttrib)));
    }


}
