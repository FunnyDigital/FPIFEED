package com.bitvilltecnologies.fpifeed;


public class User {
    public String name ,phone,regno,level;


    public User(){

    }

    public User(String name, String phone, String regno, String level) {
        this.name = name;
        this.phone = phone;
        this.regno = regno;
        this.level = level;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
