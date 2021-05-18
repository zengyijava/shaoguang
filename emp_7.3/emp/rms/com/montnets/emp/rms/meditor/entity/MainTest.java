package com.montnets.emp.rms.meditor.entity;

import java.util.UUID;

import com.google.gson.Gson;

/**
 * @author Administrator
 *
 */
public class MainTest {
	
	public static void main(String[] args) {
		//卡片公共实体
		CardCommon cardCommon = new CardCommon();
		//卡片主体
		Template tempalte = new Template();
		//属性
		Attr attr  = new Attr();
		//布局
		Layout layout = new Layout();
		
		//组件
		Children children  = new Children();
		//系统ID
		String systemId = "A";
		
		//UUID
		String uuid = UUID.randomUUID().toString().replace("-", "");
		attr.setBgsrc("/imgae/01.png");
		attr.setColor("#fff");
		attr.setSize("12");
		
		layout.setX("10");
		layout.setY("30");
		
		children.setAttr(attr);
		children.setLayout(layout);
		children.setTag("tag1");
		children.setType("image");

		cardCommon.setSid(systemId+uuid);
		Gson gson = new Gson();
		System.out.println(gson.toJson(cardCommon));
	}

}
