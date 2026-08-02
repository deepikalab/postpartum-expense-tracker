package com.deepika.postpartum.expensetracker.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;


/**
 * Responsible for saving generated reports into CSV files.
 *
 * Responsibilities:
 * - Creates report directory if it does not exist
 * - Writes report content into CSV files
 * - Handles file-related operations
 *
 * Does NOT contain:
 * - Report generation logic
 * - Expense business logic
 */
public class ReportCsvWriter {

    private static final String REPORT_FOLDER = "reports";

    /**
     * Saves report content as a CSV file.
     *
     * @param fileName Name of the CSV file
     * @param content Generated report content
     */
    public void saveReport(
            String fileName,
            String content) {


        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                    "Report content cannot be empty.");
        }

        // Create reports directory if it is not available
        File folder = new File(REPORT_FOLDER);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(
                folder,
                fileName);

        try (PrintWriter writer =
                     new PrintWriter(
                             new FileWriter(
                                     file,
                                     StandardCharsets.UTF_8))) {
            // Write generated report content into CSV file
            writer.print(content);

        } catch (IOException e) {

            String message = e.getMessage();

            if (message != null &&
                    message.toLowerCase()
                            .contains("being used")) {

                throw new RuntimeException(
                        "CSV file is open. Please close it and try again.");
            }

            throw new RuntimeException(
                    "Unable to save CSV report: " + message);
        }
    }
}