package application;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 単一のデータについて、平均、分散、標準偏差、四分位、個数を保持する ための補助クラス
 * 
 * @author yasuda
 *
 */
public class Cstat {
	MathContext mc;
	int num;
	String name;
	BigDecimal ave, dev, stdDev;
	BigDecimal[] quartile;

	//
	public Cstat(String s) {
		mc = new MathContext(4);
		this.name = new String(s);
		num = 0;
		ave = new BigDecimal(0);
		dev = new BigDecimal(0);
		stdDev = new BigDecimal(0);
		quartile = new BigDecimal[3];
		for (int i = 0; i < quartile.length; i++) {
			quartile[i] = new BigDecimal(0);
		}
	}

	//
	public String getData() {
		String str;
		str = new String(this.name);
		str += "\t平均：" + ave.toString() + "\t分散：" + dev.toString() + "\t標準偏差" + stdDev.toString();
		str += "\t四分位";
		int index =1;
		for (BigDecimal b : quartile) {
			str += "\tQ(" +index+"): "+ b.toString();
			index++;
		}
		str += "\n";
		return str;
	}
	//全部計算
	public void calcData(ArrayList<Integer> d) {
		this.ave = ave(d);
		this.dev = dev(d);
		this.stdDev = stdDev();
		quartile(d);
	}
	

	// 計算メソッド
	public BigDecimal ave(ArrayList<Integer> data) {
		BigDecimal a = new BigDecimal(0);
		BigDecimal sum = new BigDecimal(0);
		for (Integer n : data) {
			BigDecimal add = new BigDecimal(n.intValue());
			sum = sum.add(add);
		}
		a = sum.divide(new BigDecimal(data.size()), mc);
		return a;
	}
	
	//
	public BigDecimal dev(List<Integer> data) {
		BigDecimal sum = new BigDecimal(0);
		BigDecimal a = this.ave;
		for (Integer n : data) {
			BigDecimal v = new BigDecimal(n.intValue());
			BigDecimal arg = v.subtract(a);
			sum = sum.add(arg.pow(2));
		}
		dev = sum.divide(new BigDecimal(data.size()), mc);
		return dev;
	}

	//
	public BigDecimal stdDev() {
		return this.dev.sqrt(mc);
	}

	//
	public void quartile(ArrayList<Integer> data) {
		Collections.sort(data);
		int n = data.size();
		ArrayList<Integer> firstHalf = new ArrayList<Integer>();
		ArrayList<Integer> secondHalf = new ArrayList<Integer>();
		if(n%2==0) {
			int half = n/2;
			//半分
			for(int i=0;i<half;i++) {
				firstHalf.add(data.get(i));
				secondHalf.add(data.get(half+i));
			}
		}else {
			int half =(n-1)/2;
			for(int i=0;i<half;i++) {
				firstHalf.add(data.get(i));
				secondHalf.add(data.get(half+i+1));
			}
		}
		//
		quartile[0] = median(firstHalf);
		quartile[1] = median(data);
		quartile[2] = median(secondHalf);
	}

	//
	public BigDecimal median(ArrayList<Integer> list) {
		BigDecimal r = new BigDecimal(0);
		Collections.sort(list);
		int n = list.size();
		if (n % 2 == 0) {
			int half = n / 2;
			// n番目
			BigDecimal nth = new BigDecimal(list.get(half));
			// n+1番目
			BigDecimal n1th = new BigDecimal(list.get(half -1));
			r = nth.add(n1th).divide(new BigDecimal(2), mc);
		} else {
			int half = (list.size() - 1) / 2;
			// n番目
			r = new BigDecimal(list.get(half));
		}
		return r;
	}
}
