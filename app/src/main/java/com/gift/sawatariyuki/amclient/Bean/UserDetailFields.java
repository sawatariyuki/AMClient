package com.gift.sawatariyuki.amclient.Bean;

import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;

import java.io.Serializable;
import java.util.Date;

public class UserDetailFields implements Serializable {
    private String gender;
    private Float weight;
    private Date birthday;
    private int age;
    private String birthplace;
    private String liveplace;

    public UserDetailFields(String gender, Float weight, String birthday, int age, String birthplace, String liveplace) {
        this.gender = gender;
        this.weight = weight;
        this.birthday = TimeZoneChanger.StringUTCToDateLocal(birthday);
        this.age = age;
        this.birthplace = birthplace;
        this.liveplace = liveplace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getLiveplace() {
        return liveplace;
    }

    public void setLiveplace(String liveplace) {
        this.liveplace = liveplace;
    }

    @Override
    public String toString() {
        return "UserDetailFields{" +
                "gender='" + gender + '\'' +
                ", weight=" + weight +
                ", birthday=" + birthday +
                ", age=" + age +
                ", birthplace='" + birthplace + '\'' +
                ", liveplace='" + liveplace + '\'' +
                '}';
    }
}
