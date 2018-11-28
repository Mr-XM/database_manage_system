package Untils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUntils {
    public static String databasePath ="/Users/zhangyinghao/Desktop/Database_Manage_System/";
    public static String dataTablepath ="/Users/zhangyinghao/Desktop/Database_Manage_System/";
    public static String dataDictionaryFolderPath ="/Users/zhangyinghao/Desktop/DataDictionary/";
    public static String dataDictionaryFilePath ="/Users/zhangyinghao/Desktop/DataDictionary/";

    public static boolean creatDatabase(String databaseName){
        File file=new File(databasePath +databaseName);
        File fileDataDictionary=new File(dataDictionaryFolderPath +databaseName);
        File indexfile=new File(IndexUntils.indexFolderPath +databaseName);
        if(!file.exists()&&!fileDataDictionary.exists()&&!indexfile.exists()){//如果文件夹不存在
            file.mkdir();//创建文件夹
            fileDataDictionary.mkdir();
            indexfile.mkdir();
            System.out.println("database create success");
            return true;
        }else {
            System.out.println("This database existed !");
            return false;
        }
    }

    /**
     * 删除数据库
     * @param databaseName
     * @return
     */
    public static boolean deleteDatabase(String databaseName){
        File file=new File(databasePath +databaseName);
        File fileDataDictionary=new File(dataDictionaryFolderPath +databaseName);
        File indexfile=new File(IndexUntils.indexFolderPath +databaseName);
        if (file.exists()&&fileDataDictionary.exists()&&indexfile.exists()) {
            file.delete();
            fileDataDictionary.delete();
            indexfile.delete();
            System.out.println("Query Ok !");
            return true;
        }
        System.out.println("Drop database fail !");
        return  false;
    }

    /**
     * 进入数据库
     * @param databaseName
     * @return
     */
    public static boolean useDatabase(String databaseName){
        File file=new File(DatabaseUntils.databasePath+databaseName);
        if(file.exists()){
            dataTablepath = databasePath +"/"+databaseName+"/";
            dataDictionaryFilePath=dataDictionaryFolderPath+"/"+databaseName+"/";
            IndexUntils.indexFilePath=IndexUntils.indexFolderPath+"/"+databaseName+"/";
            return  true;
        }else{
            return false;
        }
    }

    /**
     * 获取文件列表
     * @param strPath
     * @return
     */
    public static List<File> getFileList(String strPath){
        List<File> filelist =new ArrayList<>();
        File dir =new File(strPath);
        File[] files=dir.listFiles();//获取该目录下文件全部放入数组
        if(files!=null){
            for (int i=0;i<files.length;i++){
                //String fileName =files[i].getName();
                if(files[i].isDirectory()){//判断是文件好事文件夹
                    getFileList(files[i].getAbsolutePath());
                    filelist.add(files[i]);
                }
            }
        }
        return filelist;
    }
}
