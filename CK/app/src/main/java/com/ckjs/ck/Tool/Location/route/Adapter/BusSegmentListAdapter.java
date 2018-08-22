package com.ckjs.ck.Tool.Location.route.Adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RailwayStationItem;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Location.route.Bean.SchemeBusStep;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class BusSegmentListAdapter extends BaseAdapter {
	private Context mContext;
	private List<SchemeBusStep> mBusStepList = new ArrayList<SchemeBusStep>();

	public BusSegmentListAdapter(Context context, List<BusStep> list) {
		this.mContext = context;
		SchemeBusStep start = new SchemeBusStep(null);
		start.setStart(true);
		mBusStepList.add(start);
		for (BusStep busStep : list) {
			if (busStep.getWalk() != null && busStep.getWalk().getDistance() > 0) {
				SchemeBusStep walk = new SchemeBusStep(busStep);
				walk.setWalk(true);
				mBusStepList.add(walk);
			}
			if (busStep.getBusLine() != null) {
				SchemeBusStep bus = new SchemeBusStep(busStep);
				bus.setBus(true);
				mBusStepList.add(bus);
			}
			if (busStep.getRailway() != null) {
				SchemeBusStep railway = new SchemeBusStep(busStep);
				railway.setRailway(true);
				mBusStepList.add(railway);
			}
			
			if (busStep.getTaxi() != null) {
				SchemeBusStep taxi = new SchemeBusStep(busStep);
				taxi.setTaxi(true);
			mBusStepList.add(taxi);
			}
		}
		SchemeBusStep end = new SchemeBusStep(null);
		end.setEnd(true);
		mBusStepList.add(end);
	}
	@Override
	public int getCount() {
		return mBusStepList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBusStepList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_bus_segment, null);
			holder.parent = (RelativeLayout) convertView
					.findViewById(R.id.bus_item);
			holder.busLineName = (TextView) convertView
					.findViewById(R.id.bus_line_name);
			holder.busDirIcon = (SimpleDraweeView) convertView
					.findViewById(R.id.bus_dir_icon);
			holder.busStationNum = (TextView) convertView
					.findViewById(R.id.bus_station_num);
			holder.busExpandImage = (SimpleDraweeView) convertView
					.findViewById(R.id.bus_expand_image);
			holder.busDirUp = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon_up);
			holder.busDirDown = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon_down);
			holder.splitLine = (ImageView) convertView
					.findViewById(R.id.bus_seg_split_line);
			holder.expandContent = (LinearLayout) convertView
					.findViewById(R.id.expand_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final SchemeBusStep item = mBusStepList.get(position);
		if (position == 0) {

			FrescoUtils.setImage(holder.busDirIcon, AppConfig.res+R.drawable.dir_start);
			holder.busLineName.setText("出发");

			MoudleUtils.viewInvisibily(holder.busDirUp);
			MoudleUtils.viewShow(holder.busDirDown);
			MoudleUtils.viewGone(holder.splitLine);
			MoudleUtils.viewGone(holder.busStationNum);
			MoudleUtils.viewGone(holder.busExpandImage);
			return convertView;
		} else if (position == mBusStepList.size() - 1) {

			FrescoUtils.setImage(holder.busDirIcon, AppConfig.res+R.drawable.dir_end);

			holder.busLineName.setText("到达终点");

			MoudleUtils.viewInvisibily(holder.busDirDown);
			MoudleUtils.viewShow(holder.busDirUp);
			MoudleUtils.viewInvisibily(holder.busStationNum);
			MoudleUtils.viewInvisibily(holder.busExpandImage);

			return convertView;
		} else {
			if (item.isWalk() && item.getWalk() != null && item.getWalk().getDistance() > 0) {
				FrescoUtils.setImage(holder.busDirIcon, AppConfig.res+R.drawable.dir13);


				holder.busLineName.setText("步行"
						+ (int) item.getWalk().getDistance() + "米");


				MoudleUtils.viewShow(holder.busDirUp);
				MoudleUtils.viewShow(holder.busDirDown);
				MoudleUtils.viewGone(holder.busStationNum);
				MoudleUtils.viewGone(holder.busExpandImage);
				return convertView;
	
			}else if (item.isBus() && item.getBusLines().size() > 0) {
				FrescoUtils.setImage(holder.busDirIcon, AppConfig.res+R.drawable.dir14);

				MoudleUtils.viewShow(holder.busDirUp);
				MoudleUtils.viewShow(holder.busDirDown);
				MoudleUtils.viewShow(holder.busStationNum);
				MoudleUtils.viewShow(holder.busExpandImage);

				holder.busLineName.setText(item.getBusLines().get(0).getBusLineName());
				holder.busStationNum
						.setText((item.getBusLines().get(0).getPassStationNum() + 1) + "站");
				ArrowClick arrowClick = new ArrowClick(holder, item);
				holder.parent.setTag(position);
				holder.parent.setOnClickListener(arrowClick);
				return convertView;
			} else if (item.isRailway() && item.getRailway() != null) {
				FrescoUtils.setImage(holder.busDirIcon, AppConfig.res+R.drawable.dir16);

				MoudleUtils.viewShow(holder.busDirUp);
				MoudleUtils.viewShow(holder.busDirDown);
				MoudleUtils.viewShow(holder.busStationNum);
				MoudleUtils.viewShow(holder.busExpandImage);

				holder.busLineName.setText(item.getRailway().getName());
				holder.busStationNum
						.setText((item.getRailway().getViastops().size() + 1) + "站");
				ArrowClick arrowClick = new ArrowClick(holder, item);
				holder.parent.setTag(position);
				holder.parent.setOnClickListener(arrowClick);
				return convertView;
			} else if (item.isTaxi() && item.getTaxi() != null) {
				FrescoUtils.setImage(holder.busDirIcon, AppConfig.res+R.drawable.dir14);

				MoudleUtils.viewShow(holder.busDirUp);
				MoudleUtils.viewShow(holder.busDirDown);
				MoudleUtils.viewGone(holder.busStationNum);
				MoudleUtils.viewGone(holder.busExpandImage);


				holder.busLineName.setText("打车到终点");

				return convertView;
			}
			
		}
		return convertView;
	}

	private class ViewHolder {
		public RelativeLayout parent;
		TextView busLineName;
		SimpleDraweeView busDirIcon;
		TextView busStationNum;
		SimpleDraweeView busExpandImage;
		ImageView busDirUp;
		ImageView busDirDown;
		ImageView splitLine;
		LinearLayout expandContent;
		boolean arrowExpend = false;
	}
	
	
	private class ArrowClick implements OnClickListener {
		private ViewHolder mHolder;
		private SchemeBusStep mItem;

		public ArrowClick(final ViewHolder holder, final SchemeBusStep item) {
			mHolder = holder;
			mItem = item;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = Integer.parseInt(String.valueOf(v.getTag()));
			mItem = mBusStepList.get(position);
			if (mItem.isBus()) {
				if (mHolder.arrowExpend == false) {
					mHolder.arrowExpend = true;

					FrescoUtils.setImage(mHolder.busExpandImage,AppConfig.res+R.drawable.up);
					addBusStation(mItem.getBusLine().getDepartureBusStation());
					for (BusStationItem station : mItem.getBusLine()
							.getPassStations()) {
						addBusStation(station);
					}
					addBusStation(mItem.getBusLine().getArrivalBusStation());

				} else {
					mHolder.arrowExpend = false;

					FrescoUtils.setImage(mHolder.busExpandImage,AppConfig.res+R.drawable.down);
					mHolder.expandContent.removeAllViews();
				}
			} else if (mItem.isRailway()) {
				if (mHolder.arrowExpend == false) {
					mHolder.arrowExpend = true;

					FrescoUtils.setImage(mHolder.busExpandImage,AppConfig.res+R.drawable.up);
					addRailwayStation(mItem.getRailway().getDeparturestop());
					for (RailwayStationItem station : mItem.getRailway().getViastops()) {
						addRailwayStation(station);
					}
					addRailwayStation(mItem.getRailway().getArrivalstop());

				} else {
					mHolder.arrowExpend = false;

					FrescoUtils.setImage(mHolder.busExpandImage,AppConfig.res+R.drawable.down);
					mHolder.expandContent.removeAllViews();
				}
			}
			

		}

		private void addBusStation(BusStationItem station) {
			LinearLayout ll = (LinearLayout) View.inflate(mContext,
					R.layout.item_bus_segment_ex, null);
			TextView tv = (TextView) ll
					.findViewById(R.id.bus_line_station_name);
			tv.setText(station.getBusStationName());
			mHolder.expandContent.addView(ll);
		}
		
		private void addRailwayStation(RailwayStationItem station) {
			LinearLayout ll = (LinearLayout) View.inflate(mContext,
					R.layout.item_bus_segment_ex, null);
			TextView tv = (TextView) ll
					.findViewById(R.id.bus_line_station_name);
			tv.setText(station.getName()+ " "+getRailwayTime(station.getTime()));
			mHolder.expandContent.addView(ll);
		}
	}
	public static String getRailwayTime(String time) {
		return time.substring(0, 2) + ":" + time.substring(2, time.length());
	}
}
