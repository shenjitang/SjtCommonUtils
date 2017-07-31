package org.shenjitang.common.officeutils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.File;

/**
 * 使用要求
 * 1.该功能目前只支持windows系统,jacob需要调用Windows的com对象来转换
 * 2.安装OFFICE 支持03以上版本
 * 3.安装PDF转换插件 一般OFFICE安装完整应该自带该功能
 *
 * 使用准备
 * 1.对应不同位操作系统 需要将以下指定文件放入以下2个目录中
 *
 *@Resource jacob-1.18-x64.dll 64位操作系统
 *@Resource jacob-1.18-x86.dll 32位操作系统
 *
 * @PATH jdk\bin  jre\bin
 *
 * Created by gang.xie on 2017/7/31.
 */
public class WindowsOfficeConvert2PDF {

    /*转PDF格式值*/
    private static final int WORD_PDF = 17;
    private static final int EXCEL_PDF = 0;
    private static final int PPT_PDF = 32;

    public static boolean convert2PDF(String inputFile, String pdfFile) {
        String suffix = getFileSufix(inputFile);
        File file = new File(inputFile);
        if (!file.exists()) {
            System.out.println("文件不存在！");
            return false;
        }
        if (suffix.equals("pdf")) {
            System.out.println("PDF not need to convert!");
            return false;
        }
        if (suffix.equals("doc") || suffix.equals("docx") || suffix.equals("txt")) {
            return word2PDF(inputFile, pdfFile);
        } else if (suffix.equals("ppt") || suffix.equals("pptx")) {
            return ppt2PDF(inputFile, pdfFile);
        } else if (suffix.equals("xls") || suffix.equals("xlsx")) {
            return excel2PDF(inputFile, pdfFile);
        } else {
            System.out.println("文件格式不支持转换!");
            return false;
        }
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    private static String getFileSufix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }

    /**
     * Word文档转换
     *
     * @param inputFile
     * @param pdfFile
     * @return
     */
    private static boolean word2PDF(String inputFile, String pdfFile) {
        ComThread.InitSTA();

        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");// 创建一个word对象
            app.setProperty("Visible", new Variant(false)); // 不可见打开word
            app.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch docs = app.getProperty("Documents").toDispatch();// 获取文挡属性

            System.out.println("打开文档 >>> " + inputFile);
            // Object[]第三个参数是表示“是否只读方式打开”
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            doc = Dispatch.call(docs, "Open", inputFile, false, true).toDispatch();
            // 调用Document对象的SaveAs方法，将文档保存为pdf格式
            System.out.println("转换文档 [" + inputFile + "] >>> [" + pdfFile + "]");
            Dispatch.call(doc, "SaveAs", pdfFile, WORD_PDF);//word保存为pdf格式宏，值为17
//            Dispatch.call(doc, "ExportAsFixedFormat", pdfFile, wdFormatPDF); // word保存为pdf格式宏，值为17

            long end = System.currentTimeMillis();

            System.out.println("用时：" + (end - start) + "ms.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            System.out.println("关闭文档");
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        // 如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
        ComThread.quitMainSTA();
        return false;
    }

    /**
     * PPT文档转换
     *
     * @param inputFile
     * @param pdfFile
     * @return
     */
    private static boolean ppt2PDF(String inputFile, String pdfFile) {
        ComThread.InitSTA();

        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch ppt = null;
        try {
            app = new ActiveXComponent("PowerPoint.Application");// 创建一个PPT对象
            // app.setProperty("Visible", new Variant(false)); // 不可见打开（PPT转换不运行隐藏，所以这里要注释掉）
            // app.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch ppts = app.getProperty("Presentations").toDispatch();// 获取文挡属性

            System.out.println("打开文档 >>> " + inputFile);
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            ppt = Dispatch.call(ppts, "Open", inputFile,
                    true,// ReadOnly
                    true,// Untitled指定文件是否有标题
                    false// WithWindow指定文件是否可见
            ).toDispatch();

            System.out.println("转换文档 [" + inputFile + "] >>> [" + pdfFile + "]");
            Dispatch.call(ppt, "SaveAs", pdfFile, PPT_PDF);

            long end = System.currentTimeMillis();

            System.out.println("用时：" + (end - start) + "ms.");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(ppt, "Close");
            System.out.println("关闭文档");
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        ComThread.Release();
        ComThread.quitMainSTA();
        return false;
    }

    /**
     * Excel文档转换
     *
     * @param inputFile
     * @param pdfFile
     * @return
     */
    private static boolean excel2PDF(String inputFile, String pdfFile) {
        ComThread.InitSTA();

        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch excel = null;
        try {
            app = new ActiveXComponent("Excel.Application");// 创建一个PPT对象
//            app.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            app.setProperty("Visible", new Variant(false)); // 不可见打开
            Dispatch workbooks = app.getProperty("Workbooks").toDispatch();

            System.out.println("打开文档 >>> " + inputFile);
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            excel = Dispatch.call(workbooks, "Open", inputFile, false, true).toDispatch();
            // 调用Document对象方法，将文档保存为pdf格式
            System.out.println("转换文档 [" + inputFile + "] >>> [" + pdfFile + "]");

            // Excel 不能调用SaveAs方法
            Dispatch.call(excel, "ExportAsFixedFormat", EXCEL_PDF, pdfFile);

            long end = System.currentTimeMillis();

            System.out.println("用时：" + (end - start) + "ms.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(excel, "Close", false);
            System.out.println("关闭文档");
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        ComThread.Release();
        ComThread.quitMainSTA();
        return false;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        WindowsOfficeConvert2PDF.convert2PDF("E:\\demo\\1.doc", "E:\\demo\\1.pdf");
        WindowsOfficeConvert2PDF.convert2PDF("E:\\demo\\2.xls", "E:\\demo\\2.pdf");
        WindowsOfficeConvert2PDF.convert2PDF("E:\\demo\\3.xlsx", "E:\\demo\\3.pdf");
    }

}
