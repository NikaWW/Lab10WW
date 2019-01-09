package com.example.weronika.czujniklista9;

import java.util.ArrayList;

public class StepCounter {
    private String TAG;

    public StepCounter(String TAG) {
        this.TAG = TAG;
    }

    public int countSteps(ArrayList<Double> y) {

        int Steps = 0;

        double ymin = y.get(0);

        for (Double f : y) {
            if (ymin > f) ymin = f;
        }

        int counter;
        for (int n = 0; n < y.size(); n++) {
            counter = 0;

            if (y.get(n) < ymin * 0.9) {

                Steps += 1;

                for (int i = 1; i < 20; i++) {
                    if (y.get(n + i) < ymin * 0.9) {
                        counter += 1;
                    }
                }

                if (counter != 0) {
                    Steps -= 1;
                }
            }
        }
        return Steps;
    }

}
