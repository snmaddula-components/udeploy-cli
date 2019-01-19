package com.fedex.udeploy.app.config;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedex.udeploy.app.domain.DataCenter;

import lombok.Getter;

@Getter
@Component
public class UdeployConfig {

	private String team;
	private String appName;
	private String componentName;
	private String resourceGroup;
	private List<DataCenter> dataCenters;
	
	@Value("${input-file}")
	private String inputFile;
	
	@PostConstruct
	public void buildConfig() throws EncryptedDocumentException, IOException {
		Workbook workbook = WorkbookFactory.create(new File(inputFile));
		
		Sheet appSheet = workbook.getSheet("App");
		Sheet comSheet = workbook.getSheet("component");
		//parseAppSheet(appSheet);
		
		int rowCount = appSheet.getPhysicalNumberOfRows();
		System.out.println("ROW COUNT = " + rowCount);
		List<Integer> dcIndexes = new ArrayList<>();
		int dcIndex = 0;
		while(dcIndex < rowCount && dcIndex >= 0) {
			dcIndex = findRow(dcIndex + 1, rowCount, appSheet, "Data Center");
			if(dcIndex > 0) {
				System.out.println("FOUND DC at ROW : " + (dcIndex + 1));
				dcIndexes.add(dcIndex);
			}
		}
		
		for(int i = 0; i < dcIndexes.size(); i++) {
			int cdci = dcIndexes.get(i);
			int ndci = (i+1) < dcIndexes.size() ? dcIndexes.get(i+1) : rowCount;
			System.out.println("CURR DC : " + cdci);
			System.out.println("NEXT DC : " + ndci);
			String dcName = dataFormatter.formatCellValue(appSheet.getRow(cdci).getCell(1)).trim();
			System.out.println("DC NAME = " + dcName);
			DataCenter dc = new DataCenter(dcName);
			List<String> levelNames = new ArrayList<>();
			int levelCount = 0;
			
			for (Cell level : appSheet.getRow(cdci + 1)) {
				String levelName = dataFormatter.formatCellValue(level).trim();
				if (!"Levels".equalsIgnoreCase(levelName) && levelName.length() > 0) {
					levelCount++;
					dc.addLevel(levelName);
					levelNames.add(levelName);
				}
			}
			System.out.println("LEVEL COUNT = " + levelCount);
			System.out.println(dc);
			
			for(int j=cdci + 2; j < ndci; j++) {
				Row crow = appSheet.getRow(j); 
				if(crow !=null) {
					for (int k = 1; k <= levelCount; k++) {
						String agentName = dataFormatter.formatCellValue(crow.getCell(k)).trim();
						if(agentName.length() > 0) {
							dc.addAgent(levelNames.get(k-1), agentName);
							System.out.println("AGENT NAME = " + agentName);
						}
					}
				}
			}
			try {
				System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dc));
			}catch(Exception ex) {
				
			}
		}
		
		
		
	}
	
	private int findRow(int fromRow, int toRow, Sheet sheet, String cellContent) {
		System.out.println("FROM ROW = " + fromRow + " TO ROW = " + toRow);
		int rowCount = sheet.getPhysicalNumberOfRows();
		for(int i=fromRow; i< rowCount; i++) {
			Row row = sheet.getRow(i);
			if(row == null || isRowEmpty(row)) {
				System.out.println("ROW " + (i + 1) + " is NULL / EMPTY");
				continue;
			}
			for(Cell cell : row) {
				String val = dataFormatter.formatCellValue(cell);
				if(cellContent.equalsIgnoreCase(val.trim())) {
					return row.getRowNum();
				}
			}
		}
		return -1;
	}
	
	static DataFormatter dataFormatter = new DataFormatter();
	
	private void parseAppSheet(Sheet appSheet) {
		
	}
	
	private void _parseAppSheet(Workbook workbook) {
		System.out.println("PARSING [ 'App' ] SHEET");
		Sheet appSheet = workbook.getSheet("App");
		Sheet comSheet = workbook.getSheet("component");
		int rowCount = appSheet.getPhysicalNumberOfRows();
		System.out.println("ROW COUNT = " + rowCount);

		initSimpleFields(appSheet, comSheet);

		String dcName = dataFormatter.formatCellValue(appSheet.getRow(9).getCell(1));
		System.out.println("DC NAME = " + dcName);
		DataCenter dc = new DataCenter(dcName);
		int dcRowIndex = 10;
		
		for(int i=9; i<100; i++) {
			Row row = appSheet.getRow(i);
			if(row == null || isRowEmpty(row)) continue;
			int cellCount = row.getPhysicalNumberOfCells();
			String val = row.getCell(0).getStringCellValue();
			if("Data Center".equalsIgnoreCase(val)) {
				getDataCenters().add(dc);
				dcName = dataFormatter.formatCellValue(row.getCell(1));
				dc = new DataCenter(dcName);
			} else if("Levels".equalsIgnoreCase(val)) {
				row.forEach(cell -> {
					String level = dataFormatter.formatCellValue(cell);
					if(! level.equals("Levels") && !level.trim().isEmpty()) {
						dataCenters.get(dataCenters.size() - 1).addLevel(level);
					}
				});
			}
		}
		
	}
	
	private void initSimpleFields(Sheet appSheet, Sheet componentSheet) {
		appName = dataFormatter.formatCellValue(appSheet.getRow(0).getCell(1));
		resourceGroup = dataFormatter.formatCellValue(appSheet.getRow(5).getCell(1));
		team = dataFormatter.formatCellValue(appSheet.getRow(7).getCell(1));
		componentName = dataFormatter.formatCellValue(componentSheet.getRow(1).getCell(0));
		dataCenters = new ArrayList<>();
	}

	public static boolean isRowEmpty(Row row) {
		DataFormatter dataFormatter = new DataFormatter();
		if(row != null) {
			for(Cell cell: row) {
				if(dataFormatter.formatCellValue(cell).trim().length() > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void parseComponentSheet() {
		
	}

	public String toString() {
		return reflectionToString(this, JSON_STYLE);
	}
}
