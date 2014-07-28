package com.ice.android.common.view;

import com.ice.android.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 对话框管理类:显示各种常用样式的对话框<br>
 * 加载对话框|提示对话框|退出/确认取消对话框|输入对话框<br>
 * @author ice
 *
 */
public class DialogManager {
	
	private static Dialog loadingDialog;
	/** 对话框相对于屏幕的宽度  */
	private static final double DIALOG_WIDTH_OF_SCREEN = 0.85; 
	private Dialog dialog;
	
	/**
	 * 构造函数  初始化对话框的默认显示风格 和 布局
	 * @param context
	 */
	public DialogManager(Context context){
		// 初始化对话框的显示风格
		dialog = new Dialog(context, R.style.aice_default_dialog);
		dialog.setContentView(R.layout.aice_dialog_default);
		initDialogWidth(context);
	}
	
	
	/**
	 * 构造函数 初始化指定风格和布局的对话框  一般用于自定义对话框的构造
	 * @param context  上下文
	 * @param styleResId  特定的 R.style.stylename (为 0 时 采用默认的 R.style.aice_default_dialog)
	 */
	public DialogManager(Context context,int styleResId){
		if(styleResId == 0){
			dialog = new Dialog(context, R.style.aice_default_dialog);
		}else{
			dialog = new Dialog(context, styleResId);
		}
		dialog.setContentView(R.layout.aice_dialog_custom);
		initDialogWidth(context);
	}


	/**
	 * 初始化设置对话框相对于屏幕的宽度
	 */
	private void initDialogWidth(Context context) {
		WindowManager wm = ((Activity)context).getWindowManager();
		Display defaultDisplay = wm.getDefaultDisplay();
		LayoutParams attributes = dialog.getWindow().getAttributes();
		attributes.width = (int) (defaultDisplay.getWidth() * DIALOG_WIDTH_OF_SCREEN);
		dialog.getWindow().setAttributes(attributes);
	}
	
	
	
	/**
	 * 显示加载对话框
	 * @param context
	 */
	public static void showLoadingDialog(Context context){
		loadingDialog = new Dialog(context,R.style.aice_default_loading);
		if(loadingDialog != null){
			 synchronized (loadingDialog) {
				 loadingDialog.setContentView(R.layout.aice_dialog_loading);
				 loadingDialog.setCancelable(false);
				 loadingDialog.show();
			}
		}
	}
	
	/**
	 * 隐藏加载对话框
	 * @param context
	 */
	public static void dismissLoadingDialog(){
		if(loadingDialog != null){
			synchronized (loadingDialog){
				if(loadingDialog.isShowing()){
					loadingDialog.dismiss();
					loadingDialog = null;
				}
			}
		}
	}

	
	/**
	 * 显示通用(非自定义)对话框
	 * @param title  对话框标题    null表示无标题   
	 * @param message  对话框内容
	 * @param leftClickListener  对话框左侧按钮点击监听事件(如果没有左侧按钮传null)
	 * @param rightClickListener 对话框右侧按钮点击监听事件(如果没有右侧按钮传null)
	 * @param leftBtnText  左侧按钮文字(如果没有左侧按钮传null)
	 * @param rightBtnText  右侧按钮文字(如果没有右侧按钮传null)
	 */
	public void showCommonDialog(String title,String message,DialogOnclickLinstener leftClickListener,
			DialogOnclickLinstener rightClickListener, String leftBtnText,String rightBtnText){
		TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
		TextView tv_dialog_message = (TextView) dialog.findViewById(R.id.tv_dialog_message);
		Button btn_dialog_left = (Button) dialog.findViewById(R.id.btn_dialog_left);
		Button btn_dialog_right = (Button) dialog.findViewById(R.id.btn_dialog_right);
		View v_btn_divider = dialog.findViewById(R.id.v_btn_divider);
		
		if(title != null){
			tv_dialog_title.setText(title);
		}else{
			tv_dialog_title.setVisibility(View.GONE);
		}
		
		if(message != null){ 
			tv_dialog_message.setText(message);
		}else{
			tv_dialog_message.setVisibility(View.GONE);
		}
		
		if(leftClickListener != null && leftBtnText != null){
			btn_dialog_left.setText(leftBtnText);
			btn_dialog_left.setOnClickListener(leftClickListener);
		}else{
			btn_dialog_left.setVisibility(View.GONE);
			v_btn_divider.setVisibility(View.GONE);
		}
		
		if(rightClickListener != null && rightBtnText != null){
			btn_dialog_right.setText(rightBtnText);
			btn_dialog_right.setOnClickListener(rightClickListener);
		}else{
			btn_dialog_right.setVisibility(View.GONE);
			v_btn_divider.setVisibility(View.GONE);
		}
		
		dialog.setCancelable(false);  // 表示点击对话框之外区域  不会使对话框隐藏
		dialog.show();
	}
	
	
	/**
	 * 显示自定义对话框
	 * @param context 上下文
	 * @param layoutResId  特定的 R.layout.layoutname 布局文件资源Id (只包含Title 和  Content的布局)
	 */
	public void showCustomerDialog(Context context,int layoutResId,DialogOnclickLinstener leftClickListener,
			DialogOnclickLinstener rightClickListener, String leftBtnText,String rightBtnText){
		LinearLayout dialogComposite = (LinearLayout) dialog.findViewById(R.id.ll_custom_dialog_composite);
		Button btn_dialog_left = (Button) dialog.findViewById(R.id.btn_dialog_left);
		Button btn_dialog_right = (Button) dialog.findViewById(R.id.btn_dialog_right);
		View v_btn_divider = dialog.findViewById(R.id.v_btn_divider);
		
		View dialogDetailView = LayoutInflater.from(context).inflate(layoutResId, null);
		dialogDetailView.setLayoutParams(new android.view.ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if(dialogComposite != null){
			dialogComposite.addView(dialogDetailView);
		}
		
		if(leftClickListener != null && leftBtnText != null){
			btn_dialog_left.setText(leftBtnText);
			leftClickListener.addDialogContentLinstener(dialogDetailView);
			btn_dialog_left.setOnClickListener(leftClickListener);
		}else{
			btn_dialog_left.setVisibility(View.GONE);
			v_btn_divider.setVisibility(View.GONE);
		}
		
		if(rightClickListener != null && rightBtnText != null){
			btn_dialog_right.setText(rightBtnText);
			rightClickListener.addDialogContentLinstener(dialogDetailView);
			btn_dialog_right.setOnClickListener(rightClickListener);
		}else{
			btn_dialog_right.setVisibility(View.GONE);
			v_btn_divider.setVisibility(View.GONE);
		}
		
		dialog.setCancelable(false);
		dialog.show();
	}
	
	
	public abstract class DialogOnclickLinstener implements OnClickListener{
		/** 自定义对话框的布局组件   */
		private View dialogContent = null;
		
		@Override
		public void onClick(View view) {
			dialog.dismiss();
			clickEvent(view,dialogContent);
		}
		
		/**
		 * 增加button点击事件对  被分离的自定义对话框组件的监听<br>
		 * <b>注意:</b> 只用于在自定义对话框中才使用该方法
		 * @param v  自定义对话框组件
		 */
		public void addDialogContentLinstener(View v){
			this.dialogContent = v;
		}
		
		/**
		 * 对话框button点击时 被调用
		 * @param view 被点击的button组件对象
		 * @param dialogContent 自定义对话框组件对象   非自定义对话框时传null
		 */
		public abstract void clickEvent(View view,View dialogContent);
	}
	
	
}
