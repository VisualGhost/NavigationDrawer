package com.mynavigationdrawer;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerContent {

    interface OnNavigationDrawerClickListener {
        void onClick(int id);
    }

    private DrawerAdapter mDrawerAdapter;

    public DrawerContent(Context context, RecyclerView recyclerView, OnNavigationDrawerClickListener clickListener) {
        mDrawerAdapter = new DrawerAdapter(recyclerView, clickListener);
        recyclerView.setAdapter(mDrawerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public int getActivatedPosition() {
        return mDrawerAdapter != null ? mDrawerAdapter.getSelectedPosition() : -1;
    }

    public void setItems(Model[] items) {
        mDrawerAdapter.setItems(items);
    }

    public void onSaveInstantState(Bundle state) {
        mDrawerAdapter.onSaveInstantState(state);
    }

    public void onRestoreInstantState(Bundle state) {
        mDrawerAdapter.onRestoreInstantState(state);
    }
}

class DrawerAdapter extends RecyclerView.Adapter<Holder> {

    private Model[] mItems;
    private ActiveMode mActiveMode;
    private RecyclerView mRecyclerView;
    private DrawerContent.OnNavigationDrawerClickListener mClickListener;

    public DrawerAdapter(RecyclerView recyclerView, DrawerContent.OnNavigationDrawerClickListener clickListener) {
        mActiveMode = new DrawerActiveMode();
        mRecyclerView = recyclerView;
        mClickListener = clickListener;
    }

    int getSelectedPosition() {
        return mActiveMode.getCheckedPosition();
    }

    public void setItems(Model[] items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.id.drawer_header:
                return new Header(this, getItemView(parent, R.layout.drawer_header_layout));
            case R.id.drawer_item:
                return new Item(this, getItemView(parent, R.layout.drawer_item_layout));
            case R.id.drawer_space:
                return new Space(this, getItemView(parent, R.layout.drawer_space));
            case R.id.drawer_space_half_top:
                return new Space(this, getItemView(parent, R.layout.drawer_space_half_top));
            case R.id.drawer_space_half_bottom:
                return new Space(this, getItemView(parent, R.layout.drawer_space_half_bottom));
            case R.id.drawer_sub_header:
                return new SubHeader(this, getItemView(parent, R.layout.drawer_sub_header));
        }
        return null;
    }

    private View getItemView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId,
                parent,
                false);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bindData(mItems[position]);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems[position].typeId;
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.length : 0;
    }

    void setActiveMode(int position, boolean active) {
        unSelectRow();
        mActiveMode.setActive(position, active);
        mClickListener.onClick(mItems[position].id);
    }

    private void unSelectRow() {
        int previouslyCheckedPosition = mActiveMode.getCheckedPosition();

        if (previouslyCheckedPosition >= 0) {
            Item rowController =
                    (Item) mRecyclerView.findViewHolderForAdapterPosition(previouslyCheckedPosition);

            if (rowController != null) {
                rowController.setActive(false);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(Holder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder.getType() == R.id.drawer_item && holder.getAdapterPosition() != mActiveMode.getCheckedPosition()) {
            ((Item) holder).setActive(false);
        }
    }

    boolean isActive(int position) {
        return mActiveMode.isActive(position);
    }

    void onSaveInstantState(Bundle state) {
        mActiveMode.onSaveInstanceState(state);
    }

    void onRestoreInstantState(Bundle state) {
        mActiveMode.onRestoreInstanceState(state);
    }
}

abstract class Holder extends RecyclerView.ViewHolder {

    Holder(DrawerAdapter adapter, View itemView) {
        super(itemView);
    }

    abstract void bindData(Model model);

    abstract int getType();
}

class Header extends Holder {

    private TextView mName;
    private TextView mEmail;

    Header(DrawerAdapter adapter, View itemView) {
        super(adapter, itemView);
        mName = (TextView) itemView.findViewById(R.id.name);
        mEmail = (TextView) itemView.findViewById(R.id.mail);
    }

    @Override
    void bindData(Model model) {
        mName.setText(model.title);
        mEmail.setText(model.subTitle);
    }

    @Override
    int getType() {
        return R.id.drawer_header;
    }
}

class SubHeader extends Holder {

    private TextView mSubHeaderTextView;

    SubHeader(DrawerAdapter adapter, View itemView) {
        super(adapter, itemView);
        mSubHeaderTextView = (TextView) itemView.findViewById(R.id.drawer_sub_header_text);
    }

    @Override
    void bindData(Model model) {
        mSubHeaderTextView.setText(model.title);
    }

    @Override
    int getType() {
        return R.id.drawer_sub_header;
    }
}

class Item extends Holder {

    private TextView mItemText;
    private ImageView mIcon;
    private DrawerAdapter mAdapter;

    Item(final DrawerAdapter adapter, final View itemView) {
        super(adapter, itemView);
        mAdapter = adapter;
        mIcon = (ImageView) itemView.findViewById(R.id.drawer_item_icon);
        mItemText = (TextView) itemView.findViewById(R.id.drawer_item_text);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setActiveMode(getAdapterPosition(), true);
                itemView.setActivated(true);
            }
        });
        manageRipple(itemView);
    }

    private void manageRipple(View itemView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    View rowContent = view.findViewById(R.id.test);
                    if (rowContent != null) {
                        rowContent
                                .getBackground()
                                .setHotspot(motionEvent.getX(), motionEvent.getY());
                    }
                    return false;
                }
            });
        }
    }


    @Override
    void bindData(Model model) {
        mItemText.setText(model.title);
        itemView.setActivated(mAdapter.isActive(getAdapterPosition()));
        if (model.iconResId != 0) {
            mIcon.setImageResource(model.iconResId);
        }
    }

    void setActive(boolean active) {
        itemView.setActivated(active);
    }

    @Override
    int getType() {
        return R.id.drawer_item;
    }
}

class Space extends Holder {

    Space(DrawerAdapter adapter, View itemView) {
        super(adapter, itemView);
    }

    @Override
    void bindData(Model model) {
        // empty
    }

    @Override
    int getType() {
        return R.id.drawer_space;
    }
}

class Model {
    String title;
    String subTitle;
    int iconResId;
    int typeId;
    int id;
}

interface ActiveMode {

    int getCheckedPosition();

    void setActive(int position, boolean active);

    boolean isActive(int position);

    void onSaveInstanceState(Bundle bundle);

    void onRestoreInstanceState(Bundle bundle);
}

class DrawerActiveMode implements ActiveMode {

    private static final String STATE_ACTIVE_STATES = "activeStates";
    private static final int DEFAULT_POSITION = 2;

    private int activePosition = DEFAULT_POSITION;

    @Override
    public int getCheckedPosition() {
        return activePosition;
    }

    @Override
    public void setActive(int position, boolean active) {
        if (active) {
            activePosition = position;
        } else {
            activePosition = DEFAULT_POSITION;
        }
    }

    @Override
    public boolean isActive(int position) {
        return position == activePosition;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(STATE_ACTIVE_STATES, activePosition);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        activePosition = bundle.getInt(STATE_ACTIVE_STATES);
    }
}
