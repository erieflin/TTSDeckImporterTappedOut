package core.Utils;

public class SelectBuilder{
    StringBuilder sb;
    String columnPrefixStr;
    public SelectBuilder(){
        this.reset();
    }

    public SelectBuilder addColumn(String column){
        sb.append(columnPrefixStr + column);
        if(columnPrefixStr.trim().equals("")){
            columnPrefixStr = ", ";
        }
        return this;
    }

    public SelectBuilder addColumnWithAlias(String column, String alias){
        this.addColumn(column);
        this.sb.append(" as ").append(alias);
        return this;
    }

    public SelectBuilder addColumnWithTableName(String tableName, String column){
        this.addColumn(tableName+"."+column);
        return this;
    }

    public SelectBuilder addColumnWithTableNameAndAlias(String tableName, String column, String alias){
        this.addColumnWithAlias(tableName+"."+column, alias);
        return this;
    }
    public SelectBuilder append(String s){
        sb.append(s);
        return this;
    }

    public String toString(){
        return sb.toString();
    }

    public void reset(){
        sb = new StringBuilder("Select");
        columnPrefixStr = " ";
    }
}
