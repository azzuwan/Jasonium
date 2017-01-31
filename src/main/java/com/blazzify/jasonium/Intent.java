package com.blazzify.jasonium;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class Intent {
    public Intent(String index, String intent, String text, String entities) {
        this.index = index;
        this.intent = intent;
        this.text = text;
        this.entities = entities;
    }

    public Intent() {
    }
    private String index;
    private String intent;
    private String text;
    private String entities;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEntities() {
        return entities;
    }

    public void setEntities(String entities) {
        this.entities = entities;
    }

    /**
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(String index) {
        this.index = index;
    }
}
