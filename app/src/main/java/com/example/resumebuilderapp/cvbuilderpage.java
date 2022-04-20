package com.example.resumebuilderapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class cvbuilderpage extends AppCompatActivity {

    String strName, strCity, strEmail, strPhone, strDate, strDegree, strUniversity, strGPA, strYear, strJD1, strJDY1, strJD2, strJDY2;
    EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7, editText8, editText9, editText10, editText11, editText12, editText13;
    SQLiteDatabase sqLiteDatabase;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    Context context;
    private File pdfFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvbuilderpage);

        mySQLiteDBHandler mysqlLiteDBHandler;
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        try {

            mysqlLiteDBHandler = new mySQLiteDBHandler(this, "CVdatabase", null, 1);
            sqLiteDatabase = mysqlLiteDBHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE CVTable(Name TEXT, City TEXT, Email TEXT, Phone TEXT, Date TEXT, Degree TEXT, University TEXT, GPA TEXT, Year TEXT, JD1 TEXT, JDY1 TEXT, JD2 TEXT, JDY2 TEXT)");

        } catch (Exception e) {

            e.printStackTrace();

        }

        editText1 = findViewById(R.id.et1);
        editText2 = findViewById(R.id.et2);
        editText3 = findViewById(R.id.et3);
        editText4 = findViewById(R.id.et4);
        editText5 = findViewById(R.id.et5);
        editText6 = findViewById(R.id.et6);
        editText7 = findViewById(R.id.et7);
        editText8 = findViewById(R.id.et8);
        editText9 = findViewById(R.id.et9);
        editText10 = findViewById(R.id.et10);
        editText11 = findViewById(R.id.et11);
        editText12 = findViewById(R.id.et12);
        editText13 = findViewById(R.id.et13);


    }

    public void savebutton(View view) throws FileNotFoundException, DocumentException {

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", editText1.getText().toString());
        contentValues.put("City", editText2.getText().toString());
        contentValues.put("Email", editText3.getText().toString());
        contentValues.put("Phone", editText4.getText().toString());
        contentValues.put("Date", editText5.getText().toString());
        contentValues.put("Degree", editText6.getText().toString());
        contentValues.put("University", editText7.getText().toString());
        contentValues.put("GPA", editText8.getText().toString());
        contentValues.put("Year", editText9.getText().toString());
        contentValues.put("JD1", editText10.getText().toString());
        contentValues.put("JDY1", editText11.getText().toString());
        contentValues.put("JD2", editText12.getText().toString());
        contentValues.put("JDY2", editText13.getText().toString());

        sqLiteDatabase.insert("CVdatabase", null, contentValues);

        String query = "Select * FROM CVdatabase";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        try {

            if (cursor.moveToFirst()) {
                do {

                    strName = cursor.getString(0);
                    strCity = cursor.getString(1);
                    strEmail = cursor.getString(2);
                    strPhone = cursor.getString(3);
                    strDate = cursor.getString(4);
                    strDegree = cursor.getString(5);
                    strUniversity = cursor.getString(6);
                    strGPA = cursor.getString(7);
                    strYear = cursor.getString(8);
                    strJD1 = cursor.getString(9);
                    strJDY1 = cursor.getString(10);
                    strJD2 = cursor.getString(11);
                    strJDY2 = cursor.getString(12);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

            e.printStackTrace();

        }

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            };
                    return;
                }
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {

            generatepdf();
        }

    }

    public void generatepdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documemts");
        if (!docsFolder.exists()){
            docsFolder.mkdir();
        }

        String pdfname = "First.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, new FileOutputStream(docsFolder));

        document.open();

        document.add(new Paragraph("CV Builder"));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("Personal Info"));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("Name: "+ strName));
        document.add(new Paragraph("Address: "+ strCity));
        document.add(new Paragraph("Email: " + strEmail));
        document.add(new Paragraph("Phone: " + strPhone));
        document.add(new Paragraph("D.O.B: " + strDate));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("Education"));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("Course: " + strDegree));
        document.add(new Paragraph("Institute: " + strUniversity));
        document.add(new Paragraph("CGPA: " + strGPA ));
        document.add(new Paragraph("Year of Passing: " + strYear));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("Experience"));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("      "));
        document.add(new Paragraph("Job Post 1: " + strJD1));
        document.add(new Paragraph("Job Experience (time): " + strJDY1));
        document.add(new Paragraph("Job post 2: " + strJD2 ));
        document.add(new Paragraph("Job Experience (time): " + strJDY2));

        PdfWriter.getInstance(document, output);
        document.open();

        document.close();

        previewPdf();


    }

    public void previewPdf(){

        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = (List) packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
    }
}
