package com.lionel.gonews.data.local.query_word;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static com.lionel.gonews.util.Constants.TABLE_QUERY_WORD;

/**
 * entity for query record
 */
@Entity(tableName = TABLE_QUERY_WORD, indices = @Index(value = {"word"}, unique = true))
public class QueryWord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String word;

    public QueryWord(String word) {
        this.word = word;
    }
}
