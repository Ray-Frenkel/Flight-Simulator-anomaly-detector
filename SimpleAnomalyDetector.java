package test;

import java.util.ArrayList;
import java.util.List;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	List<CorrelatedFeatures> correl=new ArrayList<>();
	float mythershold=(float) 0.9;


	public void setMythershold(float newthersold)
	{
		this.mythershold=newthersold;
	}
	public float getMythershold()
	{
		return mythershold;
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		
        List<String[]> csvList = ts.GetCsvList();
		String [] arrName = ts.getNames(csvList);

		int index = -1;
		float calc = 0;
		float corr = getMythershold();
		for(int i = 0; i < ts.lst.get(0).length; i++)
		{
			index = -1;
			float maxi = (float)corr;
			float [] x=ts.getColum(arrName[i]);
			for(int j = i+1; j < ts.lst.get(0).length; j++)
			{
			
			float [] y=ts.getColum(arrName[j]);

				calc = Math.abs(StatLib.pearson(x,y));
				if( calc > maxi && calc > corr)
				{
				maxi = calc;
				index = j;

			
				}
			}
			if(index != -1)
			{
			
			Point[] points = new Point[ts.lst.size()-1];
			float [] maxcolum=ts.getColum(arrName[index]);//
			for(int k =0; k < ts.lst.size()-1;k++)
			{
				points[k] = new Point(x[k],maxcolum[k]);
			}
			CorrelatedFeatures c=new CorrelatedFeatures(arrName[i],arrName[index],maxi,StatLib.linear_reg(points),(float)(ts.Maxdist(points, StatLib.linear_reg(points))*1.1));
	     	correl.add(c);
		}
		
	}


	}


	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> error = new ArrayList<>();
		Point[] points = new Point[ts.lst.size()-1];

		for(int i=0;i<correl.size();i++)
		{
			float [] f1=ts.getColum(correl.get(i).feature1);
			float [] f2=ts.getColum(correl.get(i).feature2);
			float threshold = correl.get(i).threshold;

			for(int j=0; j < ts.lst.size()-1; j++)
			{
				points[j]= new Point(f1[j], f2[j]);
			}
			for(int k =0; k < ts.lst.size()-1; k++)
			{
				if(StatLib.dev(points[k], correl.get(i).lin_reg)>threshold)
				{
					String description = correl.get(i).feature1 +"-"+ correl.get(i).feature2;
					long timeStep = k+1;
					AnomalyReport ar = new AnomalyReport(description, timeStep);
					error.add(ar);
				}


			}
		}
	
		
		return error;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
	return correl;
	}
}