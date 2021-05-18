package com.montnets.emp.qyll.entity;


public class Products {
	//产品编号
    private String ProductId;
    //产品名称
    private String ProductName; 
    //运营商品牌
    private String ISP;
    //套餐大小
    private String Volume; 
    //价格，如：10.0
    private double Price; 
    //折扣价，如：9.0
    private String DiscountPrice;
    //可服务区域
    private String Area;
    //产品类型
    private int ProductType;
    //产品属性当ProductType=0的时候才有此字段 0:后向1:前向
    private int  ProductMold ; 
    //资源属性 当ProductType=0的时候才有此字段  0-国网包 1-省漫包2-省内包 3-日包 4-三日包 5-周包
    private int Type;
    
    public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getISP() {
		return ISP;
	}
	public void setISP(String iSP) {
		ISP = iSP;
	}
	public String getVolume() {
		return Volume;
	}
	public void setVolume(String volume) {
		Volume = volume;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public String getDiscountPrice() {
		return DiscountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		DiscountPrice = discountPrice;
	}
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	public int getProductType() {
		return ProductType;
	}
	public void setProductType(int productType) {
		ProductType = productType;
	}
	public int getProductMold() {
		return ProductMold;
	}
	public void setProductMold(int productMold) {
		ProductMold = productMold;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
    
}
