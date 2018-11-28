package Untils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataDictionary {
    /**
     * 获得数据字典的表单
     *
     * @param tableName
     * @return
     */
    public static List<Attribute> getDataDictionary(String tableName) {
        List<Attribute> listDictionary = new ArrayList<>();
        try {
            String primaryKey = "";
            String indexName = "";
            String column="";
            List<String> columnlist=new ArrayList<>();
            List<String> indexNamelist=new ArrayList<>();
            File fileDataDictionary = new File(DatabaseUntils.dataDictionaryFilePath + tableName + ".txt");
            if (fileDataDictionary.exists()) {
                String line;
                FileReader fr = new FileReader(fileDataDictionary.getAbsoluteFile());
                BufferedReader reader = new BufferedReader(fr);
                while ((line = reader.readLine()) != null) {
                    if (line.substring(0, 11).equals("primary key") || line.substring(0, 11).equals("PRIMARY KEY")) {
                        String getPrimaryKey = "\\([A-Za-z_]+\\)";
                        Pattern p3 = Pattern.compile(getPrimaryKey);
                        Matcher m3 = p3.matcher(line);
                        while (m3.find()) {
                            primaryKey = m3.group();
                            int length = primaryKey.length();
                            primaryKey = primaryKey.substring(1, length - 1);
                        }

                    } else if (line.substring(0, 5).equals("index") || line.substring(0, 5).equals("INDEX")) {
                        String getColumn = "\\([A-Za-z_]+\\)";
                        Pattern p3 = Pattern.compile(getColumn);
                        Matcher m3 = p3.matcher(line);
                        while (m3.find()) {
                            column = m3.group();
                            int length = column.length();
                            column = column.substring(1, length - 1);
                            columnlist.add(column);
                        }
                        String getIndexName = "(index|INDEX)\\s+[A-Za-z_]+";
                        indexName=RegularUntils.getValues(line,getIndexName);
                        indexName=indexName.replace(" ","");
                        indexName=indexName.substring(5,indexName.length());
                        indexNamelist.add(indexName);

                    }else {
                        Attribute attribute = new Attribute();
                        String word[] = line.split(" ");
                        for (int i = 0; i < word.length; i++) {
                            if (!word[0].equals("primary") || !word[0].equals("PRIMARY")||!word[0].equals("index") || !word[0].equals("INDEX")) {
                                attribute.setAttributeName(word[0]);
                            }
                        }
                        String getAttributeType = "(varchar|int|double|float)";
                        attribute.setAttributeType(RegularUntils.getValues(word[1], getAttributeType));
                        String getAttributeLength = "[0-9]+";
                        attribute.setAttributeLength(Integer.parseInt(RegularUntils.getValues(line, getAttributeLength)));
                        String isNull = "(null|not null)";
                        String result = RegularUntils.getValues(line, isNull);
                        if (result.equals("null")) {
                            attribute.setNoEmpty(false);
                        }else{
                            attribute.setNoEmpty(true);
                        }
                        listDictionary.add(attribute);
                    }
                }
                for (int i = 0; i < listDictionary.size(); i++) {
                    if (listDictionary.get(i).getAttributeName().equals(primaryKey)) {
                        listDictionary.get(i).setPrimaryKey("pri");
                        listDictionary.get(i).setIndexName(primaryKey);
                    }
                    if(columnlist!=null&&indexNamelist!=null){
                        for(int j=0;j<columnlist.size();j++){
                            if(listDictionary.get(i).getAttributeName().equals(columnlist.get(j))){
                                listDictionary.get(i).setKey("mul");
                                listDictionary.get(i).setIndexName(indexNamelist.get(j));
                            }
                        }
                    }

                }
                fr.close();
                reader.close();
            } else {
                System.out.println("The Table not exits !");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listDictionary;
    }

    /**
     * 创建数据字典
     * @param sentence
     * @return
     */
    public static boolean createDataDictionary(String sentence) {
        String line = "";
        String tableName = "";
        String primaryKey = "";
        String getTableName = "(create|CREATE)\\s+(table|TABLE)\\s+[A-Za-z_]+";
        String getAttribute = "\\(\\s*([A-Za-z_]+\\s+(int|double|varchar|float)\\s*\\(\\s*[1-9][0-9]*\\s*\\)\\s+(not null|null),\\s*)*([A-Za-z_]+\\s+(int|double|varchar|float)\\s*\\(\\s*[1-9][0-9]*\\s*\\)\\s+(not null|null)(((\\s*,\\s*primary key)|(\\s*,\\s*PRIMARY KEY))\\([A-Za-z_]+\\))?\\))";
        Pattern p1 = Pattern.compile(getTableName);
        Matcher m1 = p1.matcher(sentence);
        while (m1.find()) {
            String temp = m1.group().replace(" ", "");
            int length = temp.length();
            temp = temp.substring(11, length);
            tableName = temp;
        }
        File fileDataDictionary = new File(DatabaseUntils.dataDictionaryFilePath + tableName + ".txt");
        if (!fileDataDictionary.exists()) {
            List<String> list = new ArrayList<>();
            Pattern p2 = Pattern.compile(getAttribute);
            Matcher m2 = p2.matcher(sentence);
            while (m2.find()) {
                String temp = m2.group();
                temp = temp.substring(1, temp.length() - 1);
                String[] word = temp.split(",");
                for (int i = 0; i < word.length; i++) {
                    line = word[i];
                    if (line.substring(0, 10).equals("primary key") || line.substring(0, 10).equals("PRIMARY KEY")) {
                        String getPrimaryKey = "\\([A-Za-z_]+\\)";
                        Pattern p3 = Pattern.compile(getPrimaryKey);
                        Matcher m3 = p3.matcher(line);
                        while (m3.find()) {
                            primaryKey = m3.group();
                            int length = primaryKey.length();
                            primaryKey = primaryKey.substring(1, length - 1);
                        }
                    } else {
                        String word2[] = line.split(" ");
                        list.add(word2[0]);
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    for (int j = i + 1; j < list.size(); j++) {
                        if (list.get(i).equals(list.get(j))) {
                            System.out.println("Create table fail ! It has same Field !");
                            return false;
                        }
                    }
                }

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals(primaryKey) || primaryKey.equals("")) {
                        DataTableUntils.createTable(tableName);
                        fileDataDictionary = new File(DatabaseUntils.dataDictionaryFilePath + tableName + ".txt");
                        for (int j = 0; j < word.length; j++) {
                            line = word[j] + "\n";
                            DataTableUntils.writeInToFile(fileDataDictionary, line);
                        }
                        System.out.println("Query Ok !");
                        return true;

                    } else {
                        continue;
                    }
                }
                System.out.println("Primary key don't belong to field !");
                return false;
            }
        } else {
            System.out.println("The table '" + tableName + "' is existed !");
            return false;
        }
        return false;
    }

    /**
     * 从文件删除一行数据
     *
     * @param file
     * @param columnName
     */
    public static void delFromFile(File file, String columnName) {
        try {
            List<String> list = new ArrayList<>();
            String line;
            FileReader fr = new FileReader(file.getAbsoluteFile());
            BufferedReader reader = new BufferedReader(fr);
            while ((line = reader.readLine()) != null) {
                String dictionary[] = line.split(" ");
                if (dictionary[0].equals(columnName)) {
                   continue;
                } else if(dictionary[0].equals("index")||dictionary[0].equals("INDEX")){
                    String getIndexName="(index|INDEX)\\s+[A-Za-z_]+";
                    String indexName=RegularUntils.getValues(line,getIndexName);
                    indexName=indexName.replace(" ","");
                    indexName=indexName.substring(5,indexName.length());
                    if(indexName.equals(columnName)){
                        continue;
                    }else{
                        list.add(line);
                    }
                }else{
                    list.add(line);
                }

            }
            fr.close();
            reader.close();
            DataTableUntils.clearInfoForFile(file);
            for (int i = 0; i < list.size(); i++) {
                DataTableUntils.writeInToFile(file, list.get(i) + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向数据字典删除或添加属性
     * @param sentence
     * @return
     */
    public static boolean addOrDropCOLUMN(String sentence) {
        String tableName;
        List<Attribute> list = new ArrayList<>();
        if (RegularUntils.isValid(sentence, RegularExpression.ALTERADDCOLUMN) == true) {
            String getTableName = "table\\s+[A-Za-z_]+";
            tableName = RegularUntils.getValues(sentence, getTableName);
            tableName = tableName.replace(" ", "");
            tableName = tableName.substring(5, tableName.length());
            String getColumnName = "(COLUMN|column)\\s+[A-Za-z_]+";
            String columnName = RegularUntils.getValues(sentence, getColumnName);
            columnName = columnName.replace(" ", "");
            columnName = columnName.substring(6, columnName.length());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getAttributeName().equals(columnName)) {
                    System.out.println("Add fail ! The field is existed !");
                    return false;
                }
            }
            String getType = "(varchar|int|float|double)\\s*\\(\\s*[1-9][0-9]*\\s*\\)";
            String type = RegularUntils.getValues(sentence, getType);
            String getIsNULL = "(null|not null)";
            String isNull = RegularUntils.getValues(sentence, getIsNULL);
            File fileDataDictionary = new File(DatabaseUntils.dataDictionaryFilePath + tableName + ".txt");
            DataTableUntils.writeInToFile(fileDataDictionary, columnName + " " + type + " " + isNull + "\n");
            System.out.println("Query Ok !");
        } else if (RegularUntils.isValid(sentence, RegularExpression.ALTERDROPCOLUMN) == true) {
            String getTableName = "table\\s+[A-Za-z_]+";
            tableName = RegularUntils.getValues(sentence, getTableName);
            tableName = tableName.replace(" ", "");
            tableName = tableName.substring(5, tableName.length());
            String getColumnName = "(COLUMN|column)\\s+[A-Za-z_]+";
            String columnName = RegularUntils.getValues(sentence, getColumnName);
            columnName = columnName.replace(" ", "");
            columnName = columnName.substring(6, columnName.length());
            File fileDataDictionary = new File(DatabaseUntils.dataDictionaryFilePath + tableName + ".txt");
            delFromFile(fileDataDictionary, columnName);
            System.out.println("Query Ok !");
        } else {
            System.out.println("The sentence is error !");
            return false;
        }
        return false;
    }
    public static void main(String[] arg){
        DataTableUntils.descTableStructure("department");
    }
}
