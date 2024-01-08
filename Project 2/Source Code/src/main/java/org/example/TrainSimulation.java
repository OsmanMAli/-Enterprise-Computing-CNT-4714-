/*
Name: <Osman Ali>
Course: CNT 4714 Fall 2023
Assignment title: Project 2 – Multi-threaded programming in Java
Date: October 10, 2023
Class: <TrainSimulator.java>
Description: To practice programming an application
			 with multiple threads of execution and synchronizing
	 	     their access to shared objects.
*/
package org.example;
import java.io.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

// * TrainSimulation class controls the train movement simulation in the yard, and contains the main functinos


public class TrainSimulation {
    public static void main(String[] args) {

        try {
            PrintStream combinedOut = new PrintStream(new PrintStream(System.out));
            System.setOut(combinedOut);
            
            //Initialize the yard from the CSV file
            Yard yard = new Yard("src/main/resources/org/example/theYardFile.csv");
            int maxTrains = calculateMaxTrains(yard);
            CountDownLatch allTrainsDispatched = new CountDownLatch(maxTrains);
            // Checking if the number of trains in the fleet exceeds the allowed limit

            if (maxTrains > Yard.MAX_TRAINS) {
                System.out.println("The number of trains in the fleet exceeds the allowed limit.");
                System.exit(1);
            }
            // Create a thread pool for train execution

            ExecutorService executor = Executors.newFixedThreadPool(Yard.MAX_TRAINS);
            // Read train details from the fleet file and dispatch trains

            try (BufferedReader fleetReader = new BufferedReader(new FileReader("src/main/resources/org/example/theFleetFile.csv"))) {
                String line;
                System.out.println("Train Movement Simulation Begins...");

                while ((line = fleetReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int trainNumber = Integer.parseInt(parts[0]);
                    int inboundTrack = Integer.parseInt(parts[1]);
                    int outboundTrack = Integer.parseInt(parts[2]);
                    List<ReentrantLock> requiredLocks = yard.getRequiredLocks(inboundTrack, outboundTrack);
                    if (requiredLocks.isEmpty()) {
                        // Train is on permanent hold and cannot be dispatched

                        System.out.println("\n$$$$$$$$ Train " + trainNumber + " is on permanent hold and cannot be dispatched.\n");
                    } else {
                        // Create a new train and execute it in a separate thread

                        Train train = new Train(trainNumber, inboundTrack, outboundTrack, yard, allTrainsDispatched);
                        executor.execute(train);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


            // Wait until all trains are dispatched

            try {
                allTrainsDispatched.await();
                executor.shutdown();
                System.out.println("\n*%*%*%*%*%*% SIMULATION ENDS *%*%*%*%*%*%");
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        } finally {
            System.exit(0);
        }
    }
    
     //Calculates the maximum number of trains that can be dispatched based on available locks in the yard.
     
    
    private static int calculateMaxTrains(Yard yard) {
        int maxTrains = 0;

        try (BufferedReader fleetReader = new BufferedReader(new FileReader("src/main/resources/org/example/theFleetFile.csv"))) {
            String line;
            while ((line = fleetReader.readLine()) != null) {
                String[] parts = line.split(",");
                int inboundTrack = Integer.parseInt(parts[1]);
                int outboundTrack = Integer.parseInt(parts[2]);
                List<ReentrantLock> requiredLocks = yard.getRequiredLocks(inboundTrack, outboundTrack);
                if (!requiredLocks.isEmpty()) {
                    maxTrains++;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return maxTrains;
    }
}
