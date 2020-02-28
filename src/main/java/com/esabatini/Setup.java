package com.esabatini;

import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Setup {

	public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static ObjectMapper OM = new ObjectMapper();
}
