package org.csystem.app.camera.car;

import org.csystem.app.camera.car.data.entity.CarCameraInfo;
import org.csystem.util.console.Console;
import org.csystem.util.console.commandline.CommandLineArgsUtil;
import org.csystem.util.numeric.NumberUtil;
import org.csystem.util.thread.ThreadUtil;

import java.util.*;

public class CarCameraInfoSimulationApp {
    private static void doSimulation(int count, Map<String, Set<CarCameraInfo>> carMap)
    {
        var random = new Random();
        var factory = new RandomCarCameraInfoFactory(random);

        for (int i = 0; i < count; ++i) {
            Console.write(".");
            var ci = factory.createCarCameraInfo();

            if (!carMap.containsKey(ci.getPlate())) {
                var set = new TreeSet<CarCameraInfo>(Comparator.reverseOrder());

                set.add(ci);
                carMap.put(ci.getPlate(), set);
            }
            else
                carMap.get(ci.getPlate()).add(ci);

            ThreadUtil.sleep(random.nextLong(300, 700));
        }
    }

    private static void printResult(Map<String, Set<CarCameraInfo>> carMap)
    {
        Console.writeLine("\nAraç geçiş bilgileri:");
        for (var p : carMap.keySet()) {
            Console.write("%s:", p);
            for (var ci : carMap.get(p))
                Console.write("%s ", ci);

            Console.writeLine();
        }
    }

    public static void run(String [] args)
    {
        CommandLineArgsUtil.checkLengthEquals(args, 1, "Wrong number of arguments");

        var optInt = NumberUtil.toInt(args[0]);

        if (optInt.isEmpty()) {
            Console.Error.writeLine("Invalid count value");
            return;
        }

        var carMap = new TreeMap<String, Set<CarCameraInfo>>();
        var count = optInt.getAsInt();

        doSimulation(count, carMap);
        printResult(carMap);
    }
}
