package application;
/**
 * �P��̃f�[�^�ɂ��āA���ρA���U�A�W���΍��A�l���ʁA����ێ�����
 * ���߂̕⏕�N���X
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
		str += "��t���ρF";
		return str;
	}
}
