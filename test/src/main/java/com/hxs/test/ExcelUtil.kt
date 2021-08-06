package com.hxs.test

import android.content.Context
import android.widget.Toast
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.format.*
import jxl.format.Alignment
import jxl.format.Border
import jxl.format.BorderLineStyle
import jxl.format.Colour
import jxl.read.biff.BiffException
import jxl.write.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


object ExcelUtil {
    private var arial14font: WritableFont? = null //可写字体
    private var arial14format: WritableCellFormat? = null //单元格格式
    private var arial10font: WritableFont? = null
    private var arial10format: WritableCellFormat? = null
    private var arial12font: WritableFont? = null
    private var arial12format: WritableCellFormat? = null
    private const val UTF8_ENCODING = "UTF-8"

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
     *
     * @param filePath  导出excel的存放地址
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名
     */
    fun initExcel(filePath: String, sheetName: String?, colName: Array<String?>) {
        format()
        //创建一个工作薄，就是整个Excel文档
        var workbook: WritableWorkbook? = null
        try {
            val file = File(filePath)
            if (!file.exists()) {
                file.createNewFile()
            }
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
            sheet.mergeCells(0, 0, colName.size - 1, 0)
            sheet.addCell(Label(0, 0, "我是标题", arial14format))
            sheet.setRowView(0, 520)

            //再同一个单元格中写入数据，上一个数据会被下一个数据覆盖
            for (col in colName.indices) {
                sheet.addCell(Label(col, 1, colName[col], arial10format))
            }
            //设置行高 参数的意义为（第几行， 行高）
            sheet.setRowView(1, 340)
            workbook.write() // 写入数据
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: WriteException) {
            e.printStackTrace()
        } finally {
            if (workbook != null) {
                try {
                    workbook.close() // 关闭文件
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: WriteException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 见指定类型的List写入到Excel文件中
     *
     * @param objList  代写入的List
     * @param fileName
     * @param context
     * @param <T>
    </T> */
    fun <T> writeObjListToExcel(objList: List<T>?, fileName: String, context: Context?) {
        if (objList != null && objList.isNotEmpty()) {
            //创建一个工作薄，就是整个Excel文档
            var writeBook: WritableWorkbook? = null
            var inputStream: InputStream? = null
            try {
                val settings = WorkbookSettings()
                settings.encoding = UTF8_ENCODING
                inputStream = FileInputStream(File(fileName))
                //Workbook不但能用来创建工作薄，也可以读取现有的工作薄
                val workbook = Workbook.getWorkbook(inputStream)
                //创建一个工作薄，就是整个Excel文档
                writeBook = Workbook.createWorkbook(File(fileName), workbook)
                //读取表格
                val sheet = writeBook.getSheet(0)
                for (j in objList.indices) {
                    val demoBean: DemoBean = objList[j] as DemoBean
                    val list: MutableList<String> = ArrayList()
                    list.add(demoBean.name)
                    list.add(java.lang.String.valueOf(demoBean.age))
                    list.add(java.lang.String.valueOf(demoBean.isBoy))
                    for (i in list.indices) {
                        sheet.addCell(Label(i, j + 2, list[i], arial12format)) //向一行中添加数据
                        if (list[i].length <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list[i].length + 8)
                        } else {
                            sheet.setColumnView(i, list[i].length + 5)
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350)
                }
                writeBook.write()
                workbook.close()
                Toast.makeText(context, "导出Excel成功", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: BiffException) {
                e.printStackTrace()
            } catch (e: WriteException) {
                e.printStackTrace()
            } finally {
                if (writeBook != null) {
                    try {
                        writeBook.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: WriteException) {
                        e.printStackTrace()
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}