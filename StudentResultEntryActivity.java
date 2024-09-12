package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentResultEntryActivity extends AppCompatActivity {

    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }

    TextView header;
    String subjectName,examName,totalStudents, standard, division;
    RecyclerView recyclerView;
    private ArrayList<String> arrayList = new ArrayList<>();
    Button exportAsPDF, exportAsExcel;
    HashMap<String, String> testInfo,finalMarks;
    HashMap<String,HashMap<String, String>> saveFinalAnswers;
    HashMap<String, String> studentFinalAnswers;
    private static String EXCEL_SHEET_NAME = "StudentMarks.xls";
    private static Cell cell;
    private static Sheet sheet;
    public static final String RollNumber = "RollNumber";
    public static final String TotalMarks = "TotalMarks";
    private AdView adView;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result_entry);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        header = findViewById(R.id.textView_student_result_entry_head);
        recyclerView = findViewById(R.id.student_result_entry_recycler);
        exportAsPDF = findViewById(R.id.button_exportAsPDF);
        exportAsExcel = findViewById(R.id.button_exportAsEXCEL);
        imageButton = findViewById(R.id.button_home);

        Intent intent = getIntent();
        testInfo = (HashMap<String,String>) intent.getSerializableExtra("testInfo");
        finalMarks = (HashMap<String,String>) intent.getSerializableExtra("finalMarks");
        saveFinalAnswers = (HashMap<String,HashMap<String, String>>) intent.getSerializableExtra("saveFinalAnswers");
        studentFinalAnswers = (HashMap<String, String>) intent.getSerializableExtra("studentFinalAnswers");

        subjectName = testInfo.get("subject");
        examName = testInfo.get("exam name");
        standard = testInfo.get("standard");
        division = testInfo.get("division");
        totalStudents = testInfo.get("students");

        EXCEL_SHEET_NAME = subjectName+ "_" + examName + "_" + standard + "_" + division + "_StudentMarks.xls";

        header.setText(subjectName + " " + examName + " " + standard + " " + division);

        for(int i=0; i<Integer.parseInt(totalStudents);i++){
            arrayList.add(String.valueOf(i+1));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StudentResultEntryAdapter studentResultEntryAdapter = new StudentResultEntryAdapter(arrayList, testInfo, finalMarks, saveFinalAnswers, studentFinalAnswers,this,Integer.parseInt(totalStudents));
        recyclerView.setAdapter(studentResultEntryAdapter);

        exportAsExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<HashMap<String, String>> myList = getProducts();

                boolean result = exportDataIntoWorkbook(EXCEL_SHEET_NAME, myList);
                if(result){
                    Toast.makeText(StudentResultEntryActivity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(StudentResultEntryActivity.this,"FAILURE",Toast.LENGTH_SHORT).show();
                }

            }
        });

        exportAsPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = getExternalFilesDir(null).getAbsolutePath().toString()+ "/"+ subjectName+ "_" + examName + "_" + standard + "_" + division + "_StudentMarks.pdf";
                File file = new File(path);

                if(!file.exists()){
                    try{
                        file.createNewFile();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }

                Document document = new Document(PageSize.A4);

                try{
                    PdfWriter.getInstance(document,new FileOutputStream(file.getAbsoluteFile()));
                }
                catch (DocumentException e){
                    e.printStackTrace();
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }

                document.open();

                Font font = new Font(Font.FontFamily.COURIER,16);
                Paragraph paragraph = new Paragraph();

                paragraph.add(new Paragraph("------------------------------------" + "\n",font));
                paragraph.add(new Paragraph("Roll Number  |" + "\t" + "Total Marks" + "\n",font));
                paragraph.add(new Paragraph("------------------------------------" + "\n",font));

                for(int i = 0; i<Integer.parseInt(totalStudents);i++){
                    paragraph.add(new Paragraph(String.valueOf(i+1) + "         " +"   |"+ "\t" + finalMarks.get(String.valueOf(i+1)) + "\n",font));
                }
                paragraph.add(new Paragraph("------------------------------------" + "\n",font));

                try{
                    document.add(paragraph);
                }
                catch (DocumentException e){
                    e.printStackTrace();
                }

                document.close();
                Toast.makeText(StudentResultEntryActivity.this,"PDF Created and saved Successfully",Toast.LENGTH_SHORT).show();



            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StudentResultEntryActivity.this, "Going to Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(StudentResultEntryActivity.this, UserDashboardActivity.class);
                startActivity(intent1);
                finish();
            }
        });

    }

    private static boolean isExternalStorageReadOnly() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }

    public static Workbook createExcelWorkbook() {
        // New Workbook
        Workbook workbook = new HSSFWorkbook();

        cell = null;


        // New Sheet
        sheet = null;
        sheet = workbook.createSheet(EXCEL_SHEET_NAME);

        // Generate column headings
        Row row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("Roll Number");

        cell = row.createCell(1);
        cell.setCellValue("Total Marks");

        return workbook;
    }

    private static void fillDataIntoExcel(ArrayList<HashMap<String, String>> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i + 1);

            // Create Cells for each row
            cell = rowData.createCell(0);
            cell.setCellValue(dataList.get(i).get("RollNumber"));

            cell = rowData.createCell(1);
            cell.setCellValue(dataList.get(i).get("TotalMarks"));

        }
    }

    private boolean storeExcelInStorage(Workbook w, String fileName) {


        boolean isSuccess = false;

        File file = new File(this.getExternalFilesDir(null),fileName);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            w.write(fileOutputStream);
            Toast.makeText(StudentResultEntryActivity.this, "Writing file" + file,Toast.LENGTH_SHORT).show();
            isSuccess = true;


        } catch (IOException e) {
            Toast.makeText(StudentResultEntryActivity.this, "Error writing Exception",Toast.LENGTH_SHORT).show();
            isSuccess = false;
        } catch (Exception e) {
            Toast.makeText(StudentResultEntryActivity.this, "Failed to save file due to Exception:" + e,Toast.LENGTH_SHORT).show();
            isSuccess = false;
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return isSuccess;
    }


    public ArrayList<HashMap<String, String>> getProducts() {
        ArrayList<HashMap<String, String>> prolist = new ArrayList<>();

        for(int i = 0; i<Integer.parseInt(totalStudents);i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(RollNumber, String.valueOf(i+1));
            map.put(TotalMarks, finalMarks.get(String.valueOf(i+1)));
            prolist.add(map);
        }
        return prolist;
    }

    public boolean exportDataIntoWorkbook(String fileName, ArrayList<HashMap<String, String>> dataList) {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Toast.makeText(StudentResultEntryActivity.this,"Storage not available or read only",Toast.LENGTH_SHORT).show();
            return false;
        }

        Workbook workbook = createExcelWorkbook();
        fillDataIntoExcel(dataList);
        isWorkbookWrittenIntoStorage = storeExcelInStorage(workbook, fileName);

        return isWorkbookWrittenIntoStorage;
    }

}