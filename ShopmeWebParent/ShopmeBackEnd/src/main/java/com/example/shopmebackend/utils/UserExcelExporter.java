package com.example.shopmebackend.utils;

import com.example.shopmecommon.entity.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExport {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "User id", cellStyle);
        createCell(row, 1, "Email", cellStyle);
        createCell(row, 2, "First Name", cellStyle);
        createCell(row, 3, "Last Name", cellStyle);
        createCell(row, 4, "Roles", cellStyle);
        createCell(row, 5, "Enabled", cellStyle);
    }
    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        if(value instanceof Long) {
            cell.setCellValue((Long)value);
        }else if(value instanceof Boolean) {
            cell.setCellValue((Boolean)value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
        private void writeDataLine(List<User> listUsers) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        XSSFFont font = this.workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for(User user : listUsers) {
            XSSFRow row = this.sheet.createRow(rowIndex++);
            int columnIndex = 0;
            createCell(row, columnIndex++, user.getId(), cellStyle);
            createCell(row, columnIndex++, user.getEmail(), cellStyle);
            createCell(row, columnIndex++, user.getFirstName(), cellStyle);
            createCell(row, columnIndex++, user.getLastName(), cellStyle);
            createCell(row, columnIndex++, user.getListRoles(), cellStyle);
            createCell(row, columnIndex++, user.isEnabled(), cellStyle);
        }
    }
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setAtributeHeader(response, ".xlsx", "application/octet-stream");

        writeHeaderLine();

        writeDataLine(listUsers);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
