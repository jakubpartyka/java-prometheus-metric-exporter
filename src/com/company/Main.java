package com.company;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        DefaultExports.initialize();

        Counter counter = Counter.build().namespace("java").name("counter_test").help("This is my counter").register();
        Gauge gauge = Gauge.build().namespace("java").name("gauge_test").help("This is my gauge").register();

        Thread valueSetterThread = new Thread(() -> {
            while (true) {
                try {
                    counter.inc(randomValue(0, 5));
                    gauge.set(randomValue(-5, 10));
                    //noinspection BusyWait
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        valueSetterThread.start();

        try {
            new HTTPServer(8070);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double randomValue(double min, double max) {
        return min + (Math.random() * (max - min));
    }
}
