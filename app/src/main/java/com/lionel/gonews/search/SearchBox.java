package com.lionel.gonews.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lionel.gonews.R;
import com.lionel.gonews.data.local.query_word.QueryWord;
import com.lionel.gonews.util.KeyboardManager;

import java.util.ArrayList;
import java.util.List;

public class SearchBox extends FrameLayout {

    private final Context context;
    private AppCompatAutoCompleteTextView edtSearchBox;
    private ISearchBoxCallback callback;

    public interface ISearchBoxCallback {
        void startQuery(String queryWord);

        void onBackBtnPressed();

        void deleteAllQueryWord();
    }

    public SearchBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.search_box, this);

        this.context = context;

        initEdtSearchBox();
        initBtnBack();
        initBtnCancel();
    }

    public void setCallback(ISearchBoxCallback callback) {
        this.callback = callback;
    }

    public void setSearchHistory(List<QueryWord> data) {
        ArrayAdapter<String> adapter = new SearchHistoryAdapter(context,
                R.layout.item_search_box_history, R.id.txItemSearchHistory,
                toListStr(data),
                edtSearchBox);

        if (edtSearchBox != null) {
            edtSearchBox.setAdapter(adapter);
        }
    }

    private List<String> toListStr(List<QueryWord> data){
        List<String> listStr = new ArrayList<>();
        if(data!= null) {
            for(QueryWord queryWord : data) {
                listStr.add(queryWord.word);
            }
        }
        return listStr;
    }

    private void initEdtSearchBox() {
        edtSearchBox = findViewById(R.id.edtSearchBox);

        //show history list only once at beginning
        edtSearchBox.post(new Runnable() {
            @Override
            public void run() {
                edtSearchBox.showDropDown();
            }
        });

        // events of history list is pressed
        edtSearchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doSearch();
            }
        });

        //as search button on keyboard is pressed
        edtSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    KeyboardManager.hideKeyboard(context, v);
                    return true;
                }
                return false;
            }
        });
    }

    private void initBtnBack() {
        ImageButton btnBack = findViewById(R.id.imgBtnBack);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onBackBtnPressed();
                }
            }
        });
    }

    private void initBtnCancel() {
        ImageButton btnCancel = findViewById(R.id.imgBtnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearchBox.setText("");
            }
        });
    }

    private void doSearch() {
        String queryWord = edtSearchBox.getText().toString();
        if (callback != null) {
            edtSearchBox.clearFocus();
            callback.startQuery(queryWord);
        }
    }


    private class SearchHistoryAdapter extends ArrayAdapter<String> {
        private final int resource;
        private final int textViewResourceId;
        private final List<String> originalData;
        private List<String> data;
        private final View targetView;

        public SearchHistoryAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects, @NonNull View targetView) {
            super(context, resource, textViewResourceId, objects);

            this.resource = resource;
            this.textViewResourceId = textViewResourceId;
            this.originalData = objects;
            this.data = objects;
            this.targetView = targetView;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (convertView == null) {
                convertView = inflater.inflate(resource, parent, false);
            }
            TextView textView = convertView.findViewById(textViewResourceId);
            textView.setText(data.get(position));

            //hide keyboard if list is scrolling or touched
            convertView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        KeyboardManager.hideKeyboard(context, targetView);
                    }
                    return false;
                }
            });

            return convertView;
        }

        @Override
        public int getCount() {
            return data != null ? data.size() : 0;
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filteredResults = new FilterResults();
                    if (constraint != null && constraint.length() > 0) {
                        List<String> filter = new ArrayList<>();

                        for (String item : originalData) {
                            if (item.contains(constraint)) {
                                filter.add(item);
                            }
                        }
                        filteredResults.count = filter.size();
                        filteredResults.values = filter;
                    } else {
                        filteredResults.count = originalData.size();
                        filteredResults.values = originalData;
                    }
                    return filteredResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    data = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
}
