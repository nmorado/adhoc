package dbfit.util;

import fit.Fixture;
import fit.TypeAdapter;

import java.lang.reflect.InvocationTargetException;

public class DbParameterAccessorTypeAdapter extends TypeAdapter {	
	private DbParameterAccessor parameterAccessor;
	public DbParameterAccessorTypeAdapter(DbParameterAccessor accessor,Fixture f){
		this.fixture=f;
		this.type=accessor.getJavaType();
		this.parameterAccessor=accessor;		
	}
	@Override
	public Object get() throws IllegalAccessException,
			InvocationTargetException {
		return parameterAccessor.get();
	}
	@Override
	public void set(Object value) throws Exception {
		parameterAccessor.set(value);
	}
	@Override
	public Object parse(String s) throws Exception {
        Log.log("parsing for type: "+this.type+", for value = "+s);
        System.out.println("parsing for type: "+this.type+", for value = "+s);
		return new ParseHelper(this.fixture,this.type).parse(s);
	}
}
