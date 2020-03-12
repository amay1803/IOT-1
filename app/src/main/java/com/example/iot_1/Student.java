package com.example.iot_1;

public class Student {
    private String number;


    Student(){}

    @Override
    public String toString() {
        return number;
    }

    public Student(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
