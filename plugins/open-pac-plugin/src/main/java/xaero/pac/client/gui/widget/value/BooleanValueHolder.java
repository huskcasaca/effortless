package xaero.pac.client.gui.widget.value;

public enum BooleanValueHolder {

	TRUE(Boolean.TRUE),
	FALSE(Boolean.FALSE),
	NULL(null);

	private final Boolean value;

	BooleanValueHolder(Boolean value) {
		this.value = value;
	}

	public Boolean getValue() {
		return value;
	}

	public static BooleanValueHolder of(Boolean value){
		if(value == null)
			return NULL;
		return value ? TRUE : FALSE;
	}

}
