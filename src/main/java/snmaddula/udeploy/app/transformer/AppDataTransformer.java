package snmaddula.udeploy.app.transformer;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

@Service
public class AppDataTransformer {

	public void it(InputStream in) throws Exception {

		Workbook workbook = WorkbookFactory.create(in);
		DataFormatter dataFormatter = new DataFormatter();
        workbook.forEach(sheet -> {
            System.out.println("SHEET: " + sheet.getSheetName());
            sheet.forEach(row -> {
                row.forEach(cell -> {
                    String cellVal = dataFormatter.formatCellValue(cell);
					System.out.print(cellVal + "\t");
                });
                System.out.println();
            });
        });
	}
}
