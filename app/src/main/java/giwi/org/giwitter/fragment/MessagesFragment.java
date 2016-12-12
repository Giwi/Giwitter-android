package giwi.org.giwitter.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import giwi.org.giwitter.R;
import giwi.org.giwitter.adapter.MessageAdapter;
import giwi.org.giwitter.database.messages.MessagesDao;
import giwi.org.giwitter.helpers.MyRestClient;
import giwi.org.giwitter.helpers.NetworkHelper;
import giwi.org.giwitter.model.Message;


/**
 * The type Messages fragment.
 */
@EFragment(R.layout.fragment_messages)
public class MessagesFragment extends Fragment {

    @ViewById
    SwipeRefreshLayout messages_swiperefresh;
    @ViewById
    RecyclerView messages_list;
    MessageAdapter adapter;
    @RestService
    MyRestClient restClient;
    MessagesDao messagesDao;

    /**
     * Init.
     */
    @AfterViews
    void init() {
        messagesDao = new MessagesDao(getActivity());
        setupRefreshLayout();
        setupRecyclerView();
        loading(false);
    }

    /**
     * On resume.
     */
    @Override
    public void onResume() {
        super.onResume();
        loading(false);
    }

    /**
     * Load messages
     */
    private void loading(boolean force) {
        messages_swiperefresh.setRefreshing(true);
        getMessageList(force);
    }

    /**
     * Setup refresh layout
     */
    private void setupRefreshLayout() {
        messages_swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading(true);
            }
        });
        messages_swiperefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
    }

    /**
     * Gets message list.
     *
     * @param refresh the refresh
     */
    @Background
    void getMessageList(boolean refresh) {
        if (NetworkHelper.isInternetAvailable(getActivity()) && refresh) {
            try {
                JSONArray mess = new JSONArray(restClient.getMesages());
                List<Message> listOfMessages = new ArrayList<>();
                for (int i = 0; i < mess.length(); i++) {
                    JSONObject m = mess.getJSONObject(i);
                    Log.i("message", m.toString());
                    String username = "inconnu";
                    if (m.has("author") && m.optJSONObject("author") != null) {
                        username = m.getJSONObject("author").getString("username");
                    }
                    listOfMessages.add(new Message(username, m.getString("content"), m.getLong("date")));
                }
                messagesDao.writeMessages(listOfMessages);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        display(messagesDao.readMessages());
    }

    /**
     * Display.
     *
     * @param messages the messages
     */
    @UiThread
    void display(List<Message> messages) {
        if (messages != null) {
            adapter.addMessage(messages);
        }
        messages_swiperefresh.setRefreshing(false);
    }

    /**
     * Setup recycler view.
     */
    private void setupRecyclerView() {
        messages_list.setLayoutManager(new LinearLayoutManager(messages_list.getContext()));
        adapter = new MessageAdapter(this.getActivity());
        messages_list.setAdapter(adapter);

        // Add this. 
        // Two scroller could have problem. 
        messages_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                messages_list.setEnabled(topRowVerticalPosition >= 0);
            }
        });
    }

}
