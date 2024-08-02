package com.example;

import java.util.Objects;

import org.junit.jupiter.api.Test;

public class ImageAnalysisCoordinatorTest {

    public ImageAnalysisCoordinatorTest() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ImageAnalysisCoordinatorTest)) {
            return false;
        }
        ImageAnalysisCoordinatorTest imageAnalysisCoordinatorTest = (ImageAnalysisCoordinatorTest) o;
        return Objects.equals(this, imageAnalysisCoordinatorTest);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                "}";
    }

    @Test
    void testHandleRequest() {

    }
}
