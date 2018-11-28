package Untils;

import java.util.SplittableRandom;

public class Attribute {
    /**
     * 属性名
     */
    public String attributeName;

    /**
     * 属性类型
     */
    public String attributeType;

    /**
     * 属性长度
     */
    public int attributeLength;

    /**
     * 键值类型
     */
    public String key="";

    /**
     * 主键
     */
    public String primaryKey="";

    /**
     * 索引名字
     */
    public String indexName="";

    /**
     * 是否能为空
     */
    public boolean isNoEmpty;

    public String getPrimaryKey() {
        return primaryKey;
    }

    public int getAttributeLength() {
        return attributeLength;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public boolean isNoEmpty() {
        return isNoEmpty;
    }

    public String getKey() {
        return key;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setNoEmpty(boolean noEmpty) {
        isNoEmpty = noEmpty;
    }

    public void setAttributeLength(int attributeLength) {
        this.attributeLength = attributeLength;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }
}
