package cpu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CpuLoad {

    private static final int CORES = Runtime.getRuntime().availableProcessors();

    private final String path;
    
    public CpuLoad(String path){
        this.path = path;
    }

    public void start() throws InterruptedException, IOException {
        ExecutorService e = Executors.newFixedThreadPool(CORES);
        
        try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {

            String data = "";

            Linpack[] loads = new Linpack[CORES];

            for (int i = 0; i < CORES; i++) {
                loads[i] = new Linpack(0, i);
                loads[i].start();
            }

            while ((data = br.readLine()) != null) {
                String[] values = data.split(",");
                long delay = Long.parseLong(values[0]);
                for (int i = 0; i < CORES; i++) {
                    loads[i].changeLoad(Integer.parseInt(values[i+1]));
                }
                Thread.sleep(delay * 1000);
            }

            for (int i = 0; i < CORES; i++) {
                loads[i].stopLoad();
            }
        }
        
    }


}
