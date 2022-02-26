package test;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Commands {

	// Default IO interface
	public interface DefaultIO {
		public String readText() ;

		public void write(String text);

		public float readVal() ;

		public void write(float val);

		// you may add default methods here
	}

	// the default IO to be used in all commands
	DefaultIO dio;

	public Commands(DefaultIO dio) {
		this.dio = dio;
	}

	// you may add other helper classes here


	// the shared state of all commands
	private class SharedState {
		SimpleAnomalyDetector sd = new SimpleAnomalyDetector();
		TimeSeries test;
		List<AnomalyReport> errorList;


		public void DoTS(String filename)
		{

			test=new TimeSeries(filename);
		}

		public void AnomalyDetector(String filename)
		{
			errorList=sd.detect(new TimeSeries(filename));
		}

		public void AnomalyLearn(String filename)
		{
			sd.learnNormal(new TimeSeries(filename));
		}

	}

	private SharedState sharedState = new SharedState();


	// Command abstract class
	public abstract class Command {
		protected String description;

		public Command(String description) {
			this.description = description;
		}

		public abstract void execute();
	}

	//1.
	public class UploadFileCsv extends Command {
		public UploadFileCsv() {
			super("Please upload your local train CSV file.\n");
		}

		@Override
		public void execute() {
			dio.write("Please upload your local train CSV file.\n");
			try {
				PrintWriter out;
				out = new PrintWriter(new FileWriter("anomalyTrain.csv"));
				String line = dio.readText();
				while (!line.equals("done")) {
					out.println(line);
					line = dio.readText();
				}
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dio.write("Upload complete.\n");

			dio.write("Please upload your local test CSV file.\n");
			try {
				PrintWriter out;
				out = new PrintWriter(new FileWriter("anomalyTest.csv"));
				String line = dio.readText();
				while (!line.equals("done")) {
					out.println(line);
					line = dio.readText();
				}
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dio.write("Upload complete.\n");

		}
	}


	//2.
	public class algoSetting extends Command {
		public algoSetting() {
			super("algorithm settings:\n");
		}

		@Override
		public void execute() {
			dio.write("The current correlation threshold is 0.9\n");
			dio.write("Type a new threshold\n");
			float thshold = Float.parseFloat(dio.readText());
			while (thshold < 0 || thshold > 1) {
				dio.write("Please choose a value between o and 1\n");
				thshold = Float.parseFloat(dio.readText());
			}
			sharedState.sd.setMythershold(thshold);

		}
	}

	//3.
	public class DetectAnomalies extends Command {
		public DetectAnomalies() {
			super("detect anomalies:");
		}

		@Override
		public void execute() {

			sharedState.AnomalyLearn("anomalyTrain.csv");

			sharedState.AnomalyDetector("anomalyTest.csv");

			dio.write("anomaly detection complete.\n");
		}
	}

	//4.
	public class DisplayResults extends Command {
		public DisplayResults() {
			super("display results:");
		}

		@Override
		public void execute() {
			for (int i = 0; i < sharedState.errorList.size(); i++) {
				dio.write(sharedState.errorList.get(i).timeStep);
				dio.write("   ");
				dio.write(sharedState.errorList.get(i).description);
				dio.write("\n");

			}
			dio.write("Done\n");
		}
	}


	//5.
	public class AnalyzeResult extends Command {
		public AnalyzeResult() {
			super("upload anomalies and analyze results:");
		}

		@Override
		public void execute() {
			float TP = 0;
			float FP = 0;
			List<long[]> contiue = new ArrayList<>();
			long arr[] = new long[2];
			String line = dio.readText();
			while (!line.equals("done")) {
				String[] values = line.split(",");
				arr[0] = Long.parseLong(values[0]);
				arr[1] = Long.parseLong(values[1]);
				contiue.add(arr);
				arr=new long [2];
				line = dio.readText();
			}
			dio.write("Please upload your local anomalies file.\n");
			dio.write("Upload complete\n");

				sharedState.DoTS("anomalyTest.csv");
				float Negative = sharedState.test.countLines;
				float Positive=0;
				List<long[]> contiueError = new ArrayList<>();
				long startindex = 0;
				long endindex = 1;
				boolean flag=false;
				for (int i = 0, index = i; i < sharedState.errorList.size() - 1; i++, index = i)
				{
					for (int j = i + 1; j < sharedState.errorList.size(); j++) {
						if (sharedState.errorList.get(i).timeStep + 1 == sharedState.errorList.get(j).timeStep && sharedState.errorList.get(i).description.equals(sharedState.errorList.get(j).description)) {
							startindex = sharedState.errorList.get(index).timeStep;
							flag = true;
							i++;
						}
						if (!flag) {
							break;
						}
						flag = false;
					}
					endindex= sharedState.errorList.get(i).timeStep;
					long[] arrError = {startindex, endindex};
					Negative-=endindex-startindex+1;
					contiueError.add(arrError);
				}

              Positive=contiue.size();

               boolean [] reported=new boolean[contiueError.size()];
			for(int i=0;i<contiueError.size();i++) {
			reported[i]=false;
			}

				for(int i=0;i<contiueError.size();i++) {
					for (int j = 0; j < contiue.size(); j++) {
						if (contiueError.get(i)[1] - contiue.get(j)[0] >= 0 && contiue.get(j)[1] - contiueError.get(i)[0] >= 0)
							{
								reported[i]=true;
							}
					}
				}


			for(int i=0;i<contiueError.size();i++) {
				if(reported[i]==false)
					FP++;
				else
					TP++;
			}

			DecimalFormat df = new DecimalFormat("#0.0");
			    df.setMaximumFractionDigits(3);
			    df.setRoundingMode(RoundingMode.DOWN);

			    float TruePositve=TP/Positive;
			    dio.write("True Positive Rate: ");
			    dio.write(df.format(TruePositve));
				dio.write("\n");


				float FalsePostive=FP/Negative;
				dio.write("False Positive Rate: ");
			   dio.write(df.format(FalsePostive));
			  dio.write("\n");


		}
	}
}
