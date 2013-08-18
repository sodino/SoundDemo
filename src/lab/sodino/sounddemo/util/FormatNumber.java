package lab.sodino.sounddemo.util;

import java.text.DecimalFormat;

public class FormatNumber {
	public static DecimalFormat df = new DecimalFormat("0.00%");
	public static String formatProgresss(int all,int current){
		return df.format(1.0d*current / all);
	}
}
