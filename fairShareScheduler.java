import java.util.*;

class Process {

    int id;
    int arrivalTime;
    int runtime;
    int remainingTime;
    int deadline;
    int completionTime;

    public Process(int id, int arrivalTime, int runtime, int deadline) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.runtime = runtime;
        this.remainingTime = runtime;
        this.deadline = deadline;
    }
}

public class fairShareScheduler {

    //change this for the number of processes
    static final int TOTAL_PROCESSES = 100; 

    public static void main(String[] args) {

        List<Process> processes = generateProcesses();
        List<Process> completed = simulate(processes);

        System.out.println("Process Completion Report\n");

        for (Process p : completed) {
            System.out.println("Process " + p.id +
                    " completed at time " + p.completionTime);
        }
    }

    // Generate processes according to assignment specification
    static List<Process> generateProcesses() {

        List<Process> processes = new ArrayList<>();

        int arrival = 0;
        //change the 2 lines below for their respective cycles
        int runtimeCycle[] = {5,10,15,20,25,30,35,40,45,50};
        int deadlineCycle[] = {10,20,30,40,50,60,70,80,90,100};

        for (int i = 0; i < TOTAL_PROCESSES; i++) {

            //change the number in the 2 lines below for the indexing
            int runtime = runtimeCycle[i % 10];
            int deadline = deadlineCycle[i % 10];

            processes.add(new Process(i, arrival, runtime, deadline));

            if ((i + 1) % 5 == 0) {
                arrival += 5; //change this lines number for the arrival timing
            }
        }

        return processes;
    }

    static List<Process> simulate(List<Process> processes) {

        List<Process> completed = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>();

        int time = 0;
        int index = 0;

        while (completed.size() < TOTAL_PROCESSES) {

            // add arriving processes
            while (index < processes.size() &&
                    processes.get(index).arrivalTime <= time) {

                readyQueue.add(processes.get(index));
                index++;
            }

            if (readyQueue.isEmpty()) {
                time++;
                continue;
            }

            // Fair share selection (process with least CPU received relative to runtime)
            Process current = readyQueue.get(0);

            for (Process p : readyQueue) {
                if (p.remainingTime < current.remainingTime) {
                    current = p;
                }
            }

            // Run process for 1 time unit (preemptive)
            current.remainingTime--;
            time++;

            if (current.remainingTime == 0) {
                current.completionTime = time;
                completed.add(current);
                readyQueue.remove(current);
            }
        }

        return completed;
    }
}