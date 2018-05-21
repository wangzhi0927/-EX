package com.slzr.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.slzr.pojo.BusPrice;
import com.slzr.utils.JsonUtils;

public class PoiIoport {

	// 批量数据导入

	public static void main(String[] args) throws Exception {

		List<BusPrice> busPrice = new ArrayList<BusPrice>();

		String F = "C:\\aaa.xls";// Excel文件读取路径


		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(F));
		//Map<Integer, String> endPal = new HashMap<Integer, String>();// 存储抵达站信息
		List<Integer> endPal=new ArrayList<>();
	//	Map<Integer, String> startPal = new HashMap<Integer, String>();//储存终点站信息
		List<Integer> startPal=new ArrayList<>();
		int count = hssfWorkbook.getNumberOfSheets();

		for (int x = 0; x < count; x++) {// 读取每个sheet表

			HSSFSheet sheet = hssfWorkbook.getSheetAt(x);
			LinkedHashMap<String, Double> myMap = new LinkedHashMap<String, Double>();// 存储起止信息 和 票价信息
			LinkedHashMap<String, Double> myMap2 = new LinkedHashMap<String, Double>();// 存储起止信息 和 票价信息
			String sheetName = sheet.getSheetName();
			BusPrice busP = new BusPrice();// 公交票价信息

			String O = "C:\\a\\"; // 写入数据路径
			for (Row row : sheet) {// 读取sheet中每一行
				int lastRowNum = sheet.getLastRowNum();
				// 第一行数据路线编号和折扣
				if (row.getRowNum() == 0) {

					// 读取第一行的第二个数据
					row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
					busP.setPathNum(row.getCell(1).getStringCellValue());// 存入公交路线编号
					// 存入卡类和折扣率
					Map<String, Integer> cardType = new HashMap<String, Integer>();// 存储卡类与折扣率
					for (int i = 3; i < 35; i++) {
						if (row.getCell(i) != null && row.getCell(i).getStringCellValue() != "") {
							String discount = row.getCell(i).getStringCellValue();
							String[] dis = discount.split("-");// 对卡类和折扣进行分割
							cardType.put(dis[0], Integer.parseInt(dis[1]));
							// 将折扣存储
							busP.setDiscount(cardType);// 存入公交卡类和卡率折扣
						}
					}
				}
				// 读取公交的抵达站站信息
				short last = row.getLastCellNum();// 起点到终点的长度
				if (row.getRowNum() == 3) {
					// System.out.println(last);
					for (int i = 1; i < last; i++) {
						if (row.getCell(i).getStringCellValue() != "" && row.getCell(i) != null) {
							String startPalts = row.getCell(i).getStringCellValue();
							//String[] strings = startPalts.split("\\.");
							endPal.add(i-1);
							startPal.add(i-1);
						}
					}
				}
				if (row.getRowNum() < 4) {
					continue;
				}
				if (row.getCell(0).getStringCellValue().equals("结束")) {
					break;
				}

				// 存储起-末站点位置
				List<String> mykey = new ArrayList<String>();

				for(int i=0;i<startPal.size();i++){
					for(int j=0;j<endPal.size();j++){
						mykey.add(startPal.get(i)+"-"+endPal.get(j));
					}
				}

				// 得到一个起始站的值
				/*String startpal = row.getCell(0).getStringCellValue();
				String[] statrts = startpal.split("\\.");
				String start = statrts[0];*/


				// 得到每一个key(起始站-终点站)
				List<String> U=new ArrayList<>();
				List<String> D=new ArrayList<>();
				/*for (Integer end : endPal.keySet()) {
					int iStart = end;
					int iEnd = end;
					String key="";
				//	if(iStart<=iEnd) {
						key =start + "-" + end;
						mykey.add(key);
				//	}
					*//*if(iStart>=iEnd){
						key ="D"+ (last-iStart-2) + "-" + (last-iEnd-2);
						mykey.add(key);
					}*//*
				}*/



				// 添加票价
				List<Double> pricess = new ArrayList<Double>();
				for (int i = 1; i < last; i++) {// 插到集合的索引位置为1 3 5
					row.getCell(i).setCellType(Cell.CELL_TYPE_NUMERIC);
					double numeric = row.getCell(i).getNumericCellValue();
					pricess.add(numeric);
				}

				for (int j = 0; j < pricess.size(); j++) {
					String add = mykey.get(j);// 起始站-终点站
					Double pr = pricess.get(j);
					// myMap.clear();
					if (!myMap.containsKey(add)) {
						myMap.put(add, pr);
					}
				}
				/**
				 * 在 起止站-票价  设置 上行- 下行
				 */

				// 将用户 起始站-终点站 票价信息存储到对象中
				busP.setPrices(myMap);


			}//这是读取一个sheet表的
			String jsonResult = null;
			File file;// 创建文件夹
			FileOutputStream stream = null;// 创建文件流
			jsonResult = JsonUtils.objectToJson(busP);
			try {
				file = new File(O+sheetName+".txt");
				stream = new FileOutputStream(file, true);// 将文件夹放在文件流中
				if (!file.exists()) {
					file.createNewFile();
				}
				byte[] contentInBytes = jsonResult.getBytes();// 转化成字节
				stream.write(contentInBytes);// 写入
				stream.flush();
				stream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}


		}

	}
}
