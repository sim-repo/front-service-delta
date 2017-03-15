package com.simple.server.domain.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.simple.server.domain.contract.AContract;
import com.simple.server.domain.contract.SalesLineMsg;

@JsonAutoDetect
public class SalesLineMsg extends AContract{		
	
	protected String outerSorderId = "";
	
	protected String outerLineId = null;
	
	protected String itemNo = null;
		
	protected String quantity = null;	
		
	protected String variantCode = null;

	protected String lineDiscountPercent  = null;
	
	protected String lineAmount = null;
	
	protected String unitPrice = null;

	protected String description  = null;
	
	protected String unitOfMeasure = null;
	
	
	public String getClazz() {
		return SalesLineMsg.class.getSimpleName();
	}
	
	public String getOuterSorderId() {
		return outerSorderId;
	}

	public void setOuterSorderId(String outerSorderId) {
		this.outerSorderId = outerSorderId;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getVariantCode() {
		return variantCode;
	}

	public void setVariantCode(String variantCode) {
		this.variantCode = variantCode;
	}

	public String getLineDiscountPercent() {
		return lineDiscountPercent;
	}

	public void setLineDiscountPercent(String lineDiscountPercent) {
		this.lineDiscountPercent = lineDiscountPercent;
	}

	public String getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(String lineAmount) {
		this.lineAmount = lineAmount;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getOuterLineId() {
		return outerLineId;
	}

	public void setOuterLineId(String outerLineId) {
		this.outerLineId = outerLineId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	@Override
	public String toString() {
		return "SalesLineMsg [outerSorderId=" + outerSorderId + ", outerLineId=" + outerLineId + ", itemNo=" + itemNo
				+ ", quantity=" + quantity + ", variantCode=" + variantCode + ", lineDiscountPercent="
				+ lineDiscountPercent + ", lineAmount=" + lineAmount + ", unitPrice=" + unitPrice + ", description="
				+ description + ", unitOfMeasure=" + unitOfMeasure + "]";
	}

}