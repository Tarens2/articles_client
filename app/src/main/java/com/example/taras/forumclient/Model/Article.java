package com.example.taras.forumclient.Model;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by User on 17.12.2016.
 */


@Table(name = Article.TABLE_NAME, id = BaseColumns._ID)
public class Article extends Model{

    final static public String ID = "_id";
    final static public String TEXT = "text";
    final static public String TITLE = "title";
    final static public String USER_ID = "user_id";
    final static public String TABLE_NAME = "Articles";



    @Column(name=TEXT)
    private String text;

    @Column(name=TITLE)
    private String title;

    @Column(name=USER_ID)
    private String user_id;

    public String getText() {
        return text;
    }
    public Article(){
        super();
    }

    public Article(String text, String title, String user_id){
        super();
        this.text = text;
        this.title = title;
        this.user_id = title;
    }

    public static List<Article> getAll() {
        // This is how you execute a query
        return new Select()
                .from(Article.class)
                .execute();
    }

    //cursors
    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(Article.class).getTableName();
        // Query all items without any conditions
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                from(Article.class).toSql();
        // Execute query on the underlying ActiveAndroid SQLite database
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }

}
