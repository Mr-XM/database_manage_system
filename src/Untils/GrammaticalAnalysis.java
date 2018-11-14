package Untils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammaticalAnalysis {
    public static Map<String, Boolean> getKeywod() {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put(Keywords.ALTER, true);
        map.put(Keywords.CREATE, true);
        map.put(Keywords.DROP, true);
        map.put(Keywords.INSERT, true);
        map.put(Keywords.SELECT, true);
        map.put(Keywords.UPDATE, true);
        map.put(Keywords.USE, true);
        map.put(Keywords.SHOW, true);
        map.put(Keywords.DELETE, true);
        map.put(Keywords.DESCRIBE,true);
        return map;
    }

    public static void grammaticlAnalysis(String sentence) {
        int i = 0;
        String temp;
        Map<String, Boolean> map = getKeywod();
        while (i < sentence.length()) {
            temp = "";
            while ((sentence.charAt(i) >= 'a' && sentence.charAt(i) <= 'z') || sentence.charAt(i) >= 'A' && sentence.charAt(i) <= 'Z' && i < sentence.length()) {
                temp = temp + sentence.charAt(i);
                i++;
            }
            i++;
            if (map.containsKey(temp)) {
                if (temp.equals(Keywords.CREATE)||temp.equals(Keywords.CREATE.toUpperCase())) {
                    if (RegularUntils.isValid(sentence, RegularExpression.CREATEDATABASE) == true) {
                        String getdatabaseName = "[A-Za-z_]+\\s*;";
                        String databaseName = RegularUntils.getValues(sentence, getdatabaseName);
                        databaseName = RegularUntils.getValues(databaseName, "[A-Za-z_]+");
                        DatabaseUntils.creatDatabase(databaseName);
                        break;
                    } else if (RegularUntils.isValid(sentence, RegularExpression.CREATETABLE) == true) {
                        DataDictionary.createDataDictionary(sentence);
                        break;
                    } else {
                        System.out.println("The input sentence error !");
                        break;
                    }

                } else if (temp.equals(Keywords.DROP)||temp.equals(Keywords.DROP.toUpperCase())) {
                    if (RegularUntils.isValid(sentence, RegularExpression.DROPDATABASE) == true) {
                        String getdatabaseName = "[A-Za-z_]+\\s*;";
                        String databaseName = RegularUntils.getValues(sentence, getdatabaseName);
                        databaseName = RegularUntils.getValues(databaseName, "[A-Za-z_]+");
                        DatabaseUntils.deleteDatabase(databaseName);
                        break;
                    } else if (RegularUntils.isValid(sentence, RegularExpression.DROPTABLE) == true) {
                        String getTableName = "[A-Za-z_]+\\s*;";
                        String tableName = RegularUntils.getValues(sentence, getTableName);
                        tableName = RegularUntils.getValues(tableName, "[A-Za-z_]+");
                        DataTableUntils.deleteTable(tableName);
                        break;
                    } else {
                        System.out.println("The input sentence error !");
                        break;
                    }
                } else if (temp.equals(Keywords.INSERT)||temp.equals(Keywords.INSERT.toUpperCase())) {
                    if (RegularUntils.isValid(sentence, RegularExpression.INSERTDATA) == true) {
                        DataTableUntils.insertValue(sentence);
                        break;
                    } else {
                        System.out.println("The input sentence error !");
                        break;
                    }
                } else if (temp.equals(Keywords.USE)||temp.equals(Keywords.USE.toUpperCase())) {
                    if (RegularUntils.isValid(sentence, RegularExpression.USEDATABASE) == true) {
                        String getdatabaseName = "[A-Za-z_]+\\s*;";
                        String databaseName = RegularUntils.getValues(sentence, getdatabaseName);
                        databaseName = RegularUntils.getValues(databaseName, "[A-Za-z_]+");
                        if(DatabaseUntils.useDatabase(databaseName)==true){
                            System.out.println("Database Changed !");
                            break;
                        }else{
                            System.out.println("The input sentence error !");
                            break;
                        }
                    } else {
                        System.out.println("The input sentence error !");
                        break;
                    }
                } else if (temp.equals(Keywords.SHOW)||temp.equals(Keywords.SHOW.toUpperCase())) {
                    if (RegularUntils.isValid(sentence, RegularExpression.SHOWDATABASE) == true) {
                        List<File> fileList = DatabaseUntils.getFileList(DatabaseUntils.databasePath);
                        for (int j = 0; j < fileList.size(); j++) {
                            System.out.println(fileList.get(j).getName());
                        }
                        break;
                    } else if (RegularUntils.isValid(sentence, RegularExpression.SHOWTABLE) == true) {
                        List<File> fileList = DataTableUntils.getFileList(DatabaseUntils.dataTablepath);
                        String name="";
                        int length;
                        for (int j = 0;j < fileList.size(); j++) {
                            name=fileList.get(j).getName();
                            name=RegularUntils.getValues(name,"[A-Za-z_]+\\.");
                            length=name.length()-1;
                            name=name.substring(0,length);
                            System.out.println(name);
                        }
                        break;
                    } else {
                        System.out.println("The input sentence error !");
                        break;
                    }


                } else if (temp.equals(Keywords.SELECT)||temp.equals(Keywords.SELECT.toUpperCase())) {
                    DataTableUntils.selectFromTable(sentence);
                    break;
                } else if (temp.equals(Keywords.ALTER)||temp.equals(Keywords.ALTER.toUpperCase())) {
                    DataDictionary.addOrDropCOLUMN(sentence);
                    break;
                } else if (temp.equals(Keywords.DELETE)||temp.equals(Keywords.DELETE.toUpperCase())) {
                    DataTableUntils.deleteSentence(sentence);
                    break;
                } else if (temp.equals(Keywords.UPDATE)||temp.equals(Keywords.UPDATE.toUpperCase())) {
                    DataTableUntils.updateSentence(sentence);
                    break;
                } else if (temp.equals(Keywords.DESCRIBE)||temp.equals(Keywords.DESCRIBE.toUpperCase())) {
                    String getTableName = "[A-Za-z_]+\\s*;";
                    String tableName = RegularUntils.getValues(sentence, getTableName);
                    tableName = RegularUntils.getValues(tableName, "[A-Za-z_]+");
                    DataTableUntils.descTableStructure(tableName);
                    break;
                } else {
                    System.out.println("The input sentence error !");
                    break;
                }
            }else{
                System.out.println("The input sentence error !");
                break;
            }
        }
    }
}
