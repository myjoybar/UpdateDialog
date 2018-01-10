package com.joybar.libupdate;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joybar.libupdate.iml.IConfirmDialog;

import java.util.List;


/**
 * Created by joybar on 6/17/16.
 */
public class UpdateDialog extends Dialog implements android.view.View.OnClickListener {
    private int layoutRes;
    private Context context;
    private boolean mIsForceUpdate;
    private String title;
    private  List<String> contentList;
    public Button confirmBtn;
    private Button cancelBtn;
    TextView txt_title;
    TextView txt_tips;
    LinearLayout lineLayoutContent;

    private String getListStr(List<String> contentList){
        if(null != contentList || contentList.size()!=0){
            int count = contentList.size();
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i<count;i++){
                sb.append((i+1)+". "+contentList.get(i)+"\n");
            }
            return sb.toString();
        }
        return "";
    }

    public UpdateDialog(Context context, String title,  List<String> contentList, boolean
            isForceUpdate) {
        super(context, R.style.mystyle);
        this.context = context;
        this.layoutRes = R.layout.custom_confirmdialog;
        this.title = title;
        this.contentList = contentList;
        mIsForceUpdate = isForceUpdate;
    }


    // 实例化接口
    IConfirmDialog onTouchingLetterChangedListener;

    // 指定事件。。。
    public void setOnTouchingLetterChangedListener(IConfirmDialog onTouchingLetterChangedListener) {

        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public void setMyOnTouchingLetterChangedListener(IConfirmDialog
                                                             onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutRes);

        // 根据id在布局中找到控件对象
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

        // 设置按钮的文本颜色
        //	confirmBtn.setTextColor(R.color.systemcolor);
        //	cancelBtn.setTextColor(R.color.systemcolor);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText(title);

        // 显示提示文本
        lineLayoutContent = findViewById(R.id.ll_content);
        if(null != contentList || contentList.size()!=0) {
            int count = contentList.size();
            for(int i = 0;i<count;i++){
                TextView showText = new TextView(context);
                showText.setTextColor(Color.parseColor("#333333"));
                showText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                showText.setPadding(dp2px(context,8),dp2px(context,2),dp2px(context,8),dp2px(context,2));
                showText.setText((i+1)+". "+contentList.get(i));
                lineLayoutContent.addView(showText);
            }
            if(count>=12){
              LinearLayout lineLayoutContentParent = findViewById(R.id.ll_content_parent);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) lineLayoutContentParent.getLayoutParams();
                layoutParams.height = getScreenHeight(context)*3/7;
                lineLayoutContentParent.setLayoutParams(layoutParams);
            }
        }

        // 为按钮绑定点击事件监听器
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        if (mIsForceUpdate) {
            setCancelable(false);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.confirm_btn) {
            onTouchingLetterChangedListener.doClick();
            this.dismiss();
        } else {
            onTouchingLetterChangedListener.doCancel();
            this.dismiss();
        }

    }


    public static int dp2px(Context context,float dip) {
        float scale = context.getResources()
                .getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


}
