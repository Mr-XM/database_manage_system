package Untils;

public interface RegularExpression {
    /**
     *
     */
    public static String LOGIN="(mysql|MYSQL)\\s+-(u|U)\\s+[A-Za-z]+\\s+-(p|P)\\s+[A-Za-z0-9]+\\s*";
    /**
     * 显示表格
     */
    public static String SHOWTABLE="(show|SHOW)\\s+(tables|TABLES)\\s*;";

    /**
     * 创建表格
     */
    //public static String CREATETABLE="(create|CREATE)\\s+(table|TABLE)\\s+[A-Za-z_]+\\s*\\(\\s*([A-Za-z_]+\\s+(int|double|varchar|float)\\s*\\(\\s*[1-9][0-9]*\\s*\\)\\s+(not null|null),\\s*)*([A-Za-z_]+\\s+(int|double|varchar|float)\\s*\\(\\s*[1-9][0-9]*\\s*\\)\\s+(not null|null)\\))\\s*;";
    public static String CREATETABLE="(create|CREATE)\\s+(table|TABLE)\\s+[A-Za-z_]+\\s*\\(\\s*([A-Za-z_]+\\s+(int|double|varchar|float)\\s*\\(\\s*[1-9][0-9]*\\s*\\)\\s+(not null|null),\\s*)*([A-Za-z_]+\\s+(int|double|varchar|float)\\s*\\(\\s*[1-9][0-9]*\\s*\\)\\s+(not null|null)(((\\s*,\\s*primary key)|(\\s*,\\s*PRIMARY KEY))\\([A-Za-z_]+\\))?\\))\\s*;";

    /**
     *  删除表格
     */
    public static String DROPTABLE="(drop|DROP)\\s+(table|TABLE)\\s+[A-Za-z_]+\\s*;";

    /**
     *显示数据库
     */
    public static String SHOWDATABASE="(show|SHOW)\\s+(databases|DATABASES)\\s*;";

    /**
     * 创建数据库
     */
    public static String CREATEDATABASE="(create|CREATE)\\s+(database|DATABASE)\\s+[A-Za-z_]+\\s*;";

    /**
     * 删除数据库
     */
    public static String DROPDATABASE="(drop|DROP)\\s+(database|DATABASE)\\s+[A-Za-z_]+\\s*;";

    /**
     * 使用数据库
     */
    public static String USEDATABASE="(use|USE)\\s+[A-Za-z_]+\\s*;";

    /**
     * 添加属性
     */
    public static String ALTERADDCOLUMN ="(alter|ALTER)\\s+(table|TABLE)\\s+[A-Za-z_]+\\s+(add|ADD)\\s+(COLUMN|column)\\s+[A-Za-z_]+\\s+(int|varchar|float|double)\\s*\\([1-9][0-9]?\\)\\s+(not null|null)\\s*;";

    /**
     * 删除属性
     */
    public static String ALTERDROPCOLUMN ="(alter|ALTER)\\s+(table|TABLE)\\s+[A-Za-z_]+\\s+(drop|DROP)\\s+(COLUMN|column)\\s+[A-Za-z_]+\\s*;";

    /**
     * 向表格插入数据
     */
    public static String INSERTDATA="(insert|INSERT)\\s+(into|INTO)\\s+[A-Za-z_]+\\s+(values|VALUES)\\s*\\(\\s*(('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+),)*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)\\s*\\)\\s*;";

    /**
     *带where的删除语句
     */
    public static String DELETEDATAWHERE="(delete|DELETE)\\s+(from|FROM)\\s+[A-Za-z_]+\\s+(where|WHERE)\\s+[A-Za-z_]+\\s*=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)\\s*;";

    /**
     *不带where的删除语句
     */
    public static String DELETEDATA="(delete|DELETE)\\s+(from|FROM)\\s+[A-Za-z_]+\\s*;";

    /**
     * 带where的选择查询
     */
    public static String SELECTDATAWHERE="(select|SELECT)\\s+([A-Za-z_]+\\s*,\\s*)*(\\*|[A-Za-z_]+)\\s+(from|FROM)\\s+([A-Za-z_]+\\s*,\\s*)*[A-Za-z_]+\\s+(where|WHERE)\\s+[A-Za-z_]+\\s*(=|<|>|(and|AND))\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)\\s*;";

    /**
     * 不带where的选择查询
     */
    public static String SELECTDATA="(select|SELECT)\\s+([A-Za-z_]+\\s*,\\s*)*(\\*|[A-Za-z_]+)\\s+(from|FROM)\\s+[A-Za-z_]+\\s*;";

    /**
     * 不带where的更新语句
     */
    public static String UPDATE="(update|UPDATE)\\s+[A-Za-z_]+\\s+(set|SET)\\s+[A-Za-z_]+\\s*=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)\\s*;";

    /**
     * 带where的更新语句
     */
    public static String UPDATEWHRER="(update|UPDATE)\\s+[A-Za-z_]+\\s+(set|SET)\\s+[A-Za-z_]+\\s*=\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)\\s+(where|WHERE)\\s+[A-Za-z_]+\\s*(=|>|<)\\s*('\\w+'|'[\\u4e00-\\u9fa5]+'|[0-9]+)\\s*;";

}
