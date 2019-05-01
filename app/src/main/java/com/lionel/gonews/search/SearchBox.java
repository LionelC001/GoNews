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
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lionel.gonews.R;
import com.lionel.gonews.util.DialogManager;
import com.lionel.gonews.util.KeyboardManager;

import java.util.ArrayList;
import java.util.List;

public class SearchBox extends FrameLayout implements DialogManager.IDialogCallback {

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

        //as search button on keyboard is pressed, hide keyboard
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

        edtSearchBox.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtSearchBox.showDropDown();
                }
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

    public void setCallback(ISearchBoxCallback callback) {
        this.callback = callback;
    }

    public void setSearchHistory(List<String> data) {
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(data);

        if (edtSearchBox != null) {
            edtSearchBox.setAdapter(adapter);
        }
    }

    private void doSearch() {
        String queryWord = edtSearchBox.getText().toString();
        if (callback != null) {
            edtSearchBox.clearFocus();
            callback.startQuery(queryWord);
        }
    }

    @Override
    public void onDialogPositiveButtonClick() {
        callback.deleteAllQueryWord();
    }

    private class SearchHistoryAdapter extends BaseAdapter implements Filterable {
        private static final int TYPE_QUERY_WORD = 1;
        private static final int TYPE_CLEAR = 2;

        private final List<String> originalData;
        private List<String> currentData;

        private SearchHistoryAdapter(List<String> data) {
            this.originalData = data;
            this.currentData = data;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;

            // don't use viewHolder mode, because it's a big trouble to handle different ItemViewType here.
            if (getItemViewType(position) == TYPE_QUERY_WORD) {
                view = bindQueryWordHolder(position, parent);
            } else {
                view = bindClearHolder(parent);
            }

            //hide keyboard if list is scrolling or touched
            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        KeyboardManager.hideKeyboard(context, edtSearchBox);
                    }
                    return false;
                }
            });

            return view;
        }

        private View bindQueryWordHolder(int position, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_search_box_history, null);
            TextView txtSearch = view.findViewById(R.id.txItemSearchHistory);
            txtSearch.setText(currentData.get(position));
            return view;
        }

        private View bindClearHolder(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_search_box_clear, null);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogManager.showAsking(context, SearchBox.this, context.getString(R.string.tip_dialog_clear_all_query));
                    edtSearchBox.clearFocus();
                }
            });
            return view;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < currentData.size()) {
                return TYPE_QUERY_WORD;
            } else {
                return TYPE_CLEAR;
            }
        }

        @Override
        public int getCount() {
            if (currentData != null && currentData.size() > 0 && currentData.size() == originalData.size()) {
                return currentData.size() + 1;  // +1 for the item "clear all"
            }
            if (currentData != null && currentData.size() > 0) {
                return currentData.size();
            }
            return 0;
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return position < currentData.size() ? currentData.get(position) : "clear all";  // null for the item "clear all"
        }

        @Override
        public long getItemId(int position) {
            return 0;
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
                    currentData = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
}
