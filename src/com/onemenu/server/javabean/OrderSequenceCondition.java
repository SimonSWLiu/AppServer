package com.onemenu.server.javabean;


public class OrderSequenceCondition {

    public OrderSequenceCondition() {
		super();
	}

	public OrderSequenceCondition(String order, String field) {
		super();
		this.order = order;
		this.field = field;
	}

	private String order;
    private String field;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }


}
