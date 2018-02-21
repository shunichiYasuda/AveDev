package application;
/**
 * 単一のデータについて、平均、分散、標準偏差、四分位、個数を保持する
 * ための補助クラス
 * @author yasuda
 *
 */
public class Cstat {
	int num;
	String name;
	double ave,dev,stdDev;
	double[] quartile;
	//
	public Cstat(String s) {
		this.name = new String(s);
		num = 0;
		ave = dev = stdDev = 0.0;
		quartile = new double[4];
	}
	//
	public String getData() {
		String str;
		str = new String(this.name);
		str += "￥t平均：";
		return str;
	}
}
