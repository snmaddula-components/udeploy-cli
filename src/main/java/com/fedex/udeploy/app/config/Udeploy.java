package com.fedex.udeploy.app.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fedex.udeploy.app.domain.DataCenter;

import lombok.Getter;

@Getter
@Component
public class Udeploy {

	private static final String DATACENTER = "Data Center";
	private static final String LEVELS = "Levels";
	private static final String APP = "App";
	private static final String COMPONENT = "component";

	private String team;
	private String appName;
	private String componentName;
	private String resourceGroup;
	private List<DataCenter> dataCenters;

	@JsonIgnore
	private final DataFormatter dataFormatter;

	@Value("${input-file}")
	private String inputFile;

	public Udeploy() {
		dataFormatter = new DataFormatter();
	}

	@PostConstruct
	public void buildConfig() throws EncryptedDocumentException, IOException {
		final Workbook workbook = WorkbookFactory.create(new File(inputFile));
		final Sheet appSheet = workbook.getSheet(APP);
		final Sheet comSheet = workbook.getSheet(COMPONENT);
		final int rowCount = appSheet.getPhysicalNumberOfRows();

		initSimpleFields(appSheet, comSheet);

		buildDCGraph(appSheet, rowCount);
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
		if (!dcIndexes.isEmpty()) {
			dataCenters = new ArrayList<>(dcIndexes.size());
		}
		for (int i = 0; i < dcIndexes.size(); i++) {
			int cdci = dcIndexes.get(i);
			int ndci = (i + 1) < dcIndexes.size() ? dcIndexes.get(i + 1) : rowCount;
			final String dcName = dataFormatter.formatCellValue(appSheet.getRow(cdci).getCell(1)).trim();
			DataCenter dc = new DataCenter(dcName);
			List<String> levelNames = findLevels(dc, appSheet, cdci);
			addAgentsToDCLevels(appSheet, levelNames, dc, cdci, ndci);
			dataCenters.add(dc);
		}
	}

	private List<Integer> findDCIndexes(Sheet appSheet, int rowCount) {
		int dcIndex = 0;
		List<Integer> dcIndexes = new ArrayList<>();
		while (dcIndex < rowCount && dcIndex >= 0) {
			dcIndex = findDCIndex(dcIndex + 1, rowCount, appSheet, rowCount);
			if (dcIndex > 0) {
				System.out.println("FOUND DC in ROW : " + (dcIndex + 1));
				dcIndexes.add(dcIndex);
			}
		}
		return dcIndexes;
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

	private void addAgentsToDCLevels(Sheet appSheet, List<String> levelNames, DataCenter dc, int cdci, int ndci) {
		for (int j = cdci + 2; j < ndci; j++) {
			Row row = appSheet.getRow(j);
			if (row != null) {
				for (int k = 1; k <= levelNames.size(); k++) {
					String agentName = dataFormatter.formatCellValue(row.getCell(k)).trim();
					if (agentName.length() > 0) {
						dc.addAgent(levelNames.get(k - 1), agentName);
					}
				}
			}
		}
	}

	private int findDCIndex(int fromRow, int toRow, Sheet appShheet, int rowCount) {
		for (int i = fromRow; i < rowCount; i++) {
			Row row = appShheet.getRow(i);
			if (row != null && !isRowEmpty(row)) {
				for (Cell cell : row) {
					if (DATACENTER.equalsIgnoreCase(dataFormatter.formatCellValue(cell).trim())) {
						return row.getRowNum();
					}
				}
			}
		}
		return -1;
	}

	private boolean isRowEmpty(Row row) {
		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					return false;
				}
			}
		}
		return true;
	}
}
