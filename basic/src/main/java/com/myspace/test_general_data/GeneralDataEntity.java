package com.myspace.test_general_data;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class GeneralDataEntity implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	@org.kie.api.definition.type.Label(value = "标识")
	private String flag;
	@org.kie.api.definition.type.Label(value = "数据体")
	private FIleOpLog data;
	@org.kie.api.definition.type.Label(value = "扩展信息")
	private ExtInfo ext;

	public GeneralDataEntity() {
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public FIleOpLog getData() {
		return this.data;
	}

	public void setData(FIleOpLog data) {
		this.data = data;
	}

	public ExtInfo getExt() {
		return this.ext;
	}

	public void setExt(ExtInfo ext) {
		this.ext = ext;
	}

	public GeneralDataEntity(String flag,
                             FIleOpLog data,
                             ExtInfo ext) {
		this.flag = flag;
		this.data = data;
		this.ext = ext;
	}

}
