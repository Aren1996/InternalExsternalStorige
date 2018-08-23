package com.example.student.interexsetstorige;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText fileName;
    private EditText fileText;
    private Button saveButton;
    private Button readButton;

    private String fileTextString;
    private String fileNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        findViews();
        final RadioButton externalStorige = findViewById(R.id.external_storige);
        final RadioButton internalStorige = findViewById(R.id.internal_storige);
        externalStorige.setOnClickListener(this);
        internalStorige.setOnClickListener(this);

    }

    private void findViews() {
        saveButton = findViewById(R.id.save_button);
        readButton = findViewById(R.id.read_button);
        fileName = findViewById(R.id.file_name);
        fileText = findViewById(R.id.file_text);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.internal_storige:
                clickInterSaveBut();
                clickInterReadBut();
                break;
            case R.id.external_storige:
                clickExterSaveBut();
                clickExterReadBut();
                break;
            default:
                Toast.makeText(this, getText(R.string.False), Toast.LENGTH_SHORT).show();
        }
    }

    private void clickInterSaveBut() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileTextString = fileText.getText().toString();
                fileNameString = fileName.getText().toString();
                try {
                    final FileOutputStream fOut = openFileOutput(fileNameString, MODE_PRIVATE);
                    fOut.write(fileTextString.getBytes());
                    fOut.close();
                    Toast.makeText(getBaseContext(),
                            getString(R.string.File_save_Internal) + getFilesDir() + File.separator + fileNameString,
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clickInterReadBut() {
        readButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fileNameString = fileName.getText().toString();
                try {
                    final FileInputStream fin = openFileInput(fileNameString);
                    int c;
                    StringBuilder temp = new StringBuilder();
                    while ((c = fin.read()) != -1) {
                        temp.append(Character.toString((char) c));
                    }
                    fileText.setText(temp.toString());
                    Toast.makeText(getBaseContext(), getString(R.string.File_read_internal), Toast.LENGTH_SHORT).show();
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void clickExterSaveBut() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileTextString = fileText.getText().toString();
                fileNameString = fileName.getText().toString();
                final File extStore = Environment.getExternalStorageDirectory();
                final String path = extStore.getAbsolutePath() + File.separator + fileNameString;
                final String data = fileTextString;
                final File file = new File(path);
                try (final FileOutputStream out = new FileOutputStream(file);) {
                    if (!file.exists()) file.createNewFile();
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(out);
                    myOutWriter.append(data);
                    myOutWriter.close();
                    Toast.makeText(getBaseContext(),
                            getString(R.string.File_saved_External) + getFilesDir() + File.separator + fileNameString,
                            Toast.LENGTH_SHORT).show();
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void clickExterReadBut() {
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileTextString = fileText.getText().toString();
                fileNameString = fileName.getText().toString();
                final File extStore = Environment.getExternalStorageDirectory();
                final String path = extStore.getAbsolutePath() + File.separator + fileNameString;
                String s;
                final StringBuilder stringBuilder = new StringBuilder();
                final File file = new File(path);
                try (final FileInputStream fIn = new FileInputStream(file);
                     final BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn))) {
                    while ((s = myReader.readLine()) != null) {
                        stringBuilder.append(s);
                    }
                    fileText.setText(stringBuilder.append("\n"));
                    Toast.makeText(getBaseContext(),
                            getString(R.string.File_read_External) + getFilesDir() + File.separator + fileNameString,
                            Toast.LENGTH_SHORT).show();
                } catch (IOException ignored) {
                }
            }
        });
    }

    private void permission() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                69);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 69:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permission();
                }
        }
    }
}
