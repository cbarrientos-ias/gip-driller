import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by barcode on 4/11/15.
 */
public class ETLExcel {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("file-last.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheet("data");
            Row nextRow = worksheet.getRow(1);
            String currentLocation = null;
            List<ExcelRow> OpenQuestions = new ArrayList();
            List<ExcelRow> CloseQuestions = new ArrayList();
            List<String> valuesArray = new ArrayList<String>();
            HashMap<String, List<ExcelRow>> sheets = new LinkedHashMap<String, List<ExcelRow>>();
            String CurrentQuestion = "";
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            while (nextRow != null) {
                ExcelRow excelRow = new ExcelRow(
                        nextRow.getCell((short) 0).getStringCellValue(),
                        nextRow.getCell((short) 1).getStringCellValue(),
                        nextRow.getCell((short) 4).getStringCellValue(),
                        nextRow.getCell((short) 5).getStringCellValue(),
                        String.valueOf(nextRow.getCell((short) 3).getNumericCellValue()),
                        (int) nextRow.getCell((short) 6).getNumericCellValue(),
                        df.parse(nextRow.getCell((short) 2).getStringCellValue()),
                        nextRow.getCell((short) 7).getStringCellValue(),
                        Double.parseDouble(nextRow.getCell((short) 8).getStringCellValue())
                );

                String type = excelRow.getQuestion_type();

                if (currentLocation == null || !currentLocation.equals(excelRow.getLocation())) {
                    currentLocation = excelRow.getLocation();
                    CloseQuestions = new ArrayList<ExcelRow>();
                    OpenQuestions = new ArrayList<ExcelRow>();
                    sheets.put(excelRow.getLocation() + "_abiertas", OpenQuestions);
                    sheets.put(excelRow.getLocation() + "_cerradas", CloseQuestions);
                }

                if (isCloseQuestion(type)) {
                    CloseQuestions.add(excelRow);
                } else {
                    OpenQuestions.add(excelRow);
                }

                nextRow = worksheet.getRow(nextRow.getRowNum() + 1);
            }

            writeSheets(sheets);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void writeSheets(HashMap<String, List<ExcelRow>> sheets) throws IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("m/d/yy"));
        Iterator it = sheets.entrySet().iterator();
        DecimalFormat dcf = new DecimalFormat("#.##");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Sheet sheet = wb.createSheet((String) pair.getKey());
            Row rh = sheet.createRow((short) 0);
            rh.createCell((short) 0).setCellValue("Componente");
            rh.createCell((short) 1).setCellValue("Ubicación");
            rh.createCell((short) 2).setCellValue("Orden pregunta");
            rh.createCell((short) 3).setCellValue("Pregunta");
            rh.createCell((short) 4).setCellValue("Respuesta");
            rh.createCell((short) 5).setCellValue("Frecuencia");
            rh.createCell((short) 6).setCellValue("Relación");
            int index = 0;
            for (ExcelRow excelRow : (List<ExcelRow>) pair.getValue()) {
                if (excelRow.getFrequency() == null) {
                    continue;
                }
                Row rw = sheet.createRow((short) ++index);
                rw.createCell((short) 0).setCellValue(excelRow.getSection());
                rw.createCell((short) 1).setCellValue(excelRow.getLocation());
                rw.createCell((short) 2).setCellValue((int) Double.parseDouble(excelRow.getQuestionOrder()));
                rw.createCell((short) 3).setCellValue(excelRow.getQuestion());
                rw.createCell((short) 4).setCellValue(excelRow.getAnswer());
                rw.createCell((short) 5).setCellValue(excelRow.getFrequency() == null ? 0 : excelRow.getFrequency());
                rw.createCell((short) 6).setCellValue(dcf.format(excelRow.getRelation() == null ? 0 : excelRow.getRelation()) + " %");
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        FileOutputStream fileOut = new FileOutputStream("resultTotal.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void WriteRows(List<ExcelRow> closeQuestions, List<ExcelRow> openQuestions, String location, HashMap<String, List<ExcelRow>> sheets) {

        sheets.put(location + "_abiertas", openQuestions);
        sheets.put(location + "_cerradas", closeQuestions);

    }

    public static Boolean isCloseQuestion(String type) {
        return (type.equals("multiChoice")) || (type.equals("multiOptionSingleChoice")) || (type.equals("multiOptionSingleChoiceWithOther")) || (type.equals("multiChoiceWithOther")) || (type.equals("yesno"));
    }
}
