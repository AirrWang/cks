package com.ckjs.ck.Tool.Location.route.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.route.DriveStep;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Location.AMapUtil;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;


import java.util.ArrayList;
import java.util.List;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
/**
 * 驾车路线详情页adapter
 * 
 * @author ligen
 * 
 */
public class DriveSegmentListAdapter extends BaseAdapter {
	private Context mContext;
	private List<DriveStep> mItemList = new ArrayList<DriveStep>();

	public DriveSegmentListAdapter(Context context, List<DriveStep> list) {
		this.mContext = context;
		mItemList.add(new DriveStep());
		for (DriveStep driveStep : list) {
			mItemList.add(driveStep);
		}
		mItemList.add(new DriveStep());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_bus_segment_drive,
					null);
			holder.driveDirIcon = (SimpleDraweeView) convertView
					.findViewById(R.id.bus_dir_icon);
			holder.driveLineName = (TextView) convertView
					.findViewById(R.id.bus_line_name);
			holder.driveDirUp = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon_up);
			holder.driveDirDown = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon_down);
			holder.splitLine = (ImageView) convertView
					.findViewById(R.id.bus_seg_split_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final DriveStep item = mItemList.get(position);
		if (position == 0) {

			FrescoUtils.setImage(holder.driveDirIcon, AppConfig.res+R.drawable.dir_start);
			holder.driveLineName.setText("出发");


			MoudleUtils.viewGone(holder.driveDirUp);
			MoudleUtils.viewShow(holder.driveDirDown);
			MoudleUtils.viewGone(holder.splitLine);
			return convertView;
		} else if (position == mItemList.size() - 1) {

			FrescoUtils.setImage(holder.driveDirIcon, AppConfig.res+R.drawable.dir_end);

			holder.driveLineName.setText("到达终点");



			MoudleUtils.viewShow(holder.driveDirUp);
			MoudleUtils.viewGone(holder.driveDirDown);
			MoudleUtils.viewShow(holder.splitLine);
			return convertView;
		} else {
			String actionName = item.getAction();
			int resID = AMapUtil.getDriveActionID(actionName);
			FrescoUtils.setImage(holder.driveDirIcon, AppConfig.res+resID);
			holder.driveLineName.setText(item.getInstruction());


			MoudleUtils.viewShow(holder.driveDirUp);
			MoudleUtils.viewShow(holder.driveDirDown);
			MoudleUtils.viewShow(holder.splitLine);
			return convertView;
		}
		
	}

	private class ViewHolder {
		TextView driveLineName;
		SimpleDraweeView driveDirIcon;
		ImageView driveDirUp;
		ImageView driveDirDown;
		ImageView splitLine;
	}

}
