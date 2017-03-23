package eclipse;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseObj {

	private Double hasInput=0d;//已经输入的二维码数量20170323
	private String inputQrs = "";//已经输入的二维码列表20170323

	private String str1;//已用
	private String str2;//已用
	private String str3;//已用
	private String str4;//仓库名称
	/**
	 * 查询模式。仅仅查询未生成二维码的还是已生成二维码
	 */
	private String str5;
	private String str6;//问题订单的操作按钮
	
	private String invName;//用于rdrecords对象中的单品名称
	
	private String qrs;//二维码列表。用于创建订单以及生成二维码
	
	private String cvouchTypeName;//vouchType的中文翻译
	
	private String ddateStr;//日期类型
	private String ddateStr2;//日期类型
	
	private String cusName;//客户名称模糊查询
	private String venName;//供应商名称模糊查询
	
	private String from;//日期的起始
	private String to;//日期的截至
	
	private String optTimeStr;//操作日志表的时间
	
	private String orderType;//单据类型。正单还是负单
	
	private String inWhName;//调拨单，入库仓库
	private String outWhName;//调拨单，出库仓库
	
	private String cusNameLog;//客户信息。日志表中用到
	
	public String getQrs() {
		return qrs;
	}
	public void setQrs(String qrs) {
		this.qrs = qrs;
	}
	public String getStr1() {
//		String s = "<input type='checkbox' name='id[]' value='123'>";
		if (this instanceof Rdrecord ) {
			Rdrecord nn = (Rdrecord) this;
			return "<input type='checkbox' name='id[]' value='123'>";
		}
		return "<input type='checkbox' name='id[]' value='123'>";
	}
	public String getInvName() {
		return invName;
	}
	public void setInvName(String invName) {
		this.invName = invName;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		if (this instanceof Rdrecord) {
			Rdrecord nn = (Rdrecord) this;
			
			String s2 = "<a href='javascript:;' class='btn btn-xs default'><i class='fa fa-search'></i> 订单详情</a> <a class='btn default' data-target='#full-width' data-toggle='modal'>View Demo </a>"
					+ "<a href='javascript:geneQR("+nn.getId()+");' class='btn btn-xs default'><i class='fa fa-search'></i> 生成二维码</a>";
			s2="<a href='javascript:geneQR("+nn.getId()+");' class='btn btn-xs default'><i class='fa fa-flash'></i> 生成二维码并下载</a>";
//			s2+="<a href='javascript:downloadQR("+nn.getId()+");' class='btn btn-xs default'><i class='fa fa-print'></i> 下载打印文件</a>";
			s2+="<a href='javascript:printQRLocal();' class='btn btn-xs default'><i class='fa fa-print'></i> 打印二维码</a>";
			return s2;
		}
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	public String getStr3() {
		if (this instanceof Inventory) {
			Inventory nn = (Inventory) this;
			String s = "<a href='javascript:geneInventoryQR("+nn.getCinvcode()+");' class='btn btn-xs default'><i class='fa fa-flash'></i> 生成二维码并下载</a>";
			s+="<a href='javascript:printQRLocal();' class='btn btn-xs default'><i class='fa fa-print'></i> 打印二维码</a>";
			return s;
		}else if (this instanceof Rdrecord) {
			Rdrecord nn = (Rdrecord) this;
			
			String s2 = "";
			if(nn.getCdefine3()==null){
				s2+="<a href='javascript:scanQR(\""+nn.getId()+"\");' class='btn btn-xs default'><i class='fa fa-delicious'></i> 扫码操作</a>";
			}
			if(StringUtils.equals("01", nn.getCvouchtype()) || StringUtils.equals("10", nn.getCvouchtype()) || StringUtils.equals("08", nn.getCvouchtype())){
				s2="";
				if(nn.getCdefine3()==null){
					s2+="<a href='javascript:geneQR("+nn.getId()+");' class='btn btn-xs default'><i class='fa fa-flash'></i> 生成二维码并下载</a>";
				}
				//s2+="<a href='javascript:downloadQR("+nn.getId()+");' class='btn btn-xs default'><i class='fa fa-download'></i> 下载二维码</a>";
				s2+="<a href='javascript:printQRLocal();' class='btn btn-xs default'><i class='fa fa-print'></i> 打印二维码</a>";
				if(nn.getCdefine3()==null){
					s2 += "<a href='javascript:scanQR(\""+nn.getId()+"\");' class='btn btn-xs default'><i class='fa fa-delicious'></i> 扫码操作</a>";
				}
			}
			if(nn.getCdefine3()!=null){
				s2+="<a href='javascript:viewQR(\"order\",\""+nn.getId()+"\");' class='btn btn-xs default'><i class='fa fa-play'></i> 查看二维码</a>";
			}
			return s2;
		}else if (this instanceof Transvouch) {
			Transvouch nn = (Transvouch) this;
			if(nn.getCdefine3()==null){
				return "<a href='javascript:scanQR(\""+nn.getCtvcode()+"\");' class='btn btn-xs default'><i class='fa fa-delicious'></i> 扫码操作</a>";
			}else{
				return "<a href='javascript:viewQR(\"trans\",\""+nn.getCtvcode()+"\");' class='btn btn-xs default'><i class='fa fa-play'></i> 查看二维码</a>";
			}
		}
			
		return str3;
	}
	public void setStr3(String str3) {
		this.str3 = str3;
	}
	public String getStr4() {
		return str4;
	}
	public void setStr4(String str4) {
		this.str4 = str4;
	}
	public String getStr5() {
		return str5;
	}
	public void setStr5(String str5) {
		this.str5 = str5;
	}

	public String getStr6() {
		if (this instanceof Rdrecord) {
			Rdrecord nn = (Rdrecord) this;
			String s2 = "";
			s2 += "<a href='javascript:delLog("
					+ nn.getId()
					+ ");' class='btn btn-xs default'><i class='fa fa-trash-o'></i> 回退操作记录</a>";
			return s2;
		}
		return "";
	}

	public void setStr6(String str6) {
		this.str6 = str6;
	}
	public String getCvouchTypeName() {
		if (this instanceof Rdrecord) {
			Rdrecord nn = (Rdrecord) this;
			String vn = Const.VOUCH_TYPE.get(nn.getCvouchtype());
			return vn;
		}
		return "----";
	}
	public void setCvouchTypeName(String cvouchTypeName) {
		this.cvouchTypeName = cvouchTypeName;
	}
	public String getDdateStr() {
		if (this instanceof Rdrecord) {
			Rdrecord nn = (Rdrecord) this;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(nn.getDdate()!=null){
				return sdf.format(nn.getDdate());
			}
		}
		if (this instanceof Transvouch) {
			Transvouch nn = (Transvouch) this;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(nn.getDtvdate()!=null){
				return sdf.format(nn.getDtvdate());
			}
		}
		return "----";
	}
	public void setDdateStr(Date ddateStr) {
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getVenName() {
		return venName;
	}
	public void setVenName(String venName) {
		this.venName = venName;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return the optTimeStr
	 */
	public String getOptTimeStr() {
		if (this instanceof QrRecordLog) {
			QrRecordLog nn = (QrRecordLog) this;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(nn.getOptTime()!=null){
				return sdf.format(nn.getOptTime());
			}
		}
		return "";
	}
	/**
	 * @param optTimeStr the optTimeStr to set
	 */
	public void setOptTimeStr(String optTimeStr) {
		this.optTimeStr = optTimeStr;
	}
	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		if(StringUtils.equals("负单", orderType)){
			return orderType;
		}else{
			return "正单";
		}
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getInWhName() {
		return inWhName;
	}
	public void setInWhName(String inWhName) {
		this.inWhName = inWhName;
	}
	public String getOutWhName() {
		return outWhName;
	}
	public void setOutWhName(String outWhName) {
		this.outWhName = outWhName;
	}
	public String getDdateStr2() {
		if (this instanceof Transvouch) {
			Transvouch nn = (Transvouch) this;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(nn.getDverifydate()!=null){
				return sdf.format(nn.getDverifydate());
			}
		}
		return ddateStr2;
	}
	public void setDdateStr2(String ddateStr2) {
		this.ddateStr2 = ddateStr2;
	}
	public String getCusNameLog() {
		return cusNameLog;
	}
	public void setCusNameLog(String cusNameLog) {
		this.cusNameLog = cusNameLog;
	}
	public void setDdateStr(String ddateStr) {
		this.ddateStr = ddateStr;
	}

	public Double getHasInput() {
		return hasInput;
	}

	public void setHasInput(Double hasInput) {
		this.hasInput = hasInput;
	}

	public String getInputQrs() {
		return inputQrs;
	}

	public void setInputQrs(String inputQrs) {
		this.inputQrs = inputQrs;
	}
}
