package com._4point.aem.docservices.rest_services.server.forms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._4point.aem.docservices.rest_services.server.Exceptions.BadRequestException;
import com._4point.aem.docservices.rest_services.server.Exceptions.InternalServerErrorException;
import com._4point.aem.docservices.rest_services.server.FormParameters;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.DocumentFactory;
import com._4point.aem.fluentforms.api.forms.FormsService;
import com._4point.aem.fluentforms.api.forms.FormsService.FormsServiceException;
import com._4point.aem.fluentforms.impl.forms.AdobeFormsServiceAdapter;
import com._4point.aem.fluentforms.impl.forms.FormsServiceImpl;
import com._4point.aem.fluentforms.impl.forms.TraditionalFormsService;
import com._4point.aem.fluentforms.testing.MockDocumentFactory;

@Component(service=Servlet.class, property={Constants.SERVICE_DESCRIPTION + "=FormsService.ImportData Service"})
@SlingServletResourceTypes(methods=HttpConstants.METHOD_POST, resourceTypes = { "" })
@SlingServletPaths("/services/FormsService/ImportData")

public class ImportData extends SlingAllMethodsServlet {

	private static final String PDF_PARAM_NAME = "pdf";
	private static final Logger log = LoggerFactory.getLogger(ImportData.class);
	private final TraditionalFormsService adobeFormsService = new AdobeFormsServiceAdapter();
	private final DocumentFactory docFactory = DocumentFactory.getDefault();

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			this.processInput(request, response);
		} catch (BadRequestException br) {
			log.warn("Bad Request from the user", br);
			response.sendError(SlingHttpServletResponse.SC_BAD_REQUEST, br.getMessage());
		} catch (InternalServerErrorException ise) {
			log.error("Internal server error", ise);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, ise.getMessage());
		} catch (Exception e) {  			// Some exception we haven't anticipated.
			log.error(e.getMessage() != null ? e.getMessage() : e.getClass().getName() , e);	// Make sure this gets into our log.
			throw e;
		}
	}

	private void processInput(SlingHttpServletRequest request, SlingHttpServletResponse response) throws InternalServerErrorException, BadRequestException {
		FormsService formsService = new FormsServiceImpl(adobeFormsService);
		
		// TODO: Validate Accept headers
		// TODO: Validate ContentType
		
		
		RequestParameter pdfParameter = FormParameters.getMandatoryParameter(request, PDF_PARAM_NAME);
		Document pdf = docFactory.create(pdfParameter.get());
		
		
//		RequestParameter dataParameter = request.getRequestParameter("data");
//		
//		// TODO: Create Document objects from request parameters.
//		
//		Document pdf = MockDocumentFactory.GLOBAL_DUMMY_DOCUMENT;
		Document data = MockDocumentFactory.GLOBAL_DUMMY_DOCUMENT;
		try {
			try (Document result = formsService.importData(pdf, data)) {
			
				response.setContentType(result.getContentType());
				response.setContentLength((int)result.length());
				transfer(result.getInputStream(), response.getOutputStream());
			}
		} catch (FormsServiceException | IOException ex1) {
			throw new InternalServerErrorException("Internal Error while importing data", ex1);
		} catch (IllegalArgumentException ex2) {
			throw new BadRequestException("Bad arguments while importing data", ex2);
		}
		
	}

	private void transfer(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		// read bytes from the input stream and store them in buffer
		while ((len = in.read(buffer)) != -1) {
			// write bytes from the buffer into output stream
			out.write(buffer, 0, len);
		}
	}
}
