package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class TimeSeries {

	List<String[]> lst = new ArrayList<>();
	protected String line="";
	int countLines=-1;//witoutnames ABCDE

	public TimeSeries(String csvFileName) {
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			 while((line = br.readLine()) != null && line!="done"){
				 countLines++;
				 String [] values = line.split(",");
				 lst.add(values);
		
			 }
		 
		 } catch (Exception e){
			 System.out.println(e);
			}
		}

		

	public String[] getNames(List<String[]> namesList){
		return namesList.get(0);
	}



	public float[] getColum(String name ){
		String[] nameArray = getNames(lst);
		float[] onlynumbrs = new float[lst.size()-1];
		int index = 0;
		for(int i =0; i < nameArray.length;i++){
			String temp = nameArray[i];

			if(name.equals(temp))
			{
			index = i;
			break;
			}
		}
		for(int j = 1; j < lst.size(); j++)
		{
			onlynumbrs[j-1] =  Float.parseFloat(lst.get(j)[index]);//only numbres 
		}
      return onlynumbrs;
	}

	public List<String[]> GetCsvList()
	{
		return lst;
	}
	
public float Maxdist(Point[] p,Line l)
{
	float maxi = 0;
	for(int i =0; i < lst.size()-1; i++)
	{
		 if(StatLib.dev(p[i], l) > maxi)
		 maxi = StatLib.dev(p[i], l);

	}
	return maxi;
}


}