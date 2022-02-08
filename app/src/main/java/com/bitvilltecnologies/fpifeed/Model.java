package com.bitvilltecnologies.fpifeed;

public class Model {

    String title,image,description,full_description,date;


    public Model(){

    }

    //public Model(String title, String image, String description) {
    //this.title = title;
    //this.image = image;
    //this.description = description;
    //   }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFull_description() {
        return full_description;
    }

    public void setFull_description(String full_description) {
        this.full_description = full_description;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
