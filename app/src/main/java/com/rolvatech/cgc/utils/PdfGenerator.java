package com.rolvatech.cgc.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rolvatech.cgc.model.Task;
import com.rolvatech.cgc.model.TaskList;
import com.rolvatech.cgc.model.UserDetails;
import com.rolvatech.cgc.model.UserDetailsResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Hanumantharao on 17-10-2020.
 */
public class PdfGenerator {


    private static Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);

    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    public static boolean generatePdf(File file, UserDetailsResponse response, String aboutChild) {

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document, "", "");
            //addTitlePage(document);
            addContent(document, response, aboutChild);
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document, String tilte, String subject) {
        document.addTitle(tilte);
        document.addSubject(subject);
        //document.addKeywords("Java, PDF, iText");
        document.addAuthor("Hanuman");
        document.addCreator("Hanuman");

    }


    private static void addContent(Document document, UserDetailsResponse response, String aboutChild) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        createChildInformation(document, response.getUserDetails());
        addEmptyLine(paragraph, 4);
        aboutChild(document, response.getUserDetails(), aboutChild);
        addEmptyLine(paragraph, 4);
        createTasksAssigned(document, response.getTaskList());

    }

    private static void createChildInformation(Document document, UserDetails childInfo)
            throws DocumentException {

        if (childInfo != null) {

            Paragraph preface = new Paragraph();
            // We add one empty line
            addEmptyLine(preface, 2);
            // Lets write a big header
            preface.add(new Paragraph("Child Information: ", headerFont));

            addEmptyLine(preface, 2);

            document.add(preface);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            table.addCell(createTableCell("Name : ", headerFont));
            table.addCell(createTableCell(validateString(childInfo.getName()), normalFont));

            table.addCell(createTableCell("Program : ", headerFont));
            table.addCell(createTableCell("ABA", normalFont));

            table.addCell(createTableCell("DOB : ", headerFont));
            table.addCell(createTableCell("" + childInfo.getAge(), normalFont));

            table.addCell(createTableCell("Period : ", headerFont));
            table.addCell(createTableCell("", normalFont));

            table.addCell(createTableCell("Parents Name : ", headerFont));
            table.addCell(createTableCell(validateString(childInfo.getParentName()), normalFont));

            table.addCell(createTableCell("Therapist : ", headerFont));
            String staffName = "";
            if (childInfo.getStaff() != null) {
                staffName = validateString(childInfo.getStaff().getFirstname()) + " " + validateString(childInfo.getStaff().getLastName());
            }
            table.addCell(createTableCell(staffName, normalFont));

            document.add(table);

        }

    }

    private static void createTasksAssigned(Document document, List<TaskList> taskList)
            throws DocumentException {

        String areaName = "Tasks Assigned";

        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph(areaName, headerFont));

        addEmptyLine(preface, 1);

        document.add(preface);

        if (taskList != null) {

            int length = taskList.size();

            for (int i = 0; i < length; i++) {
                createAreaTasks(document, taskList.get(i));
            }
        }
    }

    private static void createAreaTasks(Document document, TaskList tasks)
            throws DocumentException {

        if (tasks != null) {

            Paragraph preface = new Paragraph();
            // We add one empty line
            addEmptyLine(preface, 1);
            // Lets write a big header
            preface.add(new Paragraph(validateString(tasks.getName()), headerFont));

            addEmptyLine(preface, 2);

            document.add(preface);

            if (tasks.getTasks() != null && tasks.getTasks().size() > 0) {

                PdfPTable table = new PdfPTable(2);

                int length = tasks.getTasks().size();

                for (int i = 0; i < length; i++) {

                    Task task = tasks.getTasks().get(i);

                    table.addCell(createTableCell(validateString(task.getTaskName()), normalFont));
                    table.addCell(createTableCell(validateString(task.getTaskName()), normalFont));

                }

                document.add(table);
            }

        }

    }

    private static void aboutChild(Document document, UserDetails userDetails, String aboutChild)
            throws DocumentException {

        if (userDetails != null) {

            Paragraph preface = new Paragraph();
            // We add one empty line
            addEmptyLine(preface, 1);
            // Lets write a big header
            preface.add(new Paragraph("About Child While Joining: ", headerFont));

            addEmptyLine(preface, 2);
            // Will create: Report generated by: _name, _date
            preface.add(new Paragraph(validateString(userDetails.getAbout1()), normalFont));
            addEmptyLine(preface, 2);
            preface.add(new Paragraph(validateString(aboutChild), normalFont));
            addEmptyLine(preface, 2);
            document.add(preface);
        }

    }

    private static PdfPCell createTableCell(String value, Font font) {
        PdfPCell c1 = new PdfPCell(new Phrase(value));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setPadding(8);
        return c1;
    }

   /* private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }*/

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static String validateString(String value) {
        if (value == null) {
            value = "";
        }
        return value;
    }

}
