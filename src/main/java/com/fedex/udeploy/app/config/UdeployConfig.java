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

	private static final String DATACENTER = "Data Center";
	private static final String LEVELS = "Levels";
	private static final String APP = "App";
	private static final String COMPONENT = "component";
			
	
	private String team;
	private String appName;
	private String componentName;
	private String resourceGroup;
	private List<DataCenter> dataCenters;
	private final DataFormatter dataFormatter;
	
	@Value("${input-file}")
	private String inputFile;
	
	public UdeployConfig() {
		dataFormatter = new DataFormatter();
	} 
	
	@PostConstruct
	public void buildConfig() throws EncryptedDocumentException, IOException {
		final Workbook workbook = WorkbookFactory.create(new File(inputFile));
		final Sheet appSheet = workbook.getSheet(APP);
		final Sheet comSheet = workbook.getSheet(COMPONENT);
		final int rowCount = appSheet.getPhysicalNumberOfRows();
		
		System.out.println("ROW COUNT = " + rowCount);

		initSimpleFields(appSheet, comSheet);
		
		List<Integer> dcIndexes = findDCIndexes(appSheet, rowCount);
		
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
				if (!LEVELS.equalsIgnoreCase(levelName) && levelName.length() > 0) {
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
	
	public List<String> findLevels(DataCenter dc, Sheet appSheet, int cdci) {
		List<String> levelNames = new ArrayList<>();
		for (Cell level : appSheet.getRow(cdci + 1)) {
			String levelName = dataFormatter.formatCellValue(level).trim();
			if (!LEVELS.equalsIgnoreCase(levelName) && levelName.length() > 0) {
				dc.addLevel(levelName);
				levelNames.add(levelName);
			}
		}
		return levelNames;
	}
	
	private void initSimpleFields(Sheet appSheet, Sheet componentSheet) {
		appName = dataFormatter.formatCellValue(appSheet.getRow(0).getCell(1));
		resourceGroup = dataFormatter.formatCellValue(appSheet.getRow(5).getCell(1));
		team = dataFormatter.formatCellValue(appSheet.getRow(7).getCell(1));
		componentName = dataFormatter.formatCellValue(componentSheet.getRow(1).getCell(0));
		dataCenters = new ArrayList<>();
	}
	
	private void buildDCGraph(Sheet appSheet, int rowCount) {
		List<Integer> dcIndexes = findDCIndexes(appSheet, rowCount);
		
		
	}
	
	private int findDC(int fromRow, int toRow, Sheet sheet) {
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
				if(DATACENTER.equalsIgnoreCase(val.trim())) {
					return row.getRowNum();
				}
			}
		}
		return -1;
	}
	
	private List<Integer> findDCIndexes(Sheet appSheet, int rowCount) {
		int dcIndex = 0;
		List<Integer> dcIndexes = new ArrayList<>();
		while(dcIndex < rowCount && dcIndex >= 0) {
			dcIndex = findDC(dcIndex + 1, rowCount, appSheet);
			if(dcIndex > 0) {
				System.out.println("FOUND DC at ROW : " + (dcIndex + 1));
				dcIndexes.add(dcIndex);
			}
		}
		return dcIndexes;
	}
	
	

	private boolean isRowEmpty(Row row) {
		if(row != null) {
			for(Cell cell: row) {
				if(dataFormatter.formatCellValue(cell).trim().length() > 0) {
					return false;
				}
			}
		}
		return true;
	}

	public String toString() {
		return reflectionToString(this, JSON_STYLE);
	}
}
