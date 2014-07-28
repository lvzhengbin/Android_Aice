package com.ice.android.common.imagecache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 从网络获取图片工具类
 * @author ice
 */
public class ImageHttpUtil {

	private static final String TAG = "ImageHttpUtil";
	
	/**
	 * 请求网络  根据图片url从网络上获取图片
	 * 这里使用 apache的 HttpClient请求网络
	 * @param mImageParams
	 * @return
	 */
	public static Bitmap getImageData(String url){
		HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse response = new DefaultHttpClient().execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
				Log.w(TAG, "Error: "+statusCode+" while retrieving bitmap from "+url);
				return null;
			}
			HttpEntity entity = response.getEntity();
			if(entity != null){
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					/* return BitmapFactory.decodeStream(inputStream);  // Bug on slow connections, fixed in future release.  */
					
					FlushedInputStream fis = new FlushedInputStream(inputStream);
					return BitmapFactory.decodeStream(fis);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}finally{
					if(inputStream != null){
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			getRequest.abort();  // 中断请求
			Log.w(TAG, "Error: "+e.getMessage()+" while retrieving bitmap from "+url);
			e.printStackTrace();
		} catch (IOException e) {
			getRequest.abort();  // 中断请求
			Log.w(TAG, "I/O Error: "+e.getMessage()+" while retrieving bitmap from "+url);
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 继承 FilterInputStream <过滤的输入流>
	 * 能过滤掉/跳过输入流中的某些字节
	 * @author ice
	 *
	 */
	private static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		/**
		 * 重写 skip() 方法
		 * 跳过和丢弃此输入流中数据的 n个字节,完成输入流的字节过滤
		 */
		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
	
	
}
