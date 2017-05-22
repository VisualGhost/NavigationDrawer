package com.mynavigationdrawer;


import android.content.Context;
import android.content.res.TypedArray;

public class DrawerItemsFactory {

    private Model[] mModels;

    public DrawerItemsFactory(String name, String mail, Context context) {

        TypedArray types = context.getResources().obtainTypedArray(R.array.drawer_item_types);
        TypedArray itemsString = context.getResources().obtainTypedArray(R.array.drawer_items);
        TypedArray subHeaders = context.getResources().obtainTypedArray(R.array.drawer_subheaders);
        TypedArray drawableResources = context.getResources().obtainTypedArray(R.array.drawer_icons);

        mModels = new Model[types.length()];

        int strIndex = 0;
        int drawableIndex = 0;
        int modelIndex = 0;
        int subHeaderIndex = 0;

        for (int i = 0; i < types.length(); i++) {
            Model model = new Model();

            model.typeId = types.getResourceId(i, 0);

            if (model.typeId == R.id.drawer_header) {
                model.title = name;
                model.subTitle = mail;
            }

            if (model.typeId == R.id.drawer_item) {

                if (strIndex < itemsString.length()) {
                    int id = itemsString.getResourceId(strIndex++, 0);
                    model.id = id;
                    model.title = context.getString(id);
                }
                if (drawableIndex < drawableResources.length()) {
                    model.iconResId = drawableResources.getResourceId(drawableIndex++, 0);
                }
            }
            if (model.typeId == R.id.drawer_sub_header) {
                if (subHeaderIndex < subHeaders.length()) {
                    model.title = context.getString(subHeaders.getResourceId(subHeaderIndex++, 0));
                }
            }
            mModels[modelIndex++] = model;
        }

        drawableResources.recycle();
        itemsString.recycle();
        subHeaders.recycle();
        types.recycle();
    }

    public Model[] getItems() {
        return mModels;
    }

}
