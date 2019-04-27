package com.lionel.gonews.search;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lionel.gonews.base.BaseRemoteSourceViewModel;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.ErrorInfo;
import com.lionel.gonews.data.remote.QueryFilter;
import com.lionel.gonews.util.DateConvertManager;

import java.util.List;
import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_ISO8601;
import static com.lionel.gonews.util.Constants.DATE_YYYY_MM_DD;
import static com.lionel.gonews.util.Constants.RELEVANCY;

public class SearchViewModel extends BaseRemoteSourceViewModel {

    private QueryFilter.QueryEverythingFilter queryEverythingFilter;

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<News>> getNewsDataLiveData() {
        return super.getNewsDataLiveData();
    }

    public MutableLiveData<Integer> getTotalCountLiveData() {
        return super.getTotalSizeLiveData();
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return super.getIsLoadingLiveData();
    }

    public MutableLiveData<Boolean> getIsLastPageLiveData() {
        return super.getIsLastPageLiveData();
    }

    public MutableLiveData<ErrorInfo> getErrorInfoLiveData() {
        return super.getErrorInfoLiveData();
    }

    public void setQueryWord(String queryWord) {
        String cleanWord = replaceAllSpaceWitchPlus(queryWord);
        if (queryEverythingFilter == null) {
            queryEverythingFilter = new QueryFilter.QueryEverythingFilter(cleanWord, RELEVANCY, null, null);
        }
        queryEverythingFilter.queryWord = cleanWord;
        super.setQueryFilter(queryEverythingFilter);
    }

    public String replaceAllSpaceWitchPlus(String queryWord) {
        return queryWord.trim().replaceAll("\\s+", "+");
    }

    public void setSortBy(String sortBy) {
        if (queryEverythingFilter != null) {
            queryEverythingFilter.sortBy = sortBy;

            super.setQueryFilter(queryEverythingFilter);
        }
    }

    public void setDateRange(String dateFrom, String dateTo) {
        if (queryEverythingFilter != null) {
            queryEverythingFilter.dateFrom =
                    DateConvertManager.turnToSpecificPatternAndTimeZone(dateFrom, DATE_YYYY_MM_DD, TimeZone.getDefault(), DATE_ISO8601, TimeZone.getTimeZone("UTC"));

            //when query from remote source, the date range should end at (dateFrom+1d)
            //ex. 2019-4-23(local) should be 2019-4-23 16:00:00 (UTC)
            queryEverythingFilter.dateTo =
                    DateConvertManager.turnToSpecificPatternAndTimeZoneAndPlusDays(dateTo, DATE_YYYY_MM_DD, TimeZone.getDefault(), DATE_ISO8601, TimeZone.getTimeZone("UTC"), 1);

            super.setQueryFilter(queryEverythingFilter);
        }
    }

    /**
     * every query should without cache
     */
    public void initNewsWithoutCache() {
        super.initNewsWithoutCache();
    }

    public void loadMoreNews() {
        super.loadMoreNews();
    }
}
