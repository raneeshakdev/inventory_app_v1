package com.svym.inventory.service.common;

import com.svym.inventory.service.dto.MedicineDailyCostSummaryDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for exporting data to Excel format
 */
@Service
public class ExcelExportService {

    private static final String[] HEADERS = {
        "Location", "Delivery Center", "Date", "Medicine Name", "Units", "Total Price"
    };

    /**
     * Export medicine daily cost summary data to Excel with merged cells for same Location, Delivery Center, and Date
     */
    public byte[] exportMedicineDailyCostSummaryToExcel(List<MedicineDailyCostSummaryDTO> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Medicine Daily Cost Summary");

            // Create header style - light blue background with thin border
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create data style with thin border
            CellStyle dataStyle = createDataStyle(workbook);

            // Create footer style - bold font with thin border
            CellStyle footerStyle = createFooterStyle(workbook);

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            // Sort and group data for merging
            List<MedicineDailyCostSummaryDTO> sortedData = data.stream()
                .sorted(Comparator
                    .comparing(MedicineDailyCostSummaryDTO::getLocationName, Comparator.nullsLast(String::compareTo))
                    .thenComparing(MedicineDailyCostSummaryDTO::getDeliveryCenterName, Comparator.nullsLast(String::compareTo))
                    .thenComparing(MedicineDailyCostSummaryDTO::getDistDate, Comparator.nullsLast(LocalDate::compareTo))
                    .thenComparing(MedicineDailyCostSummaryDTO::getMedicineName, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());

            // Populate data rows
            int rowIndex = 1;
            String currentLocation = null;
            String currentDeliveryCenter = null;
            LocalDate currentDate = null;
            int locationStartRow = -1;
            int deliveryCenterStartRow = -1;
            int dateStartRow = -1;

            for (MedicineDailyCostSummaryDTO item : sortedData) {
                Row row = sheet.createRow(rowIndex);

                // Location
                String location = item.getLocationName() != null ? item.getLocationName() : "";
                if (!location.equals(currentLocation)) {
                    if (currentLocation != null && locationStartRow != rowIndex - 1) {
                        // Merge previous location cells
                        sheet.addMergedRegion(new CellRangeAddress(locationStartRow, rowIndex - 1, 0, 0));
                    }
                    currentLocation = location;
                    locationStartRow = rowIndex;
                }
                createCell(row, 0, location, dataStyle);

                // Delivery Center
                String deliveryCenter = item.getDeliveryCenterName() != null ? item.getDeliveryCenterName() : "";
                if (!deliveryCenter.equals(currentDeliveryCenter) || !location.equals(currentLocation)) {
                    if (currentDeliveryCenter != null && deliveryCenterStartRow != rowIndex - 1 &&
                        currentLocation != null && currentLocation.equals(location)) {
                        // Merge previous delivery center cells
                        sheet.addMergedRegion(new CellRangeAddress(deliveryCenterStartRow, rowIndex - 1, 1, 1));
                    }
                    currentDeliveryCenter = deliveryCenter;
                    deliveryCenterStartRow = rowIndex;
                }
                createCell(row, 1, deliveryCenter, dataStyle);

                // Date
                String dateStr = item.getDistDate() != null ?
                    item.getDistDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
                LocalDate itemDate = item.getDistDate();
                if (!Objects.equals(itemDate, currentDate) ||
                    !deliveryCenter.equals(currentDeliveryCenter) ||
                    !location.equals(currentLocation)) {
                    if (currentDate != null && dateStartRow != rowIndex - 1 &&
                        currentDeliveryCenter != null && currentDeliveryCenter.equals(deliveryCenter) &&
                        currentLocation != null && currentLocation.equals(location)) {
                        // Merge previous date cells
                        sheet.addMergedRegion(new CellRangeAddress(dateStartRow, rowIndex - 1, 2, 2));
                    }
                    currentDate = itemDate;
                    dateStartRow = rowIndex;
                }
                createCell(row, 2, dateStr, dataStyle);

                // Medicine Name
                String medicineName = item.getMedicineName() != null ? item.getMedicineName() : "";
                createCell(row, 3, medicineName, dataStyle);

                // Units
                Integer units = item.getNumberOfUnit();
                createCell(row, 4, units != null ? units.toString() : "0", dataStyle);

                // Total Price
                Double totalPrice = item.getTotalPrice();
                createCell(row, 5, totalPrice != null ? String.format("%.2f", totalPrice) : "0.00", dataStyle);

                rowIndex++;
            }

            // Merge remaining cells if needed
            if (sortedData.size() > 0) {
                if (locationStartRow != rowIndex - 1) {
                    sheet.addMergedRegion(new CellRangeAddress(locationStartRow, rowIndex - 1, 0, 0));
                }
                if (deliveryCenterStartRow != rowIndex - 1) {
                    sheet.addMergedRegion(new CellRangeAddress(deliveryCenterStartRow, rowIndex - 1, 1, 1));
                }
                if (dateStartRow != rowIndex - 1) {
                    sheet.addMergedRegion(new CellRangeAddress(dateStartRow, rowIndex - 1, 2, 2));
                }
            }

            // Add footer row with total sum
            if (!sortedData.isEmpty()) {
                // Calculate total sum
                double totalSum = sortedData.stream()
                    .mapToDouble(item -> item.getTotalPrice() != null ? item.getTotalPrice() : 0.0)
                    .sum();

                // Add empty row for spacing
                rowIndex++;

                // Create footer row
                Row footerRow = sheet.createRow(rowIndex);

                // Merge cells from column 0 to 4 for "Total" label
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 4));

                // Create "Total" label cell
                Cell totalLabelCell = footerRow.createCell(0);
                totalLabelCell.setCellValue("Total");
                totalLabelCell.setCellStyle(footerStyle);

                // Create empty cells for the merged range to apply styling
                for (int i = 1; i <= 4; i++) {
                    Cell cell = footerRow.createCell(i);
                    cell.setCellStyle(footerStyle);
                }

                // Create total sum cell
                Cell totalSumCell = footerRow.createCell(5);
                totalSumCell.setCellValue(String.format("%.2f", totalSum));
                totalSumCell.setCellStyle(footerStyle);
            }

            // Auto-size columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // Light blue background
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Thin borders
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // Font styling - white font color
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        // Center alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // Thin borders
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // Center alignment for merged cells
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createFooterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // Thin borders
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // Bold font
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        // Center alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private void createCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }
}
