package com.util;

/**
 * This class is used to retrieve a file from DB.
 *
 * @author w3spoint
 */
public class DBUtilTest {
//    public static void main(String[] args) {
//        Connection conn = null;
//        PreparedStatement preparedStatement = null;
//
//        String query = "select * from FILESTORE " +
//                "where FILE_ID = 2";
//
//        try {
//            //get connection
//            conn = DBUtil.getConnection();
//
//            //create preparedStatement
//            preparedStatement =
//                    conn.prepareStatement(query);
//
//            //execute query
//            ResultSet resultSet =
//                    preparedStatement.executeQuery();
//
//            resultSet.next();
//
//            Clob clob = resultSet.getClob(2);
//            Reader reader = clob.getCharacterStream();
//
//            FileWriter fileWriter =
//                    new FileWriter("savedFile.txt");
//
//            int i;
//            while ((i = reader.read()) != -1) {
//                fileWriter.write((char) i);
//            }
//
//            System.out.println("File retrieved successfully.");
//
//            //close connection
//            fileWriter.close();
//            preparedStatement.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}