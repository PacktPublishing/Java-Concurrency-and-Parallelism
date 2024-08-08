package com.example;

public class Tuple<X, Y> {
    public final X first;
    public final Y second;

    public Tuple(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    public static void main(String[] args) {
        // Creating a tuple of String and Integer
        Tuple<String, Integer> personAge = new Tuple<>("Joe", 30);
        System.out.println(personAge);
    }
}
