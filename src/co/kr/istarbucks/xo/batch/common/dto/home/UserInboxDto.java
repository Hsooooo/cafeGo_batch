package co.kr.istarbucks.xo.batch.common.dto.home;

import java.util.Date;

public class UserInboxDto {
	
	private 	String	sui_index;
	private 	String	sui_web_yn;
	private 	String	sui_app_yn;
	private 	String	sui_user_id;
	private 	String	sui_title;
	private 	String	sui_link_yn;
	private 	String	sui_url;
	private 	Date	sui_date;
	private 	String	sui_del_flag;
	private 	String	sui_visit;
	private 	Date	sui_del_date;
	
	public String getSui_index() {
		return sui_index;
	}
	public void setSui_index(String sui_index) {
		this.sui_index = sui_index;
	}
	public String getSui_web_yn() {
		return sui_web_yn;
	}
	public void setSui_web_yn(String sui_web_yn) {
		this.sui_web_yn = sui_web_yn;
	}
	public String getSui_app_yn() {
		return sui_app_yn;
	}
	public void setSui_app_yn(String sui_app_yn) {
		this.sui_app_yn = sui_app_yn;
	}
	public String getSui_user_id() {
		return sui_user_id;
	}
	public void setSui_user_id(String sui_user_id) {
		this.sui_user_id = sui_user_id;
	}
	public String getSui_title() {
		return sui_title;
	}
	public void setSui_title(String sui_title) {
		this.sui_title = sui_title;
	}
	public String getSui_link_yn() {
		return sui_link_yn;
	}
	public void setSui_link_yn(String sui_link_yn) {
		this.sui_link_yn = sui_link_yn;
	}
	public String getSui_url() {
		return sui_url;
	}
	public void setSui_url(String sui_url) {
		this.sui_url = sui_url;
	}
	public Date getSui_date() {
		return sui_date;
	}
	public void setSui_date(Date sui_date) {
		this.sui_date = sui_date;
	}
	public String getSui_del_flag() {
		return sui_del_flag;
	}
	public void setSui_del_flag(String sui_del_flag) {
		this.sui_del_flag = sui_del_flag;
	}
	public String getSui_visit() {
		return sui_visit;
	}
	public void setSui_visit(String sui_visit) {
		this.sui_visit = sui_visit;
	}
	public Date getSui_del_date() {
		return sui_del_date;
	}
	public void setSui_del_date(Date sui_del_date) {
		this.sui_del_date = sui_del_date;
	}
	@Override
	public String toString() {
		StringBuffer builder = new StringBuffer();
		builder.append("UserInboxModel [sui_index=").append(sui_index)
				.append(", sui_web_yn=").append(sui_web_yn)
				.append(", sui_app_yn=").append(sui_app_yn)
				.append(", sui_user_id=").append(sui_user_id)
				.append(", sui_title=").append(sui_title)
				.append(", sui_link_yn=").append(sui_link_yn)
				.append(", sui_url=").append(sui_url).append(", sui_date=")
				.append(sui_date).append(", sui_del_flag=")
				.append(sui_del_flag).append(", sui_visit=").append(sui_visit)
				.append(", sui_del_date=").append(sui_del_date).append("]");
		return builder.toString();
	}
}
