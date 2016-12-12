package giwi.org.giwitter.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giwi.org.giwitter.R;
import giwi.org.giwitter.adapter.UserAdapter;
import giwi.org.giwitter.helpers.Constants;
import giwi.org.giwitter.helpers.MyRestClient;
import giwi.org.giwitter.helpers.NetworkHelper;
import giwi.org.giwitter.model.User;

/**
 * The type Users fragment.
 */
@EFragment(R.layout.fragment_users)
public class UsersFragment extends Fragment {

    @ViewById
    SwipeRefreshLayout users_swiperefresh;
    @ViewById
    RecyclerView users_list;
    @RestService
    MyRestClient restClient;
    UserAdapter adapter;

    /**
     * Init.
     */
    @AfterViews
    void init() {
        setupRefreshLayout();
        setupRecyclerView();
    }

    /**
     * On resume.
     */
    @Override
    public void onResume() {
        super.onResume();
        loading();
    }

    /**
     * Load messages
     */
    private void loading() {
        if (!NetworkHelper.isInternetAvailable(getActivity())) {
            return;
        }
        users_swiperefresh.setRefreshing(true);
        getUsers();
    }

    /**
     * Gets users.
     */
    @Background
    void getUsers() {
        try {
            displayUsers(restClient.getUsers());
        } catch (RestClientException e) {
            dispalyError(e.getMessage());
        }
    }

    /**
     * Dispaly error.
     *
     * @param message the message
     */
    @UiThread
    void dispalyError(String message) {
        users_swiperefresh.setRefreshing(false);
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Display users.
     *
     * @param contentAsString the content as string
     */
    @UiThread
    void displayUsers(String contentAsString) {
        try {
            JSONArray users = new JSONArray(contentAsString);
            List<User> listOfUsers = new ArrayList<>();
            Log.i(Constants.TAG, contentAsString);
            for (int i = 0; i < users.length(); i++) {
                JSONObject u = users.getJSONObject(i);
                listOfUsers.add(new User(u.getString("username")));
            }
            adapter.setUser(listOfUsers);
            users_swiperefresh.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup refresh layout
     */
    private void setupRefreshLayout() {
        users_swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading();
            }
        });
        users_swiperefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
    }

    /**
     * Setup recycler view.
     */
    private void setupRecyclerView() {
        users_list.setLayoutManager(new LinearLayoutManager(users_list.getContext()));
        adapter = new UserAdapter(this.getActivity());
        users_list.setAdapter(adapter);

        // Add this.
        // Two scroller could have problem.
        users_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                users_list.setEnabled(topRowVerticalPosition >= 0);
            }
        });
    }
}
