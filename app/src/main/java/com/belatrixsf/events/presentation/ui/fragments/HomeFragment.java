package com.belatrixsf.events.presentation.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.belatrixsf.events.R;
import com.belatrixsf.events.di.component.UIComponent;
import com.belatrixsf.events.domain.model.Event;
import com.belatrixsf.events.presentation.presenters.HomeFragmentPresenter;
import com.belatrixsf.events.presentation.ui.activities.EventDetailActivity;
import com.belatrixsf.events.presentation.ui.base.BelatrixBaseFragment;
import com.belatrixsf.events.utils.Constants;
import com.belatrixsf.events.utils.media.ImageFactory;
import com.belatrixsf.events.utils.media.loaders.ImageLoader;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by dvelasquez
 */
public class HomeFragment extends BelatrixBaseFragment implements HomeFragmentPresenter.View {


    @BindString(R.string.event_title_near)
    String eventTitleNear;
    @BindString(R.string.event_title_past)
    String eventTitlePast;
    @BindView(R.id.image_home)
    ImageView homeImageView;
    @BindDrawable(R.drawable.event_placeholder)
    Drawable placeHolderDrawable;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Inject
    HomeFragmentPresenter presenter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDependencies(UIComponent uiComponent) {
        uiComponent.inject(this);
        presenter.setView(this);
    }

    @OnClick(R.id.image_home)
    public void onClickHomeEvent(){
        startActivity(EventDetailActivity.makeIntent(getActivity(),presenter.getEvent()));
    }

    @Override
    protected void initViews() {
        loadViews();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                loadViews();
            }
        });
    }

    private void loadViews() {
        replaceChildFragment(EventListSummaryFragment.newInstance(Constants.EVENT_TYPE_UPCOMING, eventTitleNear), R.id.frame_events_near);
        replaceChildFragment(EventListSummaryFragment.newInstance(Constants.EVENT_TYPE_PAST, eventTitlePast), R.id.frame_events_past);
        presenter.actionLoadHomeEvent();
    }

    @Override
    public void showHomeEvent(Event event) {
        presenter.setEvent(event);
        ImageFactory.getLoader().loadFromUrl(event.getImage(), homeImageView, null, placeHolderDrawable, ImageLoader.ScaleType.FIT_CENTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}