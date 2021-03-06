package com._4point.aem.fluentforms.api.assembler;

import java.util.Map;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.Transformable;
import com._4point.aem.fluentforms.impl.assembler.LogLevel;
import com.adobe.fd.assembler.client.OperationException;
public interface AssemblerService {
	
	AssemblerResult invoke(Document ddx, Map<String,Object> sourceDocuments, AssemblerOptionsSpec assemblerOptionSpec) throws AssemblerServiceException; 

	
	/*
	 * PDFAValidationResult isPDFA(Document inDoc, PDFAValidationOptionSpec options)
	 * throws AssemblerServiceException;
	 * 
	 * PDFAConversionResult toPDFA(Document inDoc, PDFAConversionOptionSpec options)
	 * throws AssemblerServiceException ;
	 */
	
	AssemblerArgumentBuilder invoke();
	
	@SuppressWarnings("serial")
	public static class AssemblerServiceException extends Exception {

		public AssemblerServiceException() {
			super();
		}

		public AssemblerServiceException(String message, Throwable cause) {
			super(message, cause);
		}

		public AssemblerServiceException(String message) {
			super(message);
		}

        public AssemblerServiceException(Throwable cause) {
			super(cause);
		}
        
        
	}
   
	
	public static interface AssemblerArgumentBuilder extends AssemblerOptionsSetter, Transformable<AssemblerArgumentBuilder> {
		
		@Override
		AssemblerArgumentBuilder setFailOnError(Boolean isFailOnError);
		
		@Override
		AssemblerArgumentBuilder setDefaultStyle(String defaultStyle);
	    
		@Override
		AssemblerArgumentBuilder setFirstBatesNumber(int start);
	    
		@Override
		AssemblerArgumentBuilder setLogLevel(LogLevel logLevel);
	    
		@Override
		AssemblerArgumentBuilder setTakeOwnership(Boolean takeOwnership);
	    
		@Override
		AssemblerArgumentBuilder setValidateOnly(Boolean validateOnly);	
       
		public AssemblerResult executeOn(Document ddx, Map<String,Object>sourceDocuments) throws AssemblerServiceException, OperationException;
        	
	}

}
