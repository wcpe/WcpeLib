package top.wcpe.wcpelib.model.mybatis;

import org.apache.ibatis.type.JdbcType;

public class TypeHandlerRegistry {

	public TypeHandlerRegistry(Class<?> javaTypeClass, JdbcType jdbcType, Class<?> typeHandlerClass) {
		super();
		this.javaTypeClass = javaTypeClass;
		this.jdbcType = jdbcType;
		this.typeHandlerClass = typeHandlerClass;
	}

	private Class<?> javaTypeClass;
	private JdbcType jdbcType;
	private Class<?> typeHandlerClass;

	public Class<?> getJavaTypeClass() {
		return javaTypeClass;
	}

	public void setJavaTypeClass(Class<?> javaTypeClass) {
		this.javaTypeClass = javaTypeClass;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}

	public Class<?> getTypeHandlerClass() {
		return typeHandlerClass;
	}

	public void setTypeHandlerClass(Class<?> typeHandlerClass) {
		this.typeHandlerClass = typeHandlerClass;
	}
}
