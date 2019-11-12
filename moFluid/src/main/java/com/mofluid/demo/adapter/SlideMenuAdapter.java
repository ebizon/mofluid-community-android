package com.mofluid.magento2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.model.SlideMenuListItem;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ebizon on 26/10/15.
 */
public class SlideMenuAdapter extends BaseExpandableListAdapter {
    private final Typeface lat0_Font_ttf;
    private final Context _context;
    private final List<SlideMenuListItem> _listDataHeader; // header titles
    private final HashMap<String, List<SlideMenuListItem>> _listDataChild;
    private final String TAG;
    private RelativeLayout childListItemBG;

    public SlideMenuAdapter(MainActivity context, List<SlideMenuListItem> parentListData, HashMap<String, List<SlideMenuListItem>> slideMenuListData) {

        this._context = context;
        this._listDataHeader = parentListData;
        this._listDataChild = slideMenuListData;
        TAG=SlideMenuAdapter.class.getName();
        lat0_Font_ttf = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getName()).get(childPosititon).getName();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
    if(isLastChild)
    {
        convertView.setBackground(_context.getResources().getDrawable(R.drawable.menuline));
     }
    else {
        convertView.setBackground(null);
    }



        txtListChild.setText(childText);
        txtListChild.setTypeface(lat0_Font_ttf);
        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getName()).size();
        //this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
        //.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        SlideMenuListItem headerTitle = (SlideMenuListItem) getGroup(groupPosition);

        ViewHolderGroup viewHolderGroup;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            viewHolderGroup=new ViewHolderGroup();
            viewHolderGroup.txtGroupTitle=(TextView) convertView.findViewById(R.id.lblListHeader);
            viewHolderGroup.imgGroupIcon=(ImageView) convertView.findViewById(R.id.ivGroupIndicatore);
            childListItemBG = (RelativeLayout)convertView.findViewById(R.id.child_list_item_bg);
//            if(headerTitle.getName().equals(_context.getResources().getString(R.string.shop_category)))
//            {
//                childListItemBG.setBackgroundColor(Color.parseColor("#f0f0f0"));
//            }
//            else
//            {
                childListItemBG.setBackgroundColor(Color.WHITE);
//            }
            convertView.setTag(viewHolderGroup);
        }
        else
        {
            viewHolderGroup=(ViewHolderGroup) convertView.getTag();
        }

        viewHolderGroup.txtGroupTitle.setText(headerTitle.getName());
        viewHolderGroup.txtGroupTitle.setTypeface(lat0_Font_ttf);

        if(isExpanded) {

            viewHolderGroup.imgGroupIcon.setBackgroundResource(R.drawable.up);
            viewHolderGroup.txtGroupTitle.setTypeface(lat0_Font_ttf);
            Log.d(TAG, "expandable list is not expanded "+getChildrenCount(groupPosition));
           /* viewHolderGroup.txtGroupTitle.setTypeface(null, Typeface.BOLD); */
        } else {

            viewHolderGroup.imgGroupIcon.setBackgroundResource(R.drawable.down);
            Log.d(TAG, "expandable list is expanded "+getChildrenCount(groupPosition));
//            viewHolderGroup.txtGroupTitle.setTypeface(null, Typeface.NORMAL);
        }
        if(getChildrenCount(groupPosition)==0 )
            viewHolderGroup.imgGroupIcon.setBackgroundResource(0);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    static class ViewHolderGroup {
        private TextView txtGroupTitle;
        private ImageView imgGroupIcon;
    }

}
