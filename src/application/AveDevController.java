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
 * 数値のみからなるアンケート回答のすべてのフィールドについて
 * 排除文字を処理したうえで平均と中央値、四分位、分散と標準偏差を
 * つくるだけのツール
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
		fc.setTitle("アンケートファイルを指定してください");
		file = fc.showOpenDialog(log.getScene().getWindow()).getAbsoluteFile();
		if (file == null) {
			showAlert("アンケートファイルを選択してください");
			return;
		} else {
			fileSetFlag = true;
			filePath = file.getParent();
		}
		log.appendText("アンケートファイルに" + file.getAbsolutePath() + "がセットされました。\n");
		// フィールド名を保存
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
		if(filePath==null) {
			showAlert("ファイルが選択されていませんよ。");
			return;
		}
		for (int i = 0; i < fieldNameArray.length; i++) {
			StringList col = new StringList(); // 「たて」方向データ
			for (String s : recordList) {// 「よこ」方向データ
				String[] tmpStr = s.split(","); // 1行ずついったんバラす
				col.add(tmpStr[i]);
			}
			dataList.add(col);// 「たて」に並んだ列が、横並びになったイメージ
		} // end of for(int i=0...

		// データ処理は別メソッド
		calcStat();

	}// end of execArray()

	private void calcStat() {
		// dataList リストを用いて各列の平均などを計算し、表示
		// TextField から除外番号を読み取る
		String[] eliminate = eliminateField.getText().split(",");
		// log.appendText("\neliminate:");
		// for(String s:eliminate) {
		// log.appendText(s+"\t");
		// }
		// 本当は List 全部やる
		// for check
		String theField = combo.getValue();
		int hit = hitNumber(fieldNameArray, theField);
		String theFieldName = fieldNameArray[hit];
		// log.appendText("\n" + fieldNameArray[hit] + "\n");
		ArrayList<Integer> theColData = new ArrayList<Integer>();
		for (String s : dataList.get(hit)) {
			// log.appendText("\n"+s);
			boolean eliminateFlag = false;
			for (String e : eliminate) {
				if (e.equals(s))
					eliminateFlag = true;
			}
			if (s.isEmpty() || s.equals("")) {
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
			if (!eliminateFlag) {
				v = Integer.parseInt(s);
				theColData.add(v);
			}
		} // end of for(String s:dataList.get(hit)...
			// for(Integer n: theColData) {
			// log.appendText("\n"+n.intValue());
			// }
			// 以上で排除文字を処理した上で IntegerList にデータがはいった。
			// 計算のためにCstatクラスを作る
		if (theColData.size() != 0) {
			Cstat theStat = new Cstat(theFieldName);
			theStat.calcData(theColData);
			log.appendText(theStat.getData());
		}

	}// end of calcStat()

	@FXML
	private void saveAction() {
		if(filePath==null) {
			showAlert("ファイルが選択されていませんよ。");
			return;
		}
		for (int i = 0; i < fieldNameArray.length; i++) {
			StringList col = new StringList(); // 「たて」方向データ
			for (String s : recordList) {// 「よこ」方向データ
				String[] tmpStr = s.split(","); // 1行ずついったんバラす
				col.add(tmpStr[i]);
			}
			dataList.add(col);// 「たて」に並んだ列が、横並びになったイメージ
		} // end of for(int i=0...
			// TextField から除外番号を読み取る
		String[] eliminate = eliminateField.getText().split(",");
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(filePath));
		File saveFile = fc.showSaveDialog(log.getScene().getWindow());
		String sysEncode = System.getProperty("file.encoding");
		PrintWriter ps;
		try {
			ps = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), sysEncode)));
			// ファイル情報
			ps.println("ファイル名：," + file.getAbsolutePath());
			// すべてのフィールドについて処理を行う
			int index = 0;
			for (String theField : fieldNameArray) {
				ArrayList<Integer> theColData = new ArrayList<Integer>();
				//log.appendText(index + "\n");
				for (String s : dataList.get(index)) {
					// log.appendText("\n"+s);
					boolean eliminateFlag = false;
					for (String e : eliminate) {
						if (e.equals(s))
							eliminateFlag = true;
					}
					if (s.isEmpty() || s.equals("")) {
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
					if (!eliminateFlag) {
						v = Integer.parseInt(s);
						theColData.add(v);
					}
				} // end of for(String s:dataList.get(index)...
				if (theColData.size() != 0) {
					Cstat theStat = new Cstat(theField);
					theStat.calcData(theColData);
					log.appendText(theStat.getData());
					ps.print(theStat.getData());
				}
				index++;
			} // end of for(String theField
			ps.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} // end of saveAction()

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

	// アラート
	private void showAlert(String str) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("ファイルを選択してください");
		alert.getDialogPane().setContentText(str);
		alert.showAndWait(); // 表示
	}

	// String[] からkeyと一致する場所を返す
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
