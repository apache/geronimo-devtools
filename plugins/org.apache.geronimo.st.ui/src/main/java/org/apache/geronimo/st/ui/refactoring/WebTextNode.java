package org.apache.geronimo.st.ui.refactoring;

public class WebTextNode {
	private String name;
	private String value;
	private int offset;	
	
	public static final String CONTEXT_ROOT="context-root",ARTIFACT_ID="artifactId";
	
	public WebTextNode() {
		this(null,null,-1);
	}

	public WebTextNode(String name, String value, int offset) {
		this.name = name;
		this.value = value;
		this.offset = offset;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
