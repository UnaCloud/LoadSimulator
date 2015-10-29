/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator;

import cpu.CpuLoad;
import java.io.FileNotFoundException;
import java.io.IOException;
import ramload.RamLoad;

/**
 *
 * @author Antonio de la Vega
 * args[0] ram log path
 * args[1] ram jar path.
 * 
 */
public class Simulator {
    /** args[0] direccion del archivo de ohm
     *  args[1] direccion donde queda el archivo procesado de ram.
     *  args[2] direccion donde queda el archivo procesado de cpu.
     *  args[3] direccion donde se encuentra el JAR de carga de ram.
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException{
        //DataProcessor d = new DataProcessor("./data/ohm.csv");
        //d.ohm("./data/ramload1.txt", "./data/cpuload.txt");
        try{
            RamLoad r = new RamLoad("./data/ramload.txt", "./data/RamTest.jar");
            CpuLoad c = new CpuLoad("./data/cpuload.txt");
            r.start();
            c.start();
        }
        catch(IOException | InterruptedException e){System.out.println(e.getMessage());}
    }
}
