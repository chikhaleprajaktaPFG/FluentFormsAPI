package com._4point.aem.docservices.rest_services.server;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {
	public final static Path SAMPLE_FORMS_DIR = Paths.get("src","test", "resources", "SampleForms");
	public final static Path SAMPLE_FORM = SAMPLE_FORMS_DIR.resolve("SampleForm.xdp");
	public final static Path SAMPLE_PDF = SAMPLE_FORMS_DIR.resolve("SampleForm.pdf");
	public final static Path SAMPLE_DDX = SAMPLE_FORMS_DIR.resolve("SampleForm_DDX.xml");
	public final static Path SAMPLE_MERGED_PDF = SAMPLE_FORMS_DIR.resolve("SAMPLE_MERGED_PDF.xml");
}
