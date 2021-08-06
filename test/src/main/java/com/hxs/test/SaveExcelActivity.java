package com.hxs.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveExcelActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;

    private AlertDialog alertDialog;

    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private int REQUEST_PERMISSION_CODE = 1000;

    @SuppressLint("SdCardPath")
    private String filePath = "/sdcard/AndroidExcelDemo";

    //请求权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(SaveExcelActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
//                LogUtil.e("requestPermission:" + "用户之前已经授予了权限！");
            } else {
                requestPermissions(permissions, REQUEST_PERMISSION_CODE);
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_excel);
        requestPermission();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        Button exportButton = findViewById(R.id.export_button);
        exportButton.setOnClickListener(this);

        Button openButton = findViewById(R.id.open_button);
        openButton.setOnClickListener(this);
        textView = findViewById(R.id.textView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                LogUtil.e("申请成功");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveExcelActivity.this);
                builder.setTitle("permission");
                builder.setMessage("点击允许才可以使用");
                builder.setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        ActivityCompat.requestPermissions(SaveExcelActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                });
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }
    }

    private void showDialogTipUserRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.export_button) {
            exportExcel(this);
        } else if (id == R.id.open_button) {
            openDir();
        }
    }

    private void openDir() {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setDataAndType(Uri.fromFile(file), "file/*");

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "没用正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    //导出
    private void exportExcel(Context context) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();

        }
        String excelFileName = "/demo.xls";

        String[] title = {"姓名", "年龄", "男孩"};
        String sheetName = "表格名称";

        List<DemoBean> demoBeanList = new ArrayList<>();
        DemoBean demoBean1 = new DemoBean("张三", 10, false);
        DemoBean demoBean2 = new DemoBean("李四", 11, true);
        DemoBean demoBean3 = new DemoBean("王二", 12, true);
        DemoBean demoBean4 = new DemoBean("麻子", 13, true);
        demoBeanList.add(demoBean1);
        demoBeanList.add(demoBean2);
        demoBeanList.add(demoBean3);
        demoBeanList.add(demoBean4);
        filePath = filePath + excelFileName;//文件的路径
//        LogUtil.e(filePath);

        ExcelUtil.INSTANCE.initExcel(filePath, sheetName, title);
        ExcelUtil.INSTANCE.writeObjListToExcel(demoBeanList, filePath, context);
        textView.setText("excel已导出: " + filePath);
    }
}

