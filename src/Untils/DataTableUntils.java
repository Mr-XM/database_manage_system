package Untils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTableUntils {
    /**
     * 创建表格
     *
     * @param dataTableName
     * @return
     */
    public static boolean createTable(String dataTableName) {
        File file = new File(DatabaseUntils.dataTablepath + dataTableName + ".txt");
        File fileDataDictionary = new File(DatabaseUntils.dataDictionaryFilePath + dataTableName + ".txt");
        File indexfile=new File(IndexUntils.indexFilePath + dataTableName);
        if (!file.exists() && !fileDataDictionary.exists()&&!indexfile.exists()) {
            try {
                indexfile.mkdir();
                file.createNewFile();
                fileDataDictionary.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 删除表格
     *
     * @param dataTableName
     * @return
     */
    public static boolean deleteTable(String dataTableName) {
        File file = new File(DatabaseUntils.dataTablepath + dataTableName + ".txt");
        File fileDataDictionary = new File(DatabaseUntils.dataDictionaryFilePath + dataTableName + ".txt");
        File indexfile = new File(IndexUntils.indexFilePath + dataTableName);
        if (file.exists() && fileDataDictionary.exists()) {
            fileDataDictionary.delete();
            file.delete();
            if(indexfile.exists()){
                indexfile.delete();
            }
            System.out.println("Query Ok !");
            return true;
        }else {
            System.out.println("Drop table fail !");
            return false;
        }
    }

    /**
     * 获取路径之下的全部文件
     *
     * @param strPath
     * @return
     */
    public static List<File> getFileList(String strPath) {
        List<File> filelist = new ArrayList<>();
        File dir = new File(strPath);
        File[] files = dir.listFiles();//获取该目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {//判断是文件好事文件夹
                    getFileList(files[i].getAbsolutePath());
                } else {
                    if (!files[i].getName().equals(".DS_Store")) {
                        filelist.add(files[i]);
                    }
                }
            }
        }
        return filelist;
    }

    /**
     * 打印表格数据
     *
     * @param list
     */
    public static void printList(List<LinkedHashMap> list) {
        for (int i = 0; i < list.size(); i++) {
            Iterator iter = list.get(i).entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                System.out.print(entry.getValue() + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 打印符合条件的表格数据
     * @param list
     * @param setCondition
     */
    public static void printList(List<LinkedHashMap> list, Set<String> setCondition) {
        for (int i = 0; i < list.size(); i++) {
            Iterator iter = list.get(i).entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (setCondition.contains(entry.getKey().toString()) == true) {
                    System.out.print(entry.getValue() + "\t");
                }
            }
            System.out.println();
        }
    }

    /**
     * 从表格中删除数据
     * @param sentence
     * @return
     */
    public static boolean selectFromTable(String sentence) {
        if (RegularUntils.isValid(sentence, RegularExpression.SELECTDATA) == true) {
            String getColumn = "(select|SELECT)\\s+(\\*|([A-Za-z_]+\\s*,\\s*)*[A-Za-z_]+)";
            String column = RegularUntils.getValues(sentence, getColumn);
            column = column.replace(" ", "");
            column = column.substring(6, column.length());
            String[] word = column.split(",");
            Set<String> set = new HashSet<>();
            for (int i = 0; i < word.length; i++) {
                set.add(word[i]);
            }
            String getTableName = "[A-Za-z_]+\\s*;";
            String tableName = RegularUntils.getValues(sentence, getTableName);
            tableName = RegularUntils.getValues(tableName, "[A-Za-z_]+");
            File file = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
            List<LinkedHashMap> list = getDataFromFile(file);
            if (word[0].equals("*")) {
                printList(list);
            } else {
                printList(list, set);
            }
            System.out.println("Query Ok !");
        } else if (RegularUntils.isValid(sentence, RegularExpression.SELECTDATAWHERE) == true) {
            String getColumn = "(select|SELECT)\\s+([A-Za-z_]+\\s*,\\s*)*(\\*|[A-Za-z_]+)";
            String column = RegularUntils.getValues(sentence, getColumn);
            column = column.replace(" ", "");
            column = column.substring(6, column.length());
            String[] word = column.split(",");
            Set<String> set = new HashSet<>();
            for (int i = 0; i < word.length; i++) {
                set.add(word[i]);
            }
            String getTableName = "(from|FROM)\\s+[A-Za-z_]+";
            String tableName = RegularUntils.getValues(sentence, getTableName);
            tableName=tableName.replace(" ","");
            tableName=tableName.substring(4,tableName.length());
            File file = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
            String getkey = "(where|WHERE)\\s+[A-Za-z_]+";
            String key = RegularUntils.getValues(sentence, getkey);
            key = key.replace(" ", "");
            key = key.substring(5, key.length());
            String getValue = "=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)";
            String value = RegularUntils.getValues(sentence, getValue);
            value = value.replace(" ", "");
            value = value.substring(1, value.length());
            List<LinkedHashMap> linkedHashMap = getDataFromFile(file);
            List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
            for (int i = 0; i < linkedHashMap.size(); i++) {
                Iterator iter = linkedHashMap.get(i).entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    if (key.equals(entry.getKey().toString()) && value.equals(entry.getValue().toString())) {
                        linkedHashMapList.add(linkedHashMap.get(i));
                    }
                }
            }
            if (word[0].equals("*")) {
                printList(linkedHashMapList);
            } else {
                printList(linkedHashMapList, set);
            }
            System.out.println("Query Ok !");
        } else {
            System.out.println("The sentence error !");
        }
        return false;
    }

    /**
     * 从文件获取内容
     *
     * @param file
     * @return
     */
    public static List<LinkedHashMap> getDataFromFile(File file) {
        List<LinkedHashMap> list = new ArrayList<>();
        try {
            String getFileName = "[A-Za-z_]+\\.";
            String fileName = RegularUntils.getValues(file.getName(), getFileName);
            fileName = fileName.substring(0, fileName.length() - 1);
            List<Attribute> attributes = DataDictionary.getDataDictionary(fileName);
            String line;
            FileReader fr = new FileReader(file.getAbsoluteFile());
            BufferedReader reader = new BufferedReader(fr);
            while ((line = reader.readLine()) != null) {
                String dictionary[] = line.split(" ");
                LinkedHashMap<String, String> data = new LinkedHashMap<>();
                for (int i = 0; i < attributes.size(); i++) {
                    data.put(attributes.get(i).getAttributeName(), dictionary[i]);
                }
                list.add(data);
            }
            fr.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 向文件写入内容
     *
     * @param file
     * @param writeData
     */
    public static void writeInToFile(File file, String writeData) {
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(writeData);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 清空文件
     *
     * @param file
     */
    public static void clearInfoForFile(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示表结构
     *
     * @param tableName
     */
    public static void descTableStructure(String tableName) {
        List<Attribute> listDictionary = DataDictionary.getDataDictionary(tableName);
        if (listDictionary.isEmpty()) {
            System.out.println("The table '" + tableName + "' is not exist !");
        } else {
            System.out.println("Field" + "\t" + "Type" + "\t" + "NotNull" + "\t" + "PrimaryKey" + "\t"+"Key");
            for (int i = 0; i < listDictionary.size(); i++) {
                System.out.println(listDictionary.get(i).getAttributeName() + "\t" + listDictionary.get(i).getAttributeType() + "(" + listDictionary.get(i).getAttributeLength() + ")\t" + listDictionary.get(i).isNoEmpty + "\t" + listDictionary.get(i).getPrimaryKey()+"\t"+listDictionary.get(i).getKey());
            }
        }
    }

    /**
     * 向文件插入一行数据
     *
     * @param sentence
     * @return
     */
    public static boolean insertValue(String sentence) {
        String line = "";
        String tableName = "";
        String getTableName = "into\\s+([A-Za-z_]+,)*[A-Za-z_]+";
        String getInsertValue = "(values|VALUES)\\s*\\(\\s*(('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+),)*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)\\s*\\)";
        Pattern p1 = Pattern.compile(getTableName);
        Matcher m1 = p1.matcher(sentence);
        while (m1.find()) {
            String temp = m1.group().replace(" ", "");
            int lengh = temp.length();
            temp = temp.substring(4, lengh);
            tableName = temp;
        }
        File file = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
        if (file.exists()) {
            Pattern p2 = Pattern.compile(getInsertValue);
            Matcher m2 = p2.matcher(sentence);
            while (m2.find()) {
                List<Attribute> list = DataDictionary.getDataDictionary(tableName);
                String temp = m2.group().replace(" ", "");
                temp = temp.substring(7, temp.length() - 1);
                temp = temp.replace("\'", "");
                String values[] = temp.split(",");
                for (int i = 0; i < values.length; i++) {
                    if (list.get(i).getAttributeType().equals("int")) {
                        try {
                            Integer.parseInt(values[i]);
                            if (values[i].length() <= list.get(i).getAttributeLength()) {
                                line = line + values[i] + " ";
                            } else {
                                System.out.println("'" + values[i] + "' too long !");
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("'" + values[i] + "' The type error !");
                            return false;
                        }
                    } else {
                        if (values[i].length() <= list.get(i).getAttributeLength()) {
                            line = line + values[i] + " ";
                        } else {
                            System.out.println("'" + values[i] + "' too long !");
                            return false;
                        }
                    }
                }
                List<LinkedHashMap> linkedHashMaps = getDataFromFile(file);
                if (!linkedHashMaps.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getKey().equals("pri")) {
                            for (int j = 0; j < linkedHashMaps.size(); j++) {
                                Iterator iter = linkedHashMaps.get(j).entrySet().iterator();
                                while (iter.hasNext()) {
                                    Map.Entry entry = (Map.Entry) iter.next();
                                    if (entry.getKey().toString().equals(list.get(i).getAttributeName()) && entry.getValue().toString().equals(values[i])) {
                                        System.out.println("The primary key value is existed !");
                                        return false;
                                    }
                                }
                            }
                        }

                    }
                }
            }
            line = line + "\n";
            writeInToFile(file, line);
            IndexUntils.updateIndexFile(tableName);
            System.out.println("Query Ok !");
            return true;
        } else {
            System.out.println("The table is not exists!");
            return false;
        }

    }

    /**
     * 删除数据
     *
     * @param file
     * @param key
     * @param value
     * @return
     */
    public static boolean deleteDataFromTable(File file, String key, String value) {
        List<LinkedHashMap> linkedHashMap = getDataFromFile(file);
        for (int i = 0; i < linkedHashMap.size(); i++) {
            Iterator iter = linkedHashMap.get(i).entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (key.equals(entry.getKey().toString()) && value.equals(entry.getValue().toString())) {
                    linkedHashMap.remove(i);
                }
            }
        }
        clearInfoForFile(file);
        if (!linkedHashMap.isEmpty()) {
            String line = "";
            for (int i = 0; i < linkedHashMap.size(); i++) {
                Iterator iter = linkedHashMap.get(i).entrySet().iterator();
                line = "";
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    line = line + entry.getValue().toString() + " ";
                }
                line = line + "\n";
                writeInToFile(file, line);
            }
        }
        return true;

    }

    public static void updateFromTable(File file, String key1, String value1, String key2, String value2) {
        if (key2.isEmpty() && value2.isEmpty()) {
            List<LinkedHashMap> linkedHashMap = getDataFromFile(file);
            String getFileName = "[A-Za-z_]+\\.";
            String fileName = RegularUntils.getValues(file.getName(), getFileName);
            fileName = fileName.substring(0, fileName.length() - 1);
            for (int i = 0; i < linkedHashMap.size(); i++) {
                Iterator iter = linkedHashMap.get(i).entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    if (key1.equals(entry.getKey().toString())) {
                        entry.setValue(value1);
                    }
                }
            }
            clearInfoForFile(file);
            if (!linkedHashMap.isEmpty()) {
                String line = "";
                for (int i = 0; i < linkedHashMap.size(); i++) {
                    Iterator iter = linkedHashMap.get(i).entrySet().iterator();
                    line = "";
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        line = line + entry.getValue().toString() + " ";
                    }
                    line = line + "\n";
                    writeInToFile(file, line);
                }
            }
        } else {
            List<LinkedHashMap> linkedHashMap1 = new ArrayList<>();
            List<LinkedHashMap> linkedHashMap = getDataFromFile(file);
            String getFileName = "[A-Za-z_]+\\.";
            String fileName = RegularUntils.getValues(file.getName(), getFileName);
            fileName = fileName.substring(0, fileName.length() - 1);
            for (int i = 0; i < linkedHashMap.size(); i++) {
                Iterator iter = linkedHashMap.get(i).entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    if (value2.equals(entry.getValue().toString())) {
                        linkedHashMap1.add(linkedHashMap.get(i));
                    }
                }
            }
            for (int i = 0; i < linkedHashMap1.size(); i++) {
                Iterator iter = linkedHashMap1.get(i).entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    if (key1.equals(entry.getKey().toString())) {
                        entry.setValue(value1);
                    }
                }
            }
            clearInfoForFile(file);
            if (!linkedHashMap.isEmpty()) {
                String line = "";
                for (int i = 0; i < linkedHashMap.size(); i++) {
                    Iterator iter = linkedHashMap.get(i).entrySet().iterator();
                    line = "";
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        line = line + entry.getValue().toString() + " ";
                    }
                    line = line + "\n";
                    writeInToFile(file, line);
                }
            }
        }
    }

    /**
     * delete语句操作
     *
     * @param sentence
     */
    public static void deleteSentence(String sentence) {
        if (RegularUntils.isValid(sentence, RegularExpression.DELETEDATA)) {
            String getTableName = "(FROM|from)\\s+[A-Za-z_]+";
            String tableName = RegularUntils.getValues(sentence, getTableName);
            tableName = tableName.replace(" ", "");
            tableName = tableName.substring(4, tableName.length());
            File file = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
            List<LinkedHashMap> linkedHashMap = getDataFromFile(file);
            for (int i = 0; i < linkedHashMap.size(); i++) {
                Iterator iter = linkedHashMap.get(i).entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    deleteDataFromTable(file, entry.getKey().toString(), entry.getValue().toString());
                }
            }
            IndexUntils.updateIndexFile(tableName);
            System.out.println("Query Ok !");
        } else if (RegularUntils.isValid(sentence, RegularExpression.DELETEDATAWHERE)) {
            String getTableName = "(FROM|from)\\s+[A-Za-z_]+";
            String tableName = RegularUntils.getValues(sentence, getTableName);
            tableName = tableName.replace(" ", "");
            tableName = tableName.substring(4, tableName.length());
            File file = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
            List<LinkedHashMap> linkedHashMap = getDataFromFile(file);
            String getkey = "(where|WHERE)\\s+[A-Za-z_]+";
            String key = RegularUntils.getValues(sentence, getkey);
            key = key.replace(" ", "");
            key = key.substring(5, key.length());
            String getValue = "=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)";
            String value = RegularUntils.getValues(sentence, getValue);
            value = value.replace(" ", "");
            value = value.substring(1, value.length());
            //System.out.println(getkey + value);
            for (int i = 0; i < linkedHashMap.size(); i++) {
                Iterator iter = linkedHashMap.get(i).entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    if (key.equals(entry.getKey().toString()) && value.equals(entry.getValue().toString())) {
                        deleteDataFromTable(file, entry.getKey().toString(), entry.getValue().toString());
                    }
                }
            }
            IndexUntils.updateIndexFile(tableName);
            System.out.println("Query Ok !");
        } else {
            System.out.println("The input sentence error !");
        }

    }


    /**
     * 更新语句操作
     * @param sentence
     * @return
     */
    public static boolean updateSentence(String sentence) {
        if (RegularUntils.isValid(sentence, RegularExpression.UPDATE)) {
            String getTableName = "(update|UPDATE)\\s+[A-Za-z_]+";
            String tableName = RegularUntils.getValues(sentence, getTableName);
            tableName = tableName.replace(" ", "");
            tableName = tableName.substring(6, tableName.length());
            String getKey = "(set|SET)\\s+[A-Za-z_]+";
            String key = RegularUntils.getValues(sentence, getKey);
            key = key.replace(" ", "");
            key = key.substring(3, key.length());
            String getValue = "=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)";
            String value = RegularUntils.getValues(sentence, getValue);
            value = value.replace(" ", "");
            value = value.replace("'", "");
            value = value.substring(1, value.length());
            File file = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
            updateFromTable(file, key, value, "", "");
            IndexUntils.updateIndexFile(tableName);
            IndexUntils.updateIndexFile(tableName);
            System.out.println("Query Ok !");
        } else if (RegularUntils.isValid(sentence, RegularExpression.UPDATEWHRER)) {
            String getTableName = "(update|UPDATE)\\s+[A-Za-z_]+";
            String tableName = RegularUntils.getValues(sentence, getTableName);
            tableName = tableName.replace(" ", "");
            tableName = tableName.substring(6, tableName.length());
            String getKey = "(set|SET)\\s+[A-Za-z_]+";
            String key = RegularUntils.getValues(sentence, getKey);
            key = key.replace(" ", "");
            key = key.substring(3, key.length());
            String getValue = "=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)\\s+(where|WHERE)";
            String value = RegularUntils.getValues(sentence, getValue);
            value = value.replace(" ", "");
            value = value.replace("'", "");
            value = value.substring(1, value.length() - 5);
            String getKey2 = "(where|WHERE)\\s+[A-Za-z_]+";
            String key2 = RegularUntils.getValues(sentence, getKey2);
            key2 = key2.replace(" ", "");
            key2 = key2.substring(5, key2.length());
            String getValue2 = "(where|WHERE)\\s+[A-Za-z_]+\\s*=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)";
            String value2 = RegularUntils.getValues(sentence, getValue2);
            value2 = RegularUntils.getValues(value2, "=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)");
            value2 = value2.replace(" ", "");
            value2 = value2.replace("'", "");
            value2 = value2.substring(1, value2.length());
            File file = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
            updateFromTable(file, key, value, key2, value2);
            IndexUntils.updateIndexFile(tableName);
            System.out.println("Query Ok !");
        } else {
            System.out.println("The sentence is error !");
        }
        return false;
    }
}
