package test;


public class StatLib {


	// simple average
	public static float avg(float[] x){
		int i  = 0;
		float avg1 = 0;
		for(i=0; i < x.length; i++)
		{
			avg1 += x[i];
		}
		avg1 = avg1/ (x.length);
		return avg1;
	}

	// returns the variance of X and Y
	public static float var(float[] x){

		float sum = 0;
		float mean = 0;
		for(int i=0;i<x.length;i++) 
		{
			sum=sum+x[i];
		}
		mean=sum/x.length;
		sum=0;  
		for(int i=0;i<x.length;i++) 
		{
			sum+=Math.pow((x[i]-mean),2);
		
		}
		mean=sum/(x.length);
		return mean;
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		 float sum = 0;
	     
		    for(int i = 0; i < x.length; i++)
		        sum = sum + (x[i] - avg(x)) *
		                        (y[i] - avg(y));
		    return sum / (x.length);

	}


	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float sum_X = 0, sum_Y = 0, sum_XY = 0;
        float squareSum_X = 0, squareSum_Y = 0;
       
        for (int i = 0; i < x.length; i++)
        {
            // sum of elements of array X.
            sum_X = sum_X + x[i];
       
            // sum of elements of array Y.
            sum_Y = sum_Y + y[i];
       
            // sum of X[i] * Y[i].
            sum_XY = sum_XY + x[i] * y[i];
       
            // sum of square of array elements.
            squareSum_X = squareSum_X + x[i] * x[i];
            squareSum_Y = squareSum_Y + y[i] * y[i];
        }
        float corr = (float)(x.length * sum_XY - sum_X * sum_Y)/
                (float)(Math.sqrt((x.length * squareSum_X -
                sum_X * sum_X) * (x.length * squareSum_Y - 
                sum_Y * sum_Y)));
  
   return corr;
	}
	

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float[] x = new float[points.length];
		float[] y =  new float[points.length];
		int i;
		for(i = 0; i <points.length; i++)
		{
			x[i] = points[i].x;
			y[i] = points[i].y;
		}
		float a = 0;
		float b = 0;
		a = cov(x,y)/var(x);
		b= avg(y)-a*avg(x);
		Line L = new Line(a,b);
		return L;
		
	}

	// returns the deviation between point p and the line equation of the point
	public static float dev(Point p,Point[] points){
		float deviation = 0;
		deviation = (p.y - linear_reg(points).f(p.x));
		if(deviation > 0)
			return deviation;
		else
			deviation = deviation * (-1);
		
		return deviation;
		
		
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		float deviation = 0;
		deviation = (p.y - l.f(p.x));
		if(deviation > 0)
			return deviation;
		else
			deviation = deviation * (-1);
		
		return deviation;
		
	}
	
}
