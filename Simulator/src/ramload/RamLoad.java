/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ramload;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Only works for Windows OS at the moment.
 */
public class RamLoad {

    private static final int MAX_LOAD = 512000;

    private final int totalmem;

    private final int occupiedmem;

    private final int totalProcesses;

    private final String jarPath;

    private final BufferedReader fileReader;

    private final ArrayList<String> parameters;

    public static int freeRam() throws IOException {
        // In KiBytes, available RAM of the system.
        Process memavailable = Runtime.getRuntime().exec("wmic OS get FreePhysicalMemory");
        BufferedReader br = new BufferedReader(new InputStreamReader(memavailable.getInputStream()));
        br.readLine();
        br.readLine();
        return Integer.parseInt(br.readLine().trim());
    }

    public static int totalRam() throws IOException {
        // In KiBytes, Total RAM of the system.
        Process memtot = Runtime.getRuntime().exec("wmic OS Get TotalVisibleMemorySize");

        // Lector de consola para tamaño total de RAM del equipo.
        BufferedReader br = new BufferedReader(new InputStreamReader(memtot.getInputStream()));
        br.readLine();
        br.readLine();
        // in KiBytes
        return Integer.parseInt(br.readLine().trim());
    }

    private int maxProcessesNeeded(int ram) {
        return (ram % MAX_LOAD == 0) ? ram / MAX_LOAD : ram / MAX_LOAD + 1;
    }

    private int memNeeded(int percentage) {
        return (totalmem * percentage / 100 - occupiedmem) / totalProcesses;
    }

    private ArrayList<String> arguments() throws IOException {
        ArrayList<String> args = new ArrayList<>();
        args.add("java");
        args.add("-Xms550m");
        args.add("-Xmx800m");
        args.add("-jar");
        args.add("\"" + jarPath + "\"");
        String data = "";
        while ((data = fileReader.readLine()) != null) {
            String parse[] = data.split(",");
            args.add(parse[0] + "," + memNeeded(Integer.parseInt(parse[1])));
        }
        return args;
    }

    /**
     * @param logPath
     * @param jarPath
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public RamLoad(String logPath, String jarPath) throws IOException, InterruptedException {

        this.jarPath = jarPath;

        totalmem = totalRam();

        occupiedmem = totalmem - freeRam();

        totalProcesses = maxProcessesNeeded(freeRam());

        fileReader = new BufferedReader(new FileReader(logPath));

        parameters = arguments();

    }

    public void start() throws IOException {

        ProcessBuilder pb = new ProcessBuilder(parameters);

        for (int i = 0; i < totalProcesses; i++) {
            pb.start();
        }
    }

}
