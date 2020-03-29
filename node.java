package com.pierre.cours;

import java.util.ArrayList;
import java.util.Arrays;

public class node {
    public String name;
    public node parent;
    public ArrayList<String>  branches;
    public ArrayList<node> children;
    public com.pierre.cours.learningSet lset;
    public String[] parentCond;
    public String[] parentVal;

    public static int nodeCount;
    public String ref;

    public node(String name, com.pierre.cours.learningSet lset) {
        this.name = name;
        this.lset = lset;
        this.ref = name + nodeCount;
    }

    public String[] concatenate(String[] t1, String[] t2) {
        String[] res = Arrays.copyOf(t1, t1.length + t2.length);
        System.arraycopy(t2, 0, res, t1.length, t2.length);
        return res;
    }

    public void createTree(ArrayList<String> attribvalues) {
        this.branches = attribvalues;

        for (String value: attribvalues){
            ArrayList<String> decisions = new ArrayList<String>();
            for (ArrayList<String> d: lset.data){
                if (this.parentCond.length == 0) {
                    if (d.get(lset.getIndex(lset.attribute, this.name)).equals(value)) {
                        if (!(lset.isWordInArray(d.get(d.size() - 1), decisions))) {
                            decisions.add(d.get(d.size() - 1));
                        }
                    }
                }
                else{
                    int compt = 0;
                    for (int i = 0; i < this.parentCond.length; i++){
                        if (d.get(lset.getIndex(lset.attribute, this.parentCond[i])).equals(this.parentVal[i])){
                            compt++;
                        }
                    }
                    if(compt == this.parentCond.length){
                        if (d.get(lset.getIndex(lset.attribute, this.name)).equals(value)) {
                            if (!(lset.isWordInArray(d.get(d.size() - 1), decisions))) {
                                decisions.add(d.get(d.size() - 1));
                            }
                        }
                    }
                }
            }

            if (decisions.size() == 2){

                String maxAttrib = lset.bestAttrib(parentCond, parentVal);
                node ref = new node(maxAttrib, lset);
                ref.parent = this;
                this.children.add(ref);
                ref.parentCond = concatenate(this.parentCond, new String[]{this.name});
                ref.parentVal = concatenate(this.parentVal, new String[]{value});
                nodeCount++;
                ref.createTree(lset.attributeValues.get(lset.getIndex(lset.attribute, maxAttrib)));
            }
            else if (decisions.size() == 1 && decisions.get(0).equals("no")){
                node ref = new node("no", lset);
                ref.parent = this;
                this.children.add(ref);
                ref.parentCond = concatenate(this.parentCond, new String[]{this.name});
                ref.parentVal = concatenate(this.parentVal, new String[]{value});
                nodeCount++;

            }
            else if (decisions.size() == 1 && decisions.get(0).equals("yes")){
                node ref = new node("yes", lset);
                ref.parent = this;
                this.children.add(ref);
                ref.parentCond = concatenate(this.parentCond, new String[]{this.name});
                ref.parentVal = concatenate(this.parentVal, new String[]{value});
                nodeCount++;
            }

        }

    }
}
