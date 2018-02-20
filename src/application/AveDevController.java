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
import javafx.stage.FileChooser;

public class AveDevController {
	File file,saveFile,saveLogFile;
	String sysEncode, filePath;
	boolean fileSetFlag;
	String[] fieldNameArray;
	String fieldNameRecord;
	List<String> recordList= new ArrayList<String>();
	@FXML
	TextArea log;
	@FXML
	TextField eliminateField;

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
		log.appendText("アンケートファイルに" + file.getAbsolutePath() + "がセットされました。");
		//フィールド名を保存
		String line = null;
		BufferedReader br;
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), "JISAutoDetect"));
			line = br.readLine();
			fieldNameArray = line.split(",");
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

	}

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

	// アラート
	private void showAlert(String str) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("ファイルを選択してください");
		alert.getDialogPane().setContentText(str);
		alert.showAndWait(); // 表示
	}
}
