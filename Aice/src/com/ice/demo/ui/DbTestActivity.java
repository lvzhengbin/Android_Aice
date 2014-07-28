package com.ice.demo.ui;

import java.util.List;

import com.ice.android.R;
import com.ice.android.common.core.IceDb;
import com.ice.android.common.db.DBConfig;
import com.ice.android.common.view.BaseActivity;
import com.ice.demo.entity.Person;
import com.ice.demo.entity.Product;
/**
 * DB模块测试
 * @author ice
 *
 */
public class DbTestActivity extends BaseActivity {

	@Override
	public void initView() {
		setTitleStyle(TITLE_STYLE_ONLY_BACK);
		setTitleValue("DB模块测试");
		setTitleBackgroundResource(R.drawable.aice_title_bg_blue);
		setContentLayout(R.layout.test_db);
		
		

	}

	@Override
	public void bindEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initDatas() {
		// DBConfig dbConfig = new DBConfig(DbTestActivity.this);
		
		// IceDb icedb = IceDb.getIceDb(DbTestActivity.this);
		IceDb icedb = IceDb.getIceDb(DbTestActivity.this, "icetest.db", true);
		// 实例化人员对象、并初始化人员数据
		Person p = new Person("ice", 24, "男", false, true);
		// 保存人员信息到数据库
		icedb.save(p);
		// 查找所有的人员数据
		List<Person> persons = icedb.queryAll(Person.class);
		
		// 实例化产品对象、并初始化产品数据
		Product product = new Product("999", "蛋白粉", 255.5, "营养蛋白", "http://pic.xxx.jpg");
		// 保存产品信息到数据库
		icedb.save(product);
		// 查找所有的产品信息
		List<Product> products = icedb.queryAll(Product.class);
		
		/*Person p1 = new Person("janice", 23, "女", true, false);
		icedb.save(p1);*/

	}

}
