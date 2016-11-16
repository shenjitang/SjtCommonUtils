/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.linuxense.javadbf;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * 生成DBF文件
 * @author xth
 */
public class JavaDbf {

    public static void produceDBF(String sql,String driver,String url,String username,String password,String targetFile){
        Connection connection = null;
        Statement smt = null;
        try {
            Class.forName(driver);
            DriverManager.setLoginTimeout(100);
            connection = DriverManager.getConnection(url, username,password);
            connection.setAutoCommit(false);
            smt = connection.createStatement();
            ResultSet rs = smt.executeQuery(sql);
            connection.commit();
            toFile(rs,targetFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
                smt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void toFile(ResultSet rs, String outFile)
            throws DBFException, SQLException {

        System.out.println("start write rite file ok:" + outFile);

        DBFWriter writer = null;

        ResultSetMetaData meta = rs.getMetaData();

        int columnCount = meta.getColumnCount();

        int fieldCount = columnCount;

        System.out.println("fieldCount:"+fieldCount);
//每一列的标题

        DBFField[] fields = new DBFField[fieldCount];

        for (int i = 0; i < fieldCount; i++) {
            fields[i] = new DBFField();

            fields[i].setName(meta.getColumnName(i + 1));

            fields[i].setDataType(DBFField.FIELD_TYPE_C);

            fields[i].setFieldLength(meta.getColumnDisplaySize(i + 1));


        }

        File file=new File(outFile);
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException ex) {
        }
        writer =new DBFWriter(file);

        writer.setFields(fields);
        writer.setCharactersetName("GBK");
        long index = 0;

        while (rs.next()) {

            Object rowData[] = new Object[columnCount];

            for (int j = 0; j < columnCount; j++) {
                rowData[j] = rs.getString(j + 1);
                System.out.println(rowData[j]);
            }

            writer.addRecord(rowData);

        }
        writer.write();
    }
}
