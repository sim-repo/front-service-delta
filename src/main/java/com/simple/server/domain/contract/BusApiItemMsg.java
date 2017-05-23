package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class BusApiItemMsg extends ALogContract{
	
	private static final long serialVersionUID = 1L;		
	@JsonProperty("id")
	private String id;
	@JsonProperty("top_priority")
	private String topPriority;
	@JsonProperty("title")
	private String title;	
	@JsonProperty("src_endpoint_id")
	private String srcEndpointId;
	@JsonProperty("html_src_output")
	private String htmlSrcOutput;
	@JsonProperty("html_esb_ctrl")
	private String htmlEsbCtrl;
	@JsonProperty("html_dst_input")
	private String htmlDstInput;
	@JsonProperty("html_dst_reply")
	private String htmlDstReply;
	@JsonProperty("html_any_xsmall")
	private String htmlAnyXsmall;
	@JsonProperty("html_any_small")
	private String htmlAnySmall;
	@JsonProperty("html_any_medium")
	private String htmlAnyMedium;
	@JsonProperty("html_any_big")
	private String htmlAnyBig;	
	@JsonProperty("searching_tag1")
	private String searchingTag1;
	@JsonProperty("searching_tag2")
	private String searchingTag2;
	@JsonProperty("searching_tag3")
	private String searchingTag3;
	@JsonProperty("searching_tag4")
	private String searchingTag4;
	@JsonProperty("searching_tag5")	
	private String searchingTag5;
	@JsonProperty("searching_tag6")
	private String searchingTag6;
	@JsonProperty("searching_tag7")
	private String searchingTag7;
	@JsonProperty("searching_tag8")
	private String searchingTag8;
	@JsonProperty("searching_tag9")
	private String searchingTag9;
	@JsonProperty("searching_tag10")
	private String searchingTag10;
	@JsonProperty("searching_tag11")
	private String searchingTag11;
	@JsonProperty("searching_tag12")
	private String searchingTag12;
	@JsonProperty("searching_tag13")
	private String searchingTag13;
	@JsonProperty("searching_tag14")
	private String searchingTag14;
	@JsonProperty("searching_tag15")
	private String searchingTag15;
	@JsonProperty("searching_tag16")
	private String searchingTag16;
	@JsonProperty("searching_tag17")
	private String searchingTag17;
	@JsonProperty("searching_tag18")
	private String searchingTag18;
	@JsonProperty("searching_tag19")
	private String searchingTag19;
	@JsonProperty("searching_tag20")
	private String searchingTag20;
	
	@Override
	public String getClazz() {
		return this.getClass().getName();
	}
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getTopPriority() {
		return topPriority;
	}



	public void setTopPriority(String topPriority) {
		this.topPriority = topPriority;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getSrcEndpointId() {
		return srcEndpointId;
	}



	public void setSrcEndpointId(String srcEndpointId) {
		this.srcEndpointId = srcEndpointId;
	}



	public String getHtmlSrcOutput() {
		return htmlSrcOutput;
	}



	public void setHtmlSrcOutput(String htmlSrcOutput) {
		this.htmlSrcOutput = htmlSrcOutput;
	}



	public String getHtmlEsbCtrl() {
		return htmlEsbCtrl;
	}



	public void setHtmlEsbCtrl(String htmlEsbCtrl) {
		this.htmlEsbCtrl = htmlEsbCtrl;
	}



	public String getHtmlDstInput() {
		return htmlDstInput;
	}



	public void setHtmlDstInput(String htmlDstInput) {
		this.htmlDstInput = htmlDstInput;
	}



	public String getHtmlDstReply() {
		return htmlDstReply;
	}



	public void setHtmlDstReply(String htmlDstReply) {
		this.htmlDstReply = htmlDstReply;
	}



	public String getHtmlAnyXsmall() {
		return htmlAnyXsmall;
	}



	public void setHtmlAnyXsmall(String htmlAnyXsmall) {
		this.htmlAnyXsmall = htmlAnyXsmall;
	}



	public String getHtmlAnySmall() {
		return htmlAnySmall;
	}



	public void setHtmlAnySmall(String htmlAnySmall) {
		this.htmlAnySmall = htmlAnySmall;
	}



	public String getHtmlAnyMedium() {
		return htmlAnyMedium;
	}



	public void setHtmlAnyMedium(String htmlAnyMedium) {
		this.htmlAnyMedium = htmlAnyMedium;
	}



	public String getHtmlAnyBig() {
		return htmlAnyBig;
	}



	public void setHtmlAnyBig(String htmlAnyBig) {
		this.htmlAnyBig = htmlAnyBig;
	}



	public String getSearchingTag1() {
		return searchingTag1;
	}



	public void setSearchingTag1(String searchingTag1) {
		this.searchingTag1 = searchingTag1;
	}



	public String getSearchingTag2() {
		return searchingTag2;
	}



	public void setSearchingTag2(String searchingTag2) {
		this.searchingTag2 = searchingTag2;
	}



	public String getSearchingTag3() {
		return searchingTag3;
	}



	public void setSearchingTag3(String searchingTag3) {
		this.searchingTag3 = searchingTag3;
	}



	public String getSearchingTag4() {
		return searchingTag4;
	}



	public void setSearchingTag4(String searchingTag4) {
		this.searchingTag4 = searchingTag4;
	}



	public String getSearchingTag5() {
		return searchingTag5;
	}



	public void setSearchingTag5(String searchingTag5) {
		this.searchingTag5 = searchingTag5;
	}



	public String getSearchingTag6() {
		return searchingTag6;
	}



	public void setSearchingTag6(String searchingTag6) {
		this.searchingTag6 = searchingTag6;
	}



	public String getSearchingTag7() {
		return searchingTag7;
	}



	public void setSearchingTag7(String searchingTag7) {
		this.searchingTag7 = searchingTag7;
	}



	public String getSearchingTag8() {
		return searchingTag8;
	}



	public void setSearchingTag8(String searchingTag8) {
		this.searchingTag8 = searchingTag8;
	}



	public String getSearchingTag9() {
		return searchingTag9;
	}



	public void setSearchingTag9(String searchingTag9) {
		this.searchingTag9 = searchingTag9;
	}



	public String getSearchingTag10() {
		return searchingTag10;
	}



	public void setSearchingTag10(String searchingTag10) {
		this.searchingTag10 = searchingTag10;
	}



	public String getSearchingTag11() {
		return searchingTag11;
	}



	public void setSearchingTag11(String searchingTag11) {
		this.searchingTag11 = searchingTag11;
	}



	public String getSearchingTag12() {
		return searchingTag12;
	}



	public void setSearchingTag12(String searchingTag12) {
		this.searchingTag12 = searchingTag12;
	}



	public String getSearchingTag13() {
		return searchingTag13;
	}



	public void setSearchingTag13(String searchingTag13) {
		this.searchingTag13 = searchingTag13;
	}



	public String getSearchingTag14() {
		return searchingTag14;
	}



	public void setSearchingTag14(String searchingTag14) {
		this.searchingTag14 = searchingTag14;
	}



	public String getSearchingTag15() {
		return searchingTag15;
	}



	public void setSearchingTag15(String searchingTag15) {
		this.searchingTag15 = searchingTag15;
	}



	public String getSearchingTag16() {
		return searchingTag16;
	}



	public void setSearchingTag16(String searchingTag16) {
		this.searchingTag16 = searchingTag16;
	}



	public String getSearchingTag17() {
		return searchingTag17;
	}



	public void setSearchingTag17(String searchingTag17) {
		this.searchingTag17 = searchingTag17;
	}



	public String getSearchingTag18() {
		return searchingTag18;
	}



	public void setSearchingTag18(String searchingTag18) {
		this.searchingTag18 = searchingTag18;
	}



	public String getSearchingTag19() {
		return searchingTag19;
	}



	public void setSearchingTag19(String searchingTag19) {
		this.searchingTag19 = searchingTag19;
	}



	public String getSearchingTag20() {
		return searchingTag20;
	}



	public void setSearchingTag20(String searchingTag20) {
		this.searchingTag20 = searchingTag20;
	}



	@Override
	public String toString() {
		return "BusApiItemMsg [id=" + id + ", topPriority=" + topPriority + ", title=" + title + ", srcEndpointId="
				+ srcEndpointId + ", htmlSrcOutput=" + htmlSrcOutput + ", htmlEsbCtrl=" + htmlEsbCtrl
				+ ", htmlDstInput=" + htmlDstInput + ", htmlDstReply=" + htmlDstReply + ", htmlAnyXsmall="
				+ htmlAnyXsmall + ", htmlAnySmall=" + htmlAnySmall + ", htmlAnyMedium=" + htmlAnyMedium
				+ ", htmlAnyBig=" + htmlAnyBig + ", searchingTag1=" + searchingTag1 + ", searchingTag2=" + searchingTag2
				+ ", searchingTag3=" + searchingTag3 + ", searchingTag4=" + searchingTag4 + ", searchingTag5="
				+ searchingTag5 + ", searchingTag6=" + searchingTag6 + ", searchingTag7=" + searchingTag7
				+ ", searchingTag8=" + searchingTag8 + ", searchingTag9=" + searchingTag9 + ", searchingTag10="
				+ searchingTag10 + ", searchingTag11=" + searchingTag11 + ", searchingTag12=" + searchingTag12
				+ ", searchingTag13=" + searchingTag13 + ", searchingTag14=" + searchingTag14 + ", searchingTag15="
				+ searchingTag15 + ", searchingTag16=" + searchingTag16 + ", searchingTag17=" + searchingTag17
				+ ", searchingTag18=" + searchingTag18 + ", searchingTag19=" + searchingTag19 + ", searchingTag20="
				+ searchingTag20 + "]";
	}
	
}
