package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Feb 20 2018
 * ���l�݂̂���Ȃ�A���P�[�g�񓚂̂��ׂẴt�B�[���h�ɂ���
 * �r���������������������ŕ��ςƒ����l�A�l���ʁA���U�ƕW���΍���
 * ���邾���̃c�[��
 */
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

public class AveDevController {
	File file, saveFile, saveLogFile;
	String sysEncode, filePath;
	boolean fileSetFlag;
	String[] fieldNameArray;
	String fieldNameRecord;
	List<StringList> dataList = new ArrayList<StringList>();
	List<String> recordList = new ArrayList<String>();
	@FXML
	TextArea log;
	@FXML
	TextField eliminateField;
	@FXML
	ComboBox<String> combo;

	@FXML
	private void openAction() {
		sysEncode = System.getProperty("file.encoding");
		FileChooser fc = new FileChooser();
		if (filePath != null) {
			fc.setInitialDirectory(new File(filePath));
		}
		fc.setTitle("�A���P�[�g�t�@�C�����w�肵�Ă�������");
		file = fc.showOpenDialog(log.getScene().getWindow()).getAbsoluteFile();
		if (file == null) {
			showAlert("�A���P�[�g�t�@�C����I�����Ă�������");
			return;
		} else {
			fileSetFlag = true;
			filePath = file.getParent();
		}
		log.appendText("�A���P�[�g�t�@�C����" + file.getAbsolutePath() + "���Z�b�g����܂����B");
		// �t�B�[���h����ۑ�
		String line = null;
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "JISAutoDetect"));
			line = br.readLine();
			fieldNameArray = line.split(",");
			for (String s : fieldNameArray) {
				combo.getItems().add(s);
			}
			fieldNameRecord = line;
			while ((line = br.readLine()) != null) {
				recordList.add(line);
			}
			//
			br.close();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	private void execAction() {
		for (int i = 0; i < fieldNameArray.length; i++) {
			StringList col = new StringList(); // �u���āv�����f�[�^
			for (String s : recordList) {// �u�悱�v�����f�[�^
				String[] tmpStr = s.split(","); // 1�s����������o����
				col.add(tmpStr[i]);
			}
			dataList.add(col);// �u���āv�ɕ��񂾗񂪁A�����тɂȂ����C���[�W
		} // end of for(int i=0...

		// �f�[�^�����͕ʃ��\�b�h
		calcStat();

	}// end of execArray()

	private void calcStat() {
		// dataList ���X�g��p���Ċe��̕��ςȂǂ��v�Z���A�\��
		// TextField ���珜�O�ԍ���ǂݎ��
		String[] eliminate = eliminateField.getText().split(",");
		// �{���� List �S�����
		// for check
		String theField = combo.getValue();
		int hit = hitNumber(fieldNameArray, theField);
		String theFieldName = fieldNameArray[hit];
		log.appendText("\n" + fieldNameArray[hit] + "\n");
		ArrayList<Integer> theColData = new ArrayList<Integer>();
		for (String s : dataList.get(hit)) {
			log.appendText(s+"\n");
			boolean eliminateFlag = false;
			for (String e : eliminate) {
				if (e.equals(s))
					eliminateFlag = true;
			}
			//
			int v;
			try {
				v = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				eliminateFlag = true;
			}
			//
			if(!eliminateFlag) {
				v = Integer.parseInt(s);
				theColData.add(v);
			}
		} // end of for(String s:dataList.get(hit)...
		//�ȏ�Ŕr������������������� IntegerList �Ƀf�[�^���͂������B
		//�v�Z�̂��߂�Cstat�N���X�����
		Cstat theStat = new Cstat(theFieldName);
		theStat.ave(theColData);
		log.appendText(theStat.getData());

	}// end of calcStat()

	@FXML
	private void saveAction() {

	}

	@FXML
	private void saveLogAction() {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(filePath));
		saveLogFile = fc.showSaveDialog(log.getScene().getWindow());
		try {
			PrintWriter ps = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveLogFile), sysEncode)));
			ps.print(log.getText());
			ps.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void quitAction() {
		System.exit(0);
	}

	// �A���[�g
	private void showAlert(String str) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("�t�@�C����I�����Ă�������");
		alert.getDialogPane().setContentText(str);
		alert.showAndWait(); // �\��
	}

	// String[] ����key�ƈ�v����ꏊ��Ԃ�
	private int hitNumber(String[] array, String key) {
		int r = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(key)) {
				r = i;
			}
		}
		return r;
	}
}
