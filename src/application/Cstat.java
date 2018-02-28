package application;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * �P��̃f�[�^�ɂ��āA���ρA���U�A�W���΍��A�l���ʁA����ێ����� ���߂̕⏕�N���X
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
		str += "\t���ρF" + ave.toString() + "\t���U�F" + dev.toString() + "\t�W���΍�" + stdDev.toString();
		str += "\t�l����";
		int index =1;
		for (BigDecimal b : quartile) {
			str += "\tQ(" +index+"): "+ b.toString();
			index++;
		}
		str += "\n";
		return str;
	}
	//�S���v�Z
	public void calcData(ArrayList<Integer> d) {
		this.ave = ave(d);
		this.dev = dev(d);
		this.stdDev = stdDev();
		quartile(d);
	}
	

	// �v�Z���\�b�h
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
			//����
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
			// n�Ԗ�
			BigDecimal nth = new BigDecimal(list.get(half));
			// n+1�Ԗ�
			BigDecimal n1th = new BigDecimal(list.get(half -1));
			r = nth.add(n1th).divide(new BigDecimal(2), mc);
		} else {
			int half = (list.size() - 1) / 2;
			// n�Ԗ�
			r = new BigDecimal(list.get(half));
		}
		return r;
	}
}
