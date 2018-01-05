package com.joybar.libupdate;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joybar.libupdate.iml.IConfirmDialog;


/**
 * Created by joybar on 6/17/16.
 */
public class UpdateDialog extends Dialog implements
        android.view.View.OnClickListener {
    int layoutRes;// 布局文件
    Context context;
    private boolean mIsForceUpdate;
    String tips;// 提示的内容。。。

    String title;

    /**
     * 确定按钮
     **/
    public Button confirmBtn;
    /**
     * 取消按钮
     **/
    private Button cancelBtn;
    // 内容
    TextView txt_tips;
    TextView txt_title;

    public UpdateDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 自定义布局的构造方法
     *
     * @param context
     * @param resLayout
     */
    public UpdateDialog(Context context, int resLayout) {
        super(context);
        this.context = context;
        this.layoutRes = resLayout;

    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     * @param resLayout
     */

    public UpdateDialog(Context context, int theme, int resLayout, String tips, String title) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
        this.tips = tips;
        this.title = title;

    }

    public UpdateDialog(Context context, String tips, String title, String content, boolean isForceUpdate) {
        super(context, R.style.mystyle);
        this.context = context;
        this.layoutRes = R.layout.custom_confirmdialog;
        this.tips = tips;
        this.title = title;
        mIsForceUpdate = isForceUpdate;
    }


    // 实例化接口
    IConfirmDialog onTouchingLetterChangedListener;

    // 指定事件。。。
    public void setOnTouchingLetterChangedListener(
            IConfirmDialog onTouchingLetterChangedListener) {

        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public void setMyOnTouchingLetterChangedListener(
            IConfirmDialog onTouchingLetterChangedListener) {
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

        // 显示提示文本
        txt_tips = (TextView) findViewById(R.id.txt_tips);
        txt_tips.setText(tips);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText(title);

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

}
