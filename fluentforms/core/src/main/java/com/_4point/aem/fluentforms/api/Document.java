package com._4point.aem.fluentforms.api;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Document extends AutoCloseable, Closeable, HasAttributes {

	void close() throws IOException;

	void copyToFile(File arg0) throws IOException;

	void dispose();

//	Object getAttribute(String name);		// moved to HasAttributes interface

	String getContentType() throws IOException;

	byte[] getInlineData() throws IOException;

	InputStream getInputStream() throws IOException;

	int getMaxInlineSize();

	long length() throws IOException;

	void passivate() throws IOException;

	void removeAttribute(String name);

//	void setAttribute(String name, Object val); 		// moved to HasAttributes interface

	void setContentType(String contentType);

	void setMaxInlineSize(int maxInlineSize);
	
	public default Document setContentTypeIfEmpty(String contentType) throws IOException {
		if (this.getContentType() == null || this.getContentType().isEmpty()) {
			this.setContentType(contentType);
		}
		return this;
	}
	
	public default boolean isEmpty() {
		try {
			return !(this.length() > 1);
		} catch (IOException e) {		// Convert the checked IOException to an unchecked exception.  This should be rare enough that a user shouldn't have to handle it.
			String exMsg = e.getMessage();
			throw new IllegalStateException("I/O Error while determining the length of document." + (exMsg != null ? " (" + exMsg + ")" : ""), e);
		}
	}
	
	public static String CONTENT_TYPE_PDF = "application/pdf";
}