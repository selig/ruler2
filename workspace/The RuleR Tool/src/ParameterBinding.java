
public class ParameterBinding {
	
	private final Parameter parameterName;
	private final String parameterValue;

	public ParameterBinding(Parameter newParameterName, String newParameterValue) {
		this.parameterName = newParameterName;
		this.parameterValue = newParameterValue;
	}

	public Parameter getParameterName() {
		return parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}
}
