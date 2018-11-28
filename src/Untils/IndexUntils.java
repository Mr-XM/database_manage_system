package Untils;

import Tree.BPlusTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IndexUntils {
    /**
     * 索引文件夹路径
     */
    public static String indexFolderPath="/Users/zhangyinghao/Desktop/index/";
    /**
     * 索引文件路径
     */
    public static String indexFilePath="/Users/zhangyinghao/Desktop/index/";

    /**
     * 创建索引
     * @param sentence
     * @return
     */
    public static boolean createIndex(String sentence){
        String getTableName="(table|TABLE)\\s+[A-Za-z_]+";
        String tableName=RegularUntils.getValues(sentence,getTableName);
        tableName=tableName.replace(" ","");
        tableName=tableName.substring(5,tableName.length());

        String getIndexName="(index|INDEX)\\s+[A-Za-z_]+";
        String indexName=RegularUntils.getValues(sentence,getIndexName);
        indexName=indexName.replace(" ","");
        indexName=indexName.substring(5,indexName.length());

        String getColumnName="\\(\\s*[A-Za-z_]+";
        String columnName=RegularUntils.getValues(sentence,getColumnName);
        columnName=columnName.replace(" ", "");
        columnName=columnName.substring(1,columnName.length());


        List<Attribute> list = DataDictionary.getDataDictionary(tableName);
        for(int i=0;i<list.size();i++){
            if(list.get(i).getAttributeName().equals(columnName)){
                String getWriteData="(index|INDEX)\\s+[A-Za-z_]+\\s*\\(\\s*[A-Za-z_]+\\s*\\)";
                String writeData=RegularUntils.getValues(sentence,getWriteData);
                writeData=writeData+"\n";
                File file = new File(DatabaseUntils.dataDictionaryFilePath + tableName + ".txt");
                DataTableUntils.writeInToFile(file,writeData);

                BPlusTree tree = new BPlusTree(3);
                File indexfile=new File(indexFilePath+tableName+"/"+indexName+".txt");
                if(!indexfile.exists()){
                    try {
                        indexfile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                indexfile=new File(indexFilePath+tableName+"/"+indexName+".txt");
                File datafile = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
                List<LinkedHashMap> listmap = DataTableUntils.getDataFromFile(datafile);
                for(int j=0;j<listmap.size();j++){
                    Iterator iter = listmap.get(j).entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        if(entry.getKey().equals(columnName)){
                            //String key=entry.getValue().toString();
                            int key=Integer.parseInt(entry.getValue().toString());
                            tree.insertOrUpdate(key,j);
                            int k=j+1;
                            DataTableUntils.writeInToFile(indexfile,key+"\t"+k+"\n");
                        }
                    }
                }
                BPlusTree.printBPlusTree(tree);
                return true;
            }
        }
        System.out.println("Create index fail,the column is not exist !");
        return false;
    }

    public static BPlusTree getIndexTree(File indexfile){
        BPlusTree tree = new BPlusTree(3);
        try {
            String line;
            FileReader fr = new FileReader(indexfile.getAbsoluteFile());
            BufferedReader reader = new BufferedReader(fr);
            while ((line = reader.readLine()) != null) {
                String dictionary[] = line.split("\t");
                for (int i = 0; i < dictionary.length; i++) {
                    tree.insertOrUpdate(dictionary[0],dictionary[1]);
                }
            }
            fr.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tree;
    }

    /**
     * 删除索引
     * @param sentence
     * @return
     */
    public static boolean deleteIndex(String sentence){
        String getTableName="(table|TABLE)\\s+[A-Za-z_]+";
        String tableName=RegularUntils.getValues(sentence,getTableName);
        tableName=tableName.replace(" ","");
        tableName=tableName.substring(5,tableName.length());

        String getIndexName="(index|INDEX)\\s+[A-Za-z_]+";
        String indexName=RegularUntils.getValues(sentence,getIndexName);
        indexName=indexName.replace(" ","");
        indexName=indexName.substring(5,indexName.length());

        File file = new File(DatabaseUntils.dataDictionaryFilePath + tableName + ".txt");
        DataDictionary.delFromFile(file,indexName);

        File indexfile=new File(indexFilePath+"/"+tableName+"/"+indexName+".txt");
        if (indexfile.exists()) {
            indexfile.delete();
        }else {
            System.out.println("The table not exits!");
            return false;
        }
        System.out.println("Query Ok !");
        return true;
    }

    /**
     * 查看索引
     * @param sentence
     */
    public static void showIndex(String sentence){
        String getTableName="(from|FROM)\\s+[A-Za-z_]+";
        String tableName=RegularUntils.getValues(sentence,getTableName);
        tableName=tableName.replace(" ","");
        tableName=tableName.substring(4,tableName.length());

        List<Attribute> list = DataDictionary.getDataDictionary(tableName);
        System.out.println("Table"+"\t\t"+"Non_unique"+"\t\t"+"key_name"+"\t\t"+"Column_name"+"\t");
        for(int i=0;i<list.size();i++){
            if(list.get(i).getPrimaryKey().equals("pri")){
                System.out.println(tableName+"\t\t"+"0"+"\t\t"+list.get(i).getAttributeName()+"\t\t"+list.get(i).getAttributeName()+"\t");
            }
            if(list.get(i).getKey().equals("mul")){
                System.out.println(tableName+"\t\t"+"1"+"\t\t"+list.get(i).getIndexName()+"\t\t"+list.get(i).getAttributeName()+"\t");
            }
        }
        System.out.println("Query Ok !");
    }

    public static boolean updateIndexFile(String tableName){
        List<Attribute> attributeList=DataDictionary.getDataDictionary(tableName);
        List<File> list=DataTableUntils.getFileList(indexFilePath+tableName+"/");
        File datafile = new File(DatabaseUntils.dataTablepath + tableName + ".txt");
        List<LinkedHashMap> listmap = DataTableUntils.getDataFromFile(datafile);

        for(int i=0;i<list.size();i++){
            String indexFileName=RegularUntils.getValues(list.get(i).getName(),"[A-Za-z_]+\\.");
            indexFileName=indexFileName.substring(0,indexFileName.length()-1);

            DataTableUntils.clearInfoForFile(list.get(i));
            for(int j=0;j<attributeList.size();j++){
                if(attributeList.get(j).getIndexName().equals(indexFileName)&&attributeList.get(j).getIndexName()!=null){
                    for(int k=0;k<listmap.size();k++){
                        Iterator iter = listmap.get(k).entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            if(entry.getKey().equals(attributeList.get(j).getAttributeName())){
                                String key=entry.getValue().toString();
                                int m=k+1;
                                DataTableUntils.writeInToFile(list.get(i),key+"\t"+m+"\n");
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
