package com.venus.core.builder;

import com.venus.core.AnnotationValues;

public class AnnotationValuesBuilder {
  private AnnotationValues annotationValues;
  public AnnotationValuesBuilder() {
	  this.annotationValues= new AnnotationValues();
  }
  public AnnotationValuesBuilder setSingleton(boolean isSingleton) {
		annotationValues.setSingleton(isSingleton);
		return this;
  }

	public AnnotationValuesBuilder setScope(String scope) {
		annotationValues.setScope(scope);
		return this;	
		
	}
	public AnnotationValuesBuilder setFactoryBean(String factoryBean) {
		annotationValues.setFactoryBean(factoryBean);
		return this;
	}

	public AnnotationValuesBuilder setFactoryMethod(String factoryMethod) {
		annotationValues.setFactoryMethod(factoryMethod);
		return this;
	}
	public AnnotationValuesBuilder setInitMethod(String initMethod) {
		annotationValues.setInitMethod(initMethod);
		return this;	
	}
	public AnnotationValuesBuilder setDestroyMethod(String destroyMethod) {
		annotationValues.setDestroyMethod(destroyMethod);
		return this;	
	}
    public AnnotationValues finish() {
    	return annotationValues;
    }
}
