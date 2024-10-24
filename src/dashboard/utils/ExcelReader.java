package dashboard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dashboard.panels.LineGraphPanel;

public class ExcelReader {

    // Private constructor to hide the implicit public one
    private ExcelReader() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void openExcelFile(LineGraphPanel lineGraphPanel) {
        
        JFileChooser fileChooser = new JFileChooser();
        
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
        
            File file = fileChooser.getSelectedFile();
            List<Integer> data = ExcelReader.readExcelData(file);
        
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No numeric data found in the Excel file", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                //lineGraphPanel.setData(data);
                lineGraphPanel.repaint();
            }
        }
    }

    public static List<Integer> readExcelData(File file) {

        List<Integer> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.NUMERIC) {
                        data.add((int) cell.getNumericCellValue());
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading Excel file", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return data;
    }
}

/* public class ExcelReader {

    // Map to store the data from the excel file with
    // column name as the key and the data as the value
    private static Map<String, Object[]> data = new TreeMap<>();

    // Private constructor to hide the implicit public one
    private ExcelReader() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // Get the data from the excel file
    public static Map<String, Object[]> getData() {
        return data;
    }

    // Set the data for the excel file
    public static void setData(Map<String, Object[]> data) {
        ExcelReader.data = data;
    }

    // Read an excel file and return the data
    // This should integrate with the JMenu button "Open File" to open the selected file
    public static void readExcelFile(String filePath) {
        
        // Try to read the file
        try (FileInputStream file = new FileInputStream(new File(filePath))) {
            // Create a workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            // Create a formula evaluator
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // Get the first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            // Iterate through each row and column to get the data
            Iterator<Row> rowIterator = sheet.iterator();
            
            // While there are rows to read
            while (rowIterator.hasNext()) {
                // Get the row object
                Row row = rowIterator.next();
                
                // Iterate through each cell
                Iterator<Cell> cellIterator = row.cellIterator();
                
                // While there are cells to read
                while (cellIterator.hasNext()) {
                    // Get the cell object    
                    Cell cell = cellIterator.next();
                    // Save the data in a list
                    switch (evaluator.evaluateInCell(cell).getCellType()) {
                        case NUMERIC:
                            data.put(cell.getAddress().toString(), new Object[]{cell.getNumericCellValue()});
                            break;
                        case STRING:
                            data.put(cell.getAddress().toString(), new Object[]{cell.getStringCellValue()});
                            break;
                        case BOOLEAN:
                            data.put(cell.getAddress().toString(), new Object[]{cell.getBooleanCellValue()});
                            break;
                        case FORMULA:
                            data.put(cell.getAddress().toString(), new Object[]{cell.getCellFormula()});
                            break;
                        default:
                            data.put(cell.getAddress().toString(), new Object[]{cell.getStringCellValue()});
                            break;
                    }
                }
            }
            // Close the workbook
            workbook.close();
            // Try with resources will automatically close the file
            // Set the data
            setData(data);

        } catch (Exception e) {
            
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading excel file", "Error", JOptionPane.ERROR_MESSAGE);
            setData(Collections.emptyMap());
        }
    }

    // Print the data from the excel file
    public static void printData() {
        // Print the data from the excel file
        for (Map.Entry<String, Object[]> entry : data.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue()[0]);
        }
    }
}
*/