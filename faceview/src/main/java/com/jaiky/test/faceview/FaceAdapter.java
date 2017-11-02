package com.jaiky.test.faceview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FaceAdapter extends BaseAdapter {

	private Context m_Context;
	private List<String> faceNames = new ArrayList<>();
	public FaceAdapter(Context context, List<String> faceNames) {
		m_Context = context;
		this.faceNames = faceNames;
	}

	@Override
	public int getCount() {
		return faceNames.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(m_Context).inflate(
					R.layout.global_face_item, parent, false);

			holder = new ViewHolder();
			holder.m_imgFace = convertView
					.findViewById(R.id.facelistitem_imgFace);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//判断是否为最后一个item
		if (position == getCount()-1) {
			holder.m_imgFace.setImageResource(R.drawable.compose_emotion_delete);
			holder.m_imgFace.setTag("[delete]");
		} else {
			String emotionName = faceNames.get(position);
			int face = FaceManager.getInstance().getFace(emotionName);
			holder.m_imgFace.setImageResource(face);
			holder.m_imgFace.setTag(emotionName);
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView m_imgFace;
	}
}
