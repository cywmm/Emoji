package com.jaiky.test.faceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FaceView extends LinearLayout implements OnPageChangeListener,
        OnItemClickListener, OnClickListener {
    /**
     * Convert to Html Tag, using for webPage
     */
    private static String imgTag = "<img src=\"http://www.host.com/images/-1.png\" border=\"0\" alt=\"\"/>";

    /**
     * Face size
     */
    private int faceDefaultSize = 17;

    private List<View> m_arrFacePageView;
    private ViewPager m_vpFace;
    private List<ImageView> m_arrDotView;
    private LinearLayout m_dotBar;
    private Context m_context;
    private OnFaceSelectedListener m_listener;
    private EditText m_edit;
    private View m_btnView;

    private int faceStyleResource;
    private int textStyleResource;

    /**
     * 实例化方法
     *
     * @param context
     * @param attrs
     */
    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
        LayoutInflater.from(context).inflate(R.layout.global_face_view, this, true);
        initView();
    }

    /**
     * set the button of show or hide the faceview
     *
     * @param btnView
     * @author saderos
     */
    public void setBtnView(View btnView) {
        m_btnView = btnView;
        m_btnView.setOnClickListener(this);
    }

    public void setFaceAndTextStyle(int faceStyle, int textStyle) {
        this.faceStyleResource = faceStyle;
        this.textStyleResource = textStyle;
    }


    /**
     * if you just need to set the editText， and them ,you don't have to append
     * text to the editText yourself
     *
     * @param editText
     * @author saderos
     */
    public void setEdit(EditText editText) {
        m_edit = editText;
        m_edit.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    FaceView.this.setVisibility(View.GONE);
                    setShowImageResource();
                }
                return false;
            }
        });
    }

    public void setHideToggle(View v) {
        v.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    FaceView.this.setVisibility(View.GONE);
                    setShowImageResource();
                }
                return false;
            }
        });
    }

    public void setOnFaceSelectedListener(OnFaceSelectedListener listener) {
        m_listener = listener;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        if (isInEditMode()) {
            return;
        }
        m_vpFace = findViewById(R.id.chat_vpFace);
        m_dotBar = findViewById(R.id.chat_dotbar);
        initFaceBar();
    }

    private void initFaceBar() {
        m_arrFacePageView = new ArrayList<>();
        LinkedHashMap<String, Integer> faceMap = FaceManager.getInstance().getFaceMap();
        List<String> nameList = new ArrayList<>();
        for (String name : faceMap.keySet()) {
            nameList.add(name);
        }

        List<List<String>> namePage = Lists.partition(nameList, 20);

        for (List<String> pageList : namePage) {
            GridView view = new GridView(m_context);
            FaceAdapter adapter = new FaceAdapter(m_context, pageList);
            view.setOnItemClickListener(this);
            view.setAdapter(adapter);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            m_arrFacePageView.add(view);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(m_arrFacePageView);
        m_vpFace.setAdapter(adapter);
        m_vpFace.setCurrentItem(0);
        m_vpFace.setOnPageChangeListener(this);
        initDotBar();

    }

    private void initDotBar() {
        m_arrDotView = new ArrayList<>();

        for (int i = 0; i < m_arrFacePageView.size(); i++) {
            ImageView imgView = new ImageView(m_context);
            imgView.setBackgroundResource(R.drawable.common_indicator_nor);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            DisplayMetrics dm = getResources().getDisplayMetrics();
            params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, dm);
            params.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, dm);
            // params.width = 7;
            // params.height = 7;
            m_dotBar.addView(imgView, params);
            m_arrDotView.add(imgView);
        }
        setSelDot(0);
    }

    private void setSelDot(int nSelIndex) {
        for (int i = 0; i < m_arrDotView.size(); i++) {
            if (nSelIndex == i) {
                m_arrDotView.get(i).setBackgroundResource(
                        R.drawable.common_indicator_checked);
            } else {
                m_arrDotView.get(i).setBackgroundResource(
                        R.drawable.common_indicator_nor);
            }
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setSelDot(arg0);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
        View img = v.findViewById(R.id.facelistitem_imgFace);
        String key = img.getTag().toString();
        if (key.equals("[delete]")){
            // 如果点击了最后一个回退按钮,则调用删除键事件
            m_edit.dispatchKeyEvent(new KeyEvent(
                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                FaceManager.getInstance().getFace(key));
        if (m_listener != null) {
            m_listener.OnFaceSelected(key, bitmap);
        }
        if (m_edit != null) {
            Drawable drawable = new BitmapDrawable(m_context.getResources(), bitmap);
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, faceDefaultSize, getResources().getDisplayMetrics());
            drawable.setBounds(0, 0, size, size);
            ImageSpan imgSpan = new ImageSpan(drawable);

            SpannableString spanString = new SpannableString(key);
            spanString.setSpan(imgSpan, 0, spanString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            m_edit.append(spanString);
        }
    }

    public interface OnFaceSelectedListener {
        void OnFaceSelected(String imgTag, Bitmap face);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }


    @Override
    public void onClick(View v) {
        // 点击输入框，则隐藏表情
        if (v == m_btnView) {
            InputMethodManager imm = (InputMethodManager) m_context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (this.getVisibility() == View.GONE) {
                imm.hideSoftInputFromWindow(getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.setVisibility(View.VISIBLE);
            } else {
                imm.showSoftInput(m_edit, InputMethodManager.SHOW_IMPLICIT);
                this.setVisibility(View.GONE);
            }
            setShowImageResource();
        }

    }


    private void setShowImageResource() {
        if (textStyleResource == 0 || faceStyleResource == 0) {
            //设置默认显示
            if (m_btnView instanceof ImageView) {
                if (getVisibility() == View.GONE)
                    ((ImageView) m_btnView).setImageResource(R.drawable.chatform_face_selector);
                else
                    ((ImageView) m_btnView).setImageResource(R.drawable.chatform_setmodetext_selector);
            }
        } else {
            if (getVisibility() == View.GONE)
                m_btnView.setBackgroundResource(faceStyleResource);
            else
                m_btnView.setBackgroundResource(textStyleResource);
        }
    }

}
