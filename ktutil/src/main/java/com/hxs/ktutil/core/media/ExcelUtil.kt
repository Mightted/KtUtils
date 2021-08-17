package com.hxs.ktutil.core.media

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import jxl.Workbook
import jxl.format.*

import jxl.format.Alignment
import jxl.format.Border
import jxl.format.BorderLineStyle
import jxl.format.Colour
import jxl.write.*
import java.io.File
import java.io.FileInputStream

/**
 * Excel操作类，需要引入依赖
 * implementation group: 'net.sourceforge.jexcelapi', name: 'jxl', version: '2.6.12'
 * 拓展：Android 打开office文档
 * https://blog.csdn.net/T_yoo_csdn/article/details/86590985
 */
object ExcelUtil {

    private lateinit var arial14font: WritableFont//可写字体
    private lateinit var arial14format: WritableCellFormat //单元格格式
    private lateinit var arial10font: WritableFont
    private lateinit var arial10format: WritableCellFormat
    private lateinit var arial12font: WritableFont
    private lateinit var arial12format: WritableCellFormat
//    private const val UTF8_ENCODING = "UTF-8"

    init {
        format()
    }

    //单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
    private fun format() {
        try {
            //字体 ARIAL， 字号 14  bold  粗体
            arial14font = WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD).apply {
                colour = Colour.LIGHT_BLUE //字体的颜色
                underlineStyle = UnderlineStyle.SINGLE //设置下划线

            }
            //初始化单元格格式
            arial14format = WritableCellFormat(arial14font).apply {
                alignment = Alignment.CENTRE //对齐方式
                setBorder(Border.ALL, BorderLineStyle.THIN) //边框的格式
                setBackground(Colour.VERY_LIGHT_YELLOW) //底色
            }

            arial10font = WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD)
            arial10format = WritableCellFormat(arial10font).apply {
                alignment = Alignment.CENTRE
                setBorder(Border.ALL, BorderLineStyle.THIN)
                setBackground(Colour.GRAY_25)
            }

            arial12font = WritableFont(WritableFont.ARIAL, 10)
            arial12format = WritableCellFormat(arial12font).apply {
                alignment = Alignment.CENTRE
                setBorder(Border.ALL, BorderLineStyle.THIN)
            }

        } catch (e: WriteException) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化Excel
     * 写入字段名称，表名
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名
     */
    fun initExcel(file: File, sheetName: String, colName: Array<String>) {

        if (!file.path.endsWith("xls") && !file.path.endsWith("xlsx")) {
            return
        }

        //创建一个工作薄，就是整个Excel文档
        var workbook: WritableWorkbook? = null
        try {

            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            //使用Workbook创建一个工作薄，就是整个Excel文档
            workbook = Workbook.createWorkbook(file)
            //设置表格的名称(两个参数分别是工作表名字和插入位置，这个位置从0开始)
            val sheet = workbook.createSheet(sheetName, 0)
            //创建label标签：实际就是单元格的标签（三个参数分别是：col + 1列，row + 1行， 内容， 单元格格式）
//            Label label = new Label(0, 0, filePath, arial14format);//设置第一行的单元格标签为：标题
            //将标签加入到工作表中
//            sheet.addCell(label);

            //通过writablesheet.mergeCells(int x,int y,int m,int n);来实现的。
            // 表示将从第x+1列，y+1行到m+1列，n+1行合并 (四个点定义了两个坐标，左上角和右下角)


//            sheet.mergeCells(0, 0, colName.size - 1, 0)
//            sheet.addCell(Label(0, 0, "我是标题", arial14format))
//            sheet.setRowView(0, 520)

            //再同一个单元格中写入数据，上一个数据会被下一个数据覆盖
            for (col in colName.indices) {
                sheet.addCell(Label(col, 0, colName[col], arial10format))
            }
            //设置行高 参数的意义为（第几行， 行高）
            sheet.setRowView(0, 340)
            workbook.write() // 写入数据
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (workbook != null) {
                try {
                    workbook.close() // 关闭文件
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 见指定类型的List写入到Excel文件中
     */
    fun <T> writeObjListToExcel(
        file: File,
        list: List<T>,
        buildRowData: (T) -> Array<String>,
        succeedCallback: (File: File) -> Unit
    ) {
        if (list.isEmpty()) {
            return
        }

        FileInputStream(file).use { inputStream ->

            //创建一个工作薄，就是整个Excel文档
            var writeBook: WritableWorkbook? = null
//            val settings = WorkbookSettings()
//            settings.encoding = UTF8_ENCODING
            try {

                //Workbook不但能用来创建工作薄，也可以读取现有的工作薄
                val workbook = Workbook.getWorkbook(inputStream)
                //创建一个工作薄，就是整个Excel文档
                writeBook = Workbook.createWorkbook(file, workbook)
                //读取表格
                val sheet = writeBook.getSheet(0)
                for ((rowIndex, value) in list.withIndex()) {
                    val data = buildRowData(value)
                    for ((columnIndex, item) in data.withIndex()) {
                        sheet.addCell(
                            Label(columnIndex, rowIndex + 1, item, arial12format)
                        ) //向一行中添加数据
                        if (item.length <= 4) {
                            //设置列宽
                            sheet.setColumnView(columnIndex, item.length + 8)
                        } else {
                            sheet.setColumnView(columnIndex, item.length + 5)
                        }
                    }
                    //设置行高
                    sheet.setRowView(rowIndex + 1, 350)
                }
                writeBook.write()
                workbook.close()
                succeedCallback(file)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (writeBook != null) {
                    try {
                        writeBook.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    fun <T> createExcel(
        file: File, sheetName: String, colName: Array<String>,
        list: List<T>,
        buildRowData: (T) -> Array<String>,
        succeedCallback: (File: File) -> Unit
    ) {

        if (!file.path.endsWith("xls") && !file.path.endsWith("xlsx")) {
            return
        }
        if (list.isEmpty()) {
            return
        }

        //创建一个工作薄，就是整个Excel文档
        var workbook: WritableWorkbook? = null
        try {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            //使用Workbook创建一个工作薄，就是整个Excel文档
            workbook = Workbook.createWorkbook(file)
            //设置表格的名称(两个参数分别是工作表名字和插入位置，这个位置从0开始)
            val sheet = workbook.createSheet(sheetName, 0)

            //再同一个单元格中写入数据，上一个数据会被下一个数据覆盖
            for (col in colName.indices) {
                sheet.addCell(Label(col, 0, colName[col], arial10format))
            }
            //设置行高 参数的意义为（第几行， 行高）
            sheet.setRowView(0, 340)


            //Workbook不但能用来创建工作薄，也可以读取现有的工作薄
//            val workbook = Workbook.getWorkbook(inputStream)
            //创建一个工作薄，就是整个Excel文档
//            writeBook = Workbook.createWorkbook(file, workbook)
            //读取表格
//            val sheet = writeBook.getSheet(0)
            for ((rowIndex, value) in list.withIndex()) {
                val data = buildRowData(value)
                for ((columnIndex, item) in data.withIndex()) {
                    sheet.addCell(
                        Label(columnIndex, rowIndex + 1, item, arial12format)
                    ) //向一行中添加数据
                    if (item.length <= 4) {
                        //设置列宽
                        sheet.setColumnView(columnIndex, item.length + 8)
                    } else {
                        sheet.setColumnView(columnIndex, item.length + 5)
                    }
                }
                //设置行高
                sheet.setRowView(rowIndex + 1, 350)
            }
            workbook.write()
            succeedCallback(file)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                workbook?.close() // 关闭文件
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun openExcel(context: Context, uri: Uri) {

        val intent = Intent(Intent.ACTION_VIEW)

        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        val uri: Uri
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            if (providerPath == null) {
//                return
//            }
//            uri = FileProvider.getUriForFile(context, providerPath, file)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        } else {
//            uri = Uri.fromFile(file)
//        }
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        context.startActivity(intent)
    }


}