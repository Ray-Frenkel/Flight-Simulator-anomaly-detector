package test;

import java.util.ArrayList;

import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;

	public CLI(DefaultIO dio) {
		this.dio = dio;
		c = new Commands(dio);
		commands = new ArrayList<>();
		commands.add(c.new UploadFileCsv());
		commands.add(c.new algoSetting());
		commands.add(c.new DetectAnomalies());
		commands.add(c.new DisplayResults());
		commands.add(c.new AnalyzeResult());
		// example: commands.add(c.new ExampleCommand());
		// implement
	}

	public void start() {
		String option;
		option = dio.readText();
		while (!option.equals("6")) {
			dio.write("Welcome to the Anomaly Detection Server.\n");
			dio.write("Please choose an option:\n");
			dio.write("1. upload a time series csv file\n");
			dio.write("2. algorithm settings\n");
			dio.write("3. detect anomalies\n");
			dio.write("4. display results\n");
			dio.write("5. upload anomalies and analyze results\n");
			dio.write("6. exit\n");
			int val = Integer.parseInt(option);
			commands.get(val - 1).execute();
			option = dio.readText();
		}
	}

}
